package models;

import config.Connection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import repositories.Repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

abstract public class Model extends Connection {


    protected static Repository getRepository(Model instance) {
        return Repository.getRepository(instance.getTableName());
    }

    public static int getSize(ResultSet resultSet) {
        int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            if (size > 0) {
                resultSet.beforeFirst();
            }
        } catch (SQLException e) {
            System.out.println("Error while getting size of result set");
            e.printStackTrace();
        }
        return size;
    }

    protected String getPrimaryKey() {
        return "id";
    }

    public String getTableName() {
        TableTest tableTest = getClass().getAnnotation(TableTest.class);
        if (tableTest != null) {
            return tableTest.tableName();
        }
        return null;
    }

    public boolean save() {
        String primaryKey = getPrimaryKey();
        Object primaryKeyValue = getPrimaryKeyValue();
        assert primaryKeyValue != null;
        if (!primaryKeyValue.equals(-1)) {
            return update();
        }
        StringBuilder query = new StringBuilder("INSERT INTO " + getTableName() + " (");
        StringBuilder values = new StringBuilder("VALUES (");
        ArrayList<String> unwanteds = new ArrayList<>(Arrays.asList(primaryKey));
//         filter TableName, empty, null, -1 fields
        Field[] fields = Arrays.stream(getClass().getDeclaredFields()).filter(field -> {
            field.setAccessible(true);
            try {
                return field.get(this) != null
                        && !unwanteds.contains(field.getName())
                        && !field.get(this)
                                 .toString()
                                 .isEmpty()
                        && !field.get(this)
                                 .toString()
                                 .equals("-1");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }).toArray(Field[]::new);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.get(this) != null) {
                    query.append(field.getName());
                    values.append("?");
                    if (i < fields.length - 1) {
                        query.append(", ");
                        values.append(", ");
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error could not access field " + field.getName());
                e.printStackTrace();
            }
        }
        query.append(") ").append(values).append(")");
        query.append(" RETURNING ").append(primaryKey);
        try {
            PreparedStatement preparedStatement = getPreparedStatement(query.toString());
            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (field.get(this) != null) {
                        preparedStatement.setObject(index, field.get(this));
                        index++;
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("Error while saving " + getTableName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                setPrimaryKeyValue(resultSet.getInt(primaryKey));
                refresh();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error while saving to \"" + getTableName() + "\" " + Arrays.toString(fields));
            e.printStackTrace();
            return false;
        }
    }

    public int lastInsertId() {
        try {
            ResultSet resultSet = getPreparedStatement("SELECT last_insert_id()").executeQuery();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error while getting last insert id");
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update() {
        StringBuilder query = new StringBuilder("update " + getTableName() + " set ");
        ArrayList<String> unwanteds = new ArrayList<>(Arrays.asList("id", "tableName"));
        Field[] fields = Arrays.stream(getClass().getDeclaredFields()).filter(field -> {
            field.setAccessible(true);
            try {
                return field.get(this) != null
                        && !unwanteds.contains(field.getName())
                        && !field.get(this)
                                 .toString()
                                 .isEmpty()
                        && !field.get(this)
                                 .toString()
                                 .equals("-1");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }).toArray(Field[]::new);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.get(this) != null) {
                    if (field.get(this)
                             .toString()
                             .equals("now()") || field.getName()
                                                      .equals("updated_at")) {
                        query.append(field.getName()).append(" = CURRENT_TIMESTAMP");
                    } else {
                        query.append(field.getName()).append(" = ?");
                    }
                    if (i < fields.length - 1) {
                        query.append(", ");
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error could not access field " + field.getName());
                e.printStackTrace();
            }
        }
        String primaryKey = getPrimaryKey();
        Object primaryKeyValue = getPrimaryKeyValue();
        query.append(" where ").append(primaryKey).append(" = ?");
        try {
            PreparedStatement preparedStatement = getPreparedStatement(query.toString());
            int index = 1;
            for (Field field : fields) {
                if (field.get(this).toString().equals("now()")) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    if (field.get(this) != null) {
                        preparedStatement.setObject(index, field.get(this));
                        index++;
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("Error while updating " + getTableName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            preparedStatement.setObject(index, primaryKeyValue);
            preparedStatement.executeUpdate();
            refresh();
            return true;
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error while updating to \"" + getTableName() + "\" " + Arrays.toString(fields));
            e.printStackTrace();
            return false;
        }

    }

    @Nullable
    private Object getPrimaryKeyValue() {
        String primaryKey = getPrimaryKey();
        Object primaryKeyValue = null;
        try {
            primaryKeyValue = getClass().getDeclaredField(primaryKey).get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return primaryKeyValue;
    }

    private void setPrimaryKeyValue(Object primaryKeyValue) {
        String primaryKey = getPrimaryKey();
        try {
            getClass().getDeclaredField(primaryKey).set(this, primaryKeyValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean softDelete() {
        String primaryKey = getPrimaryKey();
        String query = "UPDATE " + getTableName() + " SET deleted_at = now() WHERE " + primaryKey + " = ?";
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            assert preparedStatement != null;
            preparedStatement.setObject(1, getClass().getDeclaredField(primaryKey).get(this));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error while soft deleting entity");
            e.printStackTrace();
            return false;
        }
    }

    public void refresh() {
        String primaryKey = getPrimaryKey();
        String query = "SELECT * FROM " + getTableName() + " WHERE " + primaryKey + " = ?";
        ArrayList<String> dateFields = new ArrayList<>(Arrays.asList("created_at", "updated_at", "deleted_at", "published_at"));
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            assert preparedStatement != null;
            preparedStatement.setObject(1, getClass().getDeclaredField(primaryKey).get(this));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                copy the result set to the current object
                for (Field field : getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
//                         get field type if it is int and empty set it to -1
                        Object value = resultSet.getObject(field.getName());
                        if (field.getType().equals(int.class) && value == null) {
                            field.set(this, -1);
                        } else if (dateFields.contains(field.getName()) && value != null) {
                            field.set(this, resultSet.getTimestamp(field.getName()).toString());
                        } else {
                            field.set(this, value);
                        }
                    } catch (IllegalAccessException e) {
                        System.out.println("Error while refreshing " + getTableName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error while fetching entity by primary key");
            e.printStackTrace();
        }
    }

    protected Model parseResultSet(ResultSet resultSet) {
        return null;
    }

    @NotNull
    protected String[] getFields() {
        return Arrays.stream(getClass().getDeclaredFields()).map(Field::getName).toArray(String[]::new);
    }

    protected boolean isTimestamped() {
        return false;
    }

    public void beforeSave() {

    }

    public void beforeUpdate() {

    }

    @Override
    public String toString() {
        System.out.println(getTableName());
        String[] fields = getFields();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Entity: ").append(getClass().getName()).append(" {");
        for (String field : fields) {
            try {
                stringBuilder.append(field)
                             .append(": ")
                             .append(getClass().getDeclaredField(field).get(this))
                             .append(" ");
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

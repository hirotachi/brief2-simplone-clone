package models;

import config.Connection;
import org.jetbrains.annotations.NotNull;
import repositories.Repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

abstract public class Model extends Connection {


    protected static Repository getRepository(String tableName) {
        return Repository.getRepository(tableName);
    }

    protected String getPrimaryKey() {
        return "id";
    }

    private String getTableName() {
        return null;
    }

    public boolean update() {
//         get all properties from class definition
        beforeUpdate();
        String[] fields = getFields();
        StringBuilder query = new StringBuilder("UPDATE " + getTableName() + " SET ");
        for (String field : fields) {
            query.append(field).append(" = ?, ");
        }
        if (isTimestamped()) {
            query.append("updated_at = now(), ");
        }
        // remove last comma
        query = new StringBuilder(query.substring(0, query.length() - 2));
        String primaryKey = getPrimaryKey();
        query.append(" WHERE ").append(primaryKey).append(" = ?");
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query.toString());
            for (int i = 0; i < fields.length; i++) {
                assert preparedStatement != null;
                preparedStatement.setObject(i + 1, getClass().getDeclaredField(fields[i]).get(this));
            }
            assert preparedStatement != null;
//            get type of primary key and add it to the query
            preparedStatement.setObject(fields.length + 1, getClass().getDeclaredField(primaryKey).get(this));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error while updating entity");
            e.printStackTrace();
            return false;
        }
    }

    public boolean save() {
        beforeSave();
        @NotNull String[] fields = getFields();

        StringBuilder query = new StringBuilder("INSERT INTO " + getTableName() + " (");
        for (int i = 0; i < fields.length; i++) {
            query.append(fields[i]);
            if (i != fields.length - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            query.append("?");
            if (i != fields.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query.toString());
            for (int i = 0; i < fields.length; i++) {
                assert preparedStatement != null;
                preparedStatement.setObject(i + 1, (String) getClass().getDeclaredField(fields[i]).get(this));
            }
            assert preparedStatement != null;
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error while saving entity: " + e.getMessage());
            e.printStackTrace();
            return false;
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

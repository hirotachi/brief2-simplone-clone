package models;

import config.Connection;
import org.jetbrains.annotations.NotNull;
import repositories.Repository;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public boolean save() {

        try {
            String primaryKey = getPrimaryKey();
            Object primaryKeyValue = getClass().getDeclaredField(primaryKey).get(this);
            boolean isInsert = primaryKeyValue.equals(-1);
            if (isInsert) {
                beforeSave();
            } else {
                beforeUpdate();
            }

            String[] fields = getFields();
            String operation = isInsert ? "insert into " : "update ";
            StringBuilder query = new StringBuilder(operation + getTableName() + (isInsert
                    ? " ("
                    : " set "));
            for (String field : fields) {
                query.append(field).append(" = ?, ");
            }
            if (isTimestamped()) {
                query.append("updated_at = now(), ");
            }
            // remove last comma
            query = new StringBuilder(query.substring(0, query.length() - 2));
            query.append(" WHERE ").append(primaryKey).append(" = ?");

            PreparedStatement preparedStatement = Connection.getPreparedStatement(query.toString());
            for (int i = 0; i < fields.length; i++) {
                assert preparedStatement != null;
                preparedStatement.setObject(i + 1, getClass().getDeclaredField(fields[i]).get(this));
            }
            assert preparedStatement != null;
//            get type of primary key and add it to the query
            preparedStatement.setObject(fields.length + 1, primaryKeyValue);
            preparedStatement.executeUpdate();
            refresh();
            return true;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            System.out.println("Error while saving entity" + e.getMessage());
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

    public void refresh() {
        String primaryKey = getPrimaryKey();
        String query = "SELECT * FROM " + getTableName() + " WHERE " + primaryKey + " = ?";
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            assert preparedStatement != null;
            preparedStatement.setObject(1, getClass().getDeclaredField(primaryKey).get(this));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                for (String field : getFields()) {
                    getClass().getDeclaredField(field).set(this, resultSet.getObject(field));
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

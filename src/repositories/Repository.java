package repositories;

import config.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class Repository extends Connection {
    public static HashMap<String, Repository> repositories = new HashMap<>();
    private final String tableName;

    private Repository(String tableName) {
        this.tableName = tableName;
    }

    public static Repository getRepository(String name) {
        Repository repository = repositories.get(name);
        if (repository == null) {
            repository = new Repository(name);
            repositories.put(name, repository);
        }
        return repository;
    }

    public ResultSet getByStringFields(String[] fields, String[] values) {
        String query = createSelectQuery(fields);
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            for (int i = 0; i < values.length; i++) {
                assert preparedStatement != null;
                preparedStatement.setString(i + 1, values[i]);
            }
            assert preparedStatement != null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while getting from \"" + getTableName() + "\" by string fields: " + Arrays.toString(fields) + " values:" + Arrays.toString(values));
            return null;
        }
    }

    protected String createSelectQuery(String[] fields) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + getTableName() + " WHERE ");
        for (int i = 0; i < fields.length; i++) {
            query.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                query.append(" AND ");
            }
        }
        return query.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public ResultSet getByIntFields(String[] fields, int[] values) {
        String query = createSelectQuery(fields);
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setInt(i + 1, values[i]);
            }
            assert preparedStatement != null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while getting from \"" + getTableName() + "\" by int fields: " + Arrays.toString(fields) + " values:" + Arrays.toString(values));
            return null;
        }
    }

    public ResultSet getAll() {
        String query = "SELECT * FROM " + getTableName();
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            assert preparedStatement != null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error while getting all from \"" + getTableName() + "\"");
            return null;
        }
    }

    public int count() {
        String query = "SELECT COUNT(*) FROM " + getTableName();
        try {
            PreparedStatement preparedStatement = Connection.getPreparedStatement(query);
            assert preparedStatement != null;
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return 0;
            }
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error while counting from \"" + getTableName() + "\"");
            return 0;
        }
    }
}

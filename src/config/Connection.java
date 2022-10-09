package config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

abstract public class Connection {

    private static java.sql.Connection connection;


    public static java.sql.Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "docker";
                connection = java.sql.DriverManager.getConnection(url, user, password);
                return connection;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }


    protected static PreparedStatement getPreparedStatement(String query) throws SQLException {
        return Objects.requireNonNull(getConnection())
                      .prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

}

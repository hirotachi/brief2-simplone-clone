package models;

import config.Util;
import org.jetbrains.annotations.NotNull;
import repositories.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

//create table if not exists admins(
//        id serial primary key,
//        username varchar(255) not null unique,
//        password varchar(255) not null
//        );
public class Admin extends Model implements Table {
    protected static final String tableName = "admins";
    protected final int id;
    protected final String username;
    protected String password;

    public Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Admin() {
        id = -1;
        username = null;
        password = null;
    }

    public static Admin getByUsername(String username) {
        return fromResultSet(Objects.requireNonNull(Model.getRepository(tableName).getByStringFields(
                new String[]{"username"},
                new String[]{username}
        )));
    }

    private static Admin fromResultSet(ResultSet resultSet) {
        try {
            return new Admin(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Admin[] getAll() {
        return fromResultSetArray(getRepository().getAll());
    }

    private static @NotNull Repository getRepository() {
        return Model.getRepository(tableName);
    }

    private static Admin[] fromResultSetArray(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return null;
            }
            resultSet.last();
            int size = resultSet.getRow();
            resultSet.beforeFirst();
            Admin[] admins = new Admin[size];
            int i = 0;
            while (resultSet.next()) {
                admins[i++] = new Admin(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }
            return admins;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public void beforeSave() {
        password = Util.hashString(password);
    }
}

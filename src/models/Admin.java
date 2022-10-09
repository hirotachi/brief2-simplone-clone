package models;

import config.Util;
import repositories.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

//create table if not exists admins(
//        id serial primary key,
//        username varchar(255) not null unique,
//        password varchar(255) not null
//        );
@TableTest(tableName = "admins")
public class Admin extends Model {
    protected int id;
    protected String username;
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

    public Admin(String username, String password) {
        this(-1, username, password);
    }

    public static Admin getByUsername(String username) {
        return fromResultSet(getRepository().getByStringFields(
                new String[]{"username"},
                new String[]{username}
        ));
    }

    private static Repository getRepository() {
        return Model.getRepository(new Admin());
    }

    private static Admin fromResultSet(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
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


    private static Admin[] fromResultSetArray(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return new Admin[0];
            }
            int size = getSize(resultSet);
            Admin[] admins = new Admin[size];
            for (int i = 0; i < size; i++) {
                resultSet.next();
                admins[i] = fromResultSet(resultSet);
            }
            return admins;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Admin[0];
        }
    }


    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public void beforeSave() {
        password = Util.hashString(password);
    }

    public String getUsername() {
        return username;
    }
}

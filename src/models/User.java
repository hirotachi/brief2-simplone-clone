package models;

import config.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User extends TimestampedModel {
    protected static final String tableName = "users";
    private static final String[] roles = new String[]{"formateur", "apprenant"};
    protected final String email;
    protected final int role;
    protected final String name;
    protected final int id;
    protected String password;
    protected int promo_id;


    public User(String email, String name, String password, int role, int id, int promo_id, String updated_at, String created_at, String deleted_at) {
        super(updated_at, created_at, deleted_at);
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.id = id;
        this.promo_id = promo_id;
    }

    public User() {
        super();
        email = null;
        name = null;
        password = null;
        role = -1;
        id = -1;
        promo_id = -1;
    }

    public static User getByEmail(String email) {
        return (User) Model.getRepository(tableName).getByStringFields(new String[]{"email"}, new String[]{email});
    }

    public static User getById(int id) {
        ResultSet resultSet = Model.getRepository(tableName).getByIntFields(new String[]{"id"}, new int[]{id});
        if (resultSet == null) {
            return null;
        }
        return fromResultSet(resultSet);
    }

    public static User getByRole(int role) {
        return fromResultSet(Objects.requireNonNull(Model.getRepository(tableName).getByIntFields(
                new String[]{"role"},
                new int[]{role}
        )));
    }

    public static User fromResultSet(ResultSet resultSet) {
        try {
            return new User(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getInt("role"),
                    resultSet.getInt("id"),
                    resultSet.getInt("promo_id"),
                    resultSet.getString("updated_at"),
                    resultSet.getString("created_at"),
                    resultSet.getString("deleted_at")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static User[] manyFromResultSet(ResultSet resultSet) {
        try {
            resultSet.beforeFirst();
            resultSet.last();
            int size = resultSet.getRow();
            resultSet.beforeFirst();
            User[] users = new User[size];
            for (int i = 0; i < size; i++) {
                resultSet.next();
                users[i] = fromResultSet(resultSet);
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return roles[role];
    }

    public String getName() {
        return name;
    }

    @Override
    public void beforeSave() {
        password = Util.hashString(password);
    }

    public int getPromo() {
        return promo_id;
    }

    public void setPromo(int promoId) {
        promo_id = promoId;
    }
}

package models;

import config.Util;
import repositories.Repository;
import services.EmailService;
import services.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;


//create table if not exists users
//        (
//        id                   serial primary key,
//        name                 varchar(255) not null,
//        email                varchar(255) not null unique,
//        password             varchar(255) not null,
//        created_at           timestamp default current_timestamp,
//        updated_at           timestamp default current_timestamp,
//        role                 int       default 0,
//        promo_id             int references promos (id),
//        deleted_at           timestamp,
//        last_brief_read_date timestamp
//        );
@TableTest(tableName = "users")
public class User extends TimestampedModel implements Option {
    protected String email;
    protected int role;
    protected String name;
    protected int id;
    protected String password;
    protected int promo_id;
    protected String last_brief_read_date;


    public User(String email, String name, String password, int role, int id, int promo_id, String last_brief_read_date,
                String updated_at,
                String created_at, String deleted_at) {
        super(updated_at, created_at, deleted_at);
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.id = id;
        this.promo_id = promo_id;
        this.last_brief_read_date = last_brief_read_date;
    }

    public User() {
        super();
        email = null;
        name = null;
        password = null;
        role = -1;
        id = -1;
        promo_id = -1;
        last_brief_read_date = null;

    }

    public User(String email, String password, String name, int role) {
        this(email, name, password, role, -1, -1, null, null, null, null);

    }

    public static User getByEmail(String email) {
        return fromResultSet(getRepository().getByStringFields(
                new String[]{"email"},
                new String[]{email}
        ));
    }

    public static User getById(int id) {
        return fromResultSet(getRepository().getByIntFields(
                new String[]{"id"},
                new int[]{id}
        ));
    }

    private static Repository getRepository() {
        return Model.getRepository(new User());
    }


    public static User getByRole(int role) {
        return fromResultSet(getRepository().getByIntFields(
                new String[]{"role"},
                new int[]{role}
        ));
    }

    public static User fromResultSet(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        try {
            return new User(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getInt("role"),
                    resultSet.getInt("id"),
                    resultSet.getInt("promo_id"),
                    resultSet.getString("last_brief_read_date"),
                    resultSet.getString("updated_at"),
                    resultSet.getString("created_at"),
                    resultSet.getString("deleted_at")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static User[] fromResultSetArray(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return new User[0];
            }
            int size = getSize(resultSet);
            User[] users = new User[size];
            for (int i = 0; i < size; i++) {
                resultSet.next();
                users[i] = fromResultSet(resultSet);
            }
            return users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new User[0];
        }
    }

    public static User[] getAll() {
        return fromResultSetArray(getRepository().getAll());
    }

    public static User[] getAllByRole(int role) {
        return fromResultSetArray(getRepository().getByIntFields(
                new String[]{"role"},
                new int[]{role}
        ));
    }

    public static User[] getAllByPromoId(int promoId) {
        return fromResultSetArray(getRepository().getByIntFields(
                new String[]{"promo_id"},
                new int[]{promoId}
        ));
    }

    public static User[] getAllByPromoIdAndRole(int promo_id, int role) {
        return fromResultSetArray(getRepository().getByIntFields(
                new String[]{"promo_id", "role"},
                new int[]{promo_id, role}
        ));
    }


    @Override
    protected User parseResultSet(ResultSet resultSet) {
        return fromResultSet(resultSet);
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

    public Role getRole() {
        return Role.values()[role];
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


    public Promotion getPromotion() {
        if (promo_id == -1) {
            return null;
        }
        return Promotion.getById(promo_id);
    }

    public void notifyAboutBrief(Brief brief) {
        String body = "A new brief (" + brief.getName() + ") has been published to your promotion. Login to the Simplon app to read it.";
        String subject = "Briefing";
        EmailService.send(getEmail(), subject, body);
        Logger.infoln("Sent email to " + getEmail() + " about brief " + brief.getName());
    }

    @Override
    public String toString() {
        return getName() + " - (" + getEmail() + ")" + (getPromotion() != null ? " - " + getPromotion().getName() : "");
    }

}

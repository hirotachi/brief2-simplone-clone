package models;

import repositories.Repository;
import services.Logger;
import services.PromotionService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

//create table if not exists briefs (
//        id serial primary key,
//        name varchar(255) not null,
//        description text not null,
//        created_at timestamp default current_timestamp,
//        updated_at timestamp default current_timestamp,
//        deleted_at timestamp,
//        published_at timestamp,
//        user_id int references users(id),
//        promo_id int references promos(id)
//        );
@TableTest(tableName = "briefs")
public class Brief extends TimestampedModel implements Option {

    protected int id;
    protected String name;
    protected String description;
    protected int user_id;
    protected int promo_id;
    protected String published_at;

    public Brief(int id, String name, String description, int user_id, int promo_id, String updated_at, String created_at, String deleted_at, String published_at) {
        super(updated_at, created_at, deleted_at);
        this.id = id;
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.promo_id = promo_id;
        this.published_at = published_at;
    }

    public Brief() {
        super();
        id = -1;
        name = null;
        description = null;
        user_id = -1;
        promo_id = -1;
        published_at = null;
    }

    public Brief(String name, String description, int userId, int promoId) {
        this(-1, name, description, userId, promoId, null, null, null, null);
    }

    public static Brief getById(int id) {
        return fromResultSet(Objects.requireNonNull(getRepository().getByIntFields(
                new String[]{"id"},
                new int[]{id}
        )));
    }

    public static Brief[] getAll() {
        return fromResultSetArray(getRepository().getAll());
    }

    public static Brief[] getAllByPromoId(int promo_id) {
        return fromResultSetArray(getRepository().getByIntFields(
                new String[]{"promo_id"},
                new int[]{promo_id}
        ));
    }

    public static Brief[] getAllByUserId(int user_id) {
        return fromResultSetArray(getRepository().getByIntFields(
                new String[]{"user_id"},
                new int[]{user_id}
        ));
    }

    private static Repository getRepository() {
        return Model.getRepository(new Brief());
    }

    private static Brief fromResultSet(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        try {
            return new Brief(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("promo_id"),
                    resultSet.getString("updated_at"),
                    resultSet.getString("created_at"),
                    resultSet.getString("deleted_at"),
                    resultSet.getString("published_at")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Brief[] fromResultSetArray(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return new Brief[0];
            }
            int size = getSize(resultSet);
            Brief[] briefs = new Brief[size];
            for (int i = 0; i < size; i++) {
                resultSet.next();
                briefs[i] = fromResultSet(resultSet);
            }
            return briefs;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Brief[0];
        }
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return User.getById(user_id);
    }

    public Promotion getPromotion() {
        return Promotion.getById(promo_id);
    }


    public void publish(boolean publish) {
        published_at = publish ? "now()" : null;
        save();
        PromotionService.notifyPromotion(this, promo_id);
    }

    @Override
    public String toString() {
        if (isPublished()) {
            Logger.success("(Published) ");
        } else {
            Logger.error("(Draft) ");
        }
        return " " + getName() + " - " + getDescription();
    }

    public boolean isPublished() {
        return published_at != null;
    }
}

package models;

import org.jetbrains.annotations.NotNull;
import repositories.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

//create table if not exists promos (
//        id serial primary key,
//        name varchar(255) not null unique,
//        created_at timestamp default current_timestamp,
//        updated_at timestamp default current_timestamp,
//        deleted_at timestamp,
//        year int not null default 2020
//        );
public class Promotion extends TimestampedModel implements Table {
    protected static final String tableName = "promos";

    protected final int id;
    protected final String name;
    protected final int year;

    public Promotion(int id, String name, int year, String updated_at, String created_at, String deleted_at) {
        super(updated_at, created_at, deleted_at);
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public Promotion() {
        super();
        id = -1;
        name = null;
        year = -1;
    }

    public static Promotion getById(int id) {
        return fromResultSet(Objects.requireNonNull(Model.getRepository(tableName).getByIntFields(
                new String[]{"id"},
                new int[]{id}
        )));
    }

    public static Promotion getByName(String name) {
        return fromResultSet(Objects.requireNonNull(Model.getRepository(tableName).getByStringFields(
                new String[]{"name"},
                new String[]{name}
        )));
    }

    public static Promotion getByYear(int year) {
        return fromResultSet(Objects.requireNonNull(getRepository().getByIntFields(
                new String[]{"year"},
                new int[]{year}
        )));
    }

    public static Promotion[] getAll() {
        return fromResultSetArray(getRepository().getAll());
    }

    public static Promotion fromResultSet(ResultSet resultSet) {
        try {
            return new Promotion(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("year"),
                    resultSet.getString("updated_at"),
                    resultSet.getString("created_at"),
                    resultSet.getString("deleted_at")
            );
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Promotion[] fromResultSetArray(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                return null;
            }
            resultSet.last();
            int size = resultSet.getRow();
            resultSet.beforeFirst();
            Promotion[] promotions = new Promotion[size];
            int i = 0;
            while (resultSet.next()) {
                promotions[i++] = fromResultSet(resultSet);
            }
            return promotions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @NotNull
    private static Repository getRepository() {
        return Model.getRepository(tableName);
    }
}

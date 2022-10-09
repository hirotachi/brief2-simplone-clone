package models;

import repositories.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

//create table if not exists promos (
//        id serial primary key,
//        name varchar(255) not null unique,
//        created_at timestamp default current_timestamp,
//        updated_at timestamp default current_timestamp,
//        deleted_at timestamp,
//        year int not null default 2020
//        );
@TableTest(tableName = "promos")
public class Promotion extends TimestampedModel implements Option {
    protected int id;
    protected String name;
    protected int year;

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

    public Promotion(String name, int year) {
        this(-1, name, year, null, null, null);
    }

    public static Promotion getById(int id) {
        return fromResultSet(getRepository().getByIntFields(
                new String[]{"id"},
                new int[]{id}
        ));
    }

    private static Repository getRepository() {
        return Model.getRepository(new Promotion());
    }

    public static Promotion getByName(String name) {
        return fromResultSet(getRepository().getByStringFields(
                new String[]{"name"},
                new String[]{name}
        ));
    }

    public static Promotion getByYear(int year) {
        return fromResultSet(getRepository().getByIntFields(
                new String[]{"year"},
                new int[]{year}
        ));
    }

    public static Promotion[] getAll() {
        return fromResultSetArray(getRepository().getAll());
    }

    public static Promotion fromResultSet(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
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
                return new Promotion[0];
            }
            int size = getSize(resultSet);
            Promotion[] promotions = new Promotion[size];
            for (int i = 0; i < size; i++) {
                resultSet.next();
                promotions[i] = fromResultSet(resultSet);
            }
            return promotions;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Promotion[0];
        }
    }


    public static int count() {
        return getRepository().count();
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }

}

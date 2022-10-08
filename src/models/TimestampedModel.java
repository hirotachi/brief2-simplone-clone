package models;

public class TimestampedModel extends Model {
    protected final String updated_at;
    protected final String created_at;
    protected final String deleted_at;

    public TimestampedModel(String updated_at, String created_at, String deleted_at) {
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
    }

    public TimestampedModel() {
        updated_at = null;
        created_at = null;
        deleted_at = null;
    }

    @Override
    protected boolean isTimestamped() {
        return true;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }
}

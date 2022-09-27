public class Command {

    private final String description;

    private final Runnable action;

    public Command(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }


    public String getDescription() {
        return description;
    }

    public void run() {
        action.run();
    }
}

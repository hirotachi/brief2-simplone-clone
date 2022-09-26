public class Command {

    private final String name;
    private final String description;

    private final Runnable action;

    public Command(String name, String description, Runnable action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void run() {
        action.run();
    }
}

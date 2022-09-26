import java.util.ArrayList;
import java.util.Arrays;

public class Admin implements Commander {
    private final ArrayList<Command> commands = new ArrayList<Command>();
    private final String username;
    private final String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        assignCommands(
                new Command("create user", "Create user", () -> {
                    System.out.println("Create user");
                }),
                new Command("update user", "Update user", () -> {
                    System.out.println("Update user");
                })
        );
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void assignCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    @Override
    public ArrayList<Command> getCommands() {
        return commands;
    }
}

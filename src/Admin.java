import java.util.ArrayList;
import java.util.Arrays;


public class Admin implements Commander {
    private final ArrayList<Command> commands = new ArrayList<Command>();
    private final String username;
    private final String password;

    private final int id;

    public Admin(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
        assignCommands(
                new Command("Create formatteur", Formateur::createFormatteur),
                new Command("Create Apprenant", Apprenant::createApprenant),
                new Command("List Formatteurs", Formateur::listFormatteurs),
                new Command("List Apprenant", Apprenant::listApprenants),
                new Command("Assign Formatteur to promotion", () -> {
                    User.assignPromotion(Role.FORMATTEUR);
                }),
                new Command("Assign Apprenant to promotion", () -> {
                    User.assignPromotion(Role.FORMATTEUR);
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


    public int getId() {
        return id;
    }
}

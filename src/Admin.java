import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Admin implements Commander {

    private static HashMap<String, Admin> adminsByUsername;
    private static int nextAdminId = 1;

    private final ArrayList<Command> commands = new ArrayList<Command>();
    private final String username;
    private final String password;

    private final int id;

    public Admin(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
        assignCommands(
                new Command("Create formatteur", Formateur::create),
                new Command("Create Apprenant", Apprenant::create),
                new Command("Create Promotion", Promotion::create),
                new Command("List Formatteurs", Formateur::list),
                new Command("List Apprenant", Apprenant::list),
                new Command("List Promotions", Promotion::list),
                new Command("Assign Formatteur to promotion", () -> {
                    User.assignPromotion(Role.FORMATTEUR);
                }),
                new Command("Assign Apprenant to promotion", () -> {
                    User.assignPromotion(Role.APPRENANT);
                })
        );
    }

    public static Admin getAdminByUsername(String username) {
        return adminsByUsername.get(username);
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

    public static void parseAndLoad(HashMap<String, Admin> adminsByUsername) {
        Admin.adminsByUsername = adminsByUsername;
    }

    private static int getNextAdminId() {
        return nextAdminId++;
    }

    public static void add(String username, String password) {
        Admin admin = new Admin(username, password, getNextAdminId());
        adminsByUsername.put(admin.getUsername(), admin);
    }
}

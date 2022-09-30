import services.EmainService;

import java.util.ArrayList;
import java.util.HashMap;


public class Admin implements Commander {

    private static HashMap<String, Admin> adminsByUsername;
    private static int nextAdminId = 1;

    private final String username;
    private final String password;

    private final int id;

    public Admin(String username, String password, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
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


    public static ArrayList<Command> getCommands() {
        return new ArrayList<>() {{
            add(new Command("Create formatteur", Formateur::create));
            add(new Command("Create Apprenant", Apprenant::create));
            add(new Command("Create Promotion", Promotion::create));
            add(new Command("List Formatteurs", Formateur::list));
            add(new Command("List Apprenant", Apprenant::list));
            add(new Command("List Promotions", Promotion::list));
            add(new Command("Assign Formatteur to promotion", () -> {
                User.assignPromotion(Role.FORMATTEUR);
            }));
            add(new Command("Assign Apprenant to promotion", () -> {
                User.assignPromotion(Role.APPRENANT);
            }));

            add(new Command("Send test email", () -> {
                EmainService.send("saidoudouane@gmail.com", "test subject", "test description");
                Logger.successln("Email sent successfully");
            }));

        }};
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

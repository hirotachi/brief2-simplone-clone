import java.util.HashMap;

public class State {

    private static HashMap<String, User> usersByEmail;
    private static HashMap<String, Admin> adminsByUsername;


    public static void load() {
        usersByEmail = new HashMap<>();
        adminsByUsername = new HashMap<>();
        usersByEmail.put("said", new Formateur("said", 1, "said", "said", Role.FORMATTEUR, 1));
        adminsByUsername.put("admin", new Admin("admin", "admin"));
    }

    public static User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    public static Admin getAdminByUsername(String username) {
        return adminsByUsername.get(username);
    }

    public static void addUser(User user) {
        usersByEmail.put(user.getEmail(), user);
    }

    public static void addAdmin(Admin admin) {
        adminsByUsername.put(admin.getUsername(), admin);
    }
}

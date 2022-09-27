import java.util.ArrayList;
import java.util.HashMap;

public class State {

    private static HashMap<String, User> usersByEmail;
    private static HashMap<String, Admin> adminsByUsername;

    private static HashMap<Integer, Promotion> promotionsById;

    private static int nextUserId = 1;
    private static int nextPromoId = 1;

    private static int nextAdminId = 1;


    public static void load() {
        usersByEmail = new HashMap<>();
        adminsByUsername = new HashMap<>();
        promotionsById = new HashMap<>();

//        static data for testing

        addFormatteur("formatteur", "formatteur", "formatteur");
        addApprenant("apprenant", "apprenant", "apprenant");
        addAdmin("admin", "admin");
        addPromotion("Promotion 1");
        addPromotion("Promotion 2");
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

    public static void addAdmin(String username, String password) {
        Admin admin = new Admin(username, password, getNextAdminId());
        adminsByUsername.put(admin.getUsername(), admin);
    }


    public static Formateur addFormatteur(String email, String name, String password) {
        if (usersByEmail.containsKey(email)) {
            throw new RuntimeException("Email already exists");
        }
        Formateur formateur = new Formateur(email, getNextUserId(), password, name);
        usersByEmail.put(email, formateur);
        return formateur;
    }

    public static Apprenant addApprenant(String email, String name, String password) {
        if (usersByEmail.containsKey(email)) {
            throw new RuntimeException("Email already exists");
        }
        Apprenant apprenant = new Apprenant(email, getNextUserId(), password, name);
        usersByEmail.put(email, apprenant);
        return apprenant;
    }

    private static int getNextUserId() {
        return nextUserId++;
    }

    private static int getNextPromoId() {
        return nextPromoId++;
    }

    private static int getNextAdminId() {
        return nextAdminId++;
    }

    public static ArrayList<User> getUsersByPromoId(int promoId) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : usersByEmail.values()) {
            if (user.getPromoId() == promoId) {
                users.add(user);
            }
        }
        return users;
    }


    public static ArrayList<Option> getPromotionsAsOptions() {
        return new ArrayList<>(promotionsById.values());
    }

    public static ArrayList<Option> getUsersByRoleAsOptions(Role role) {
        ArrayList<Option> options = new ArrayList<>();
        for (User user : usersByEmail.values()) {
            if (user.getRole() == role) {
                options.add(user);
            }
        }
        return options;
    }


    public static Promotion getPromotionById(int id) {
        return promotionsById.get(id);
    }

    public static Promotion addPromotion(String name) {
        Promotion promotion = new Promotion(name, getNextPromoId());
        promotionsById.put(promotion.getId(), promotion);
        return promotion;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class User extends Option implements Commander {
    protected static HashMap<String, User> usersByEmail;
    protected static HashMap<Integer, User> usersById;
    private static int nextUserId = 1;
    private final ArrayList<Command> commands = new ArrayList<Command>();

    private int promoId;
    private String email;

    private int id;
    private String password;
    private String name;

    private Role role;

    public User(String email, int id, String password, String name, Role role) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public static void parseAndLoad(HashMap<String, User> usersByEmail) {
        User.usersByEmail = usersByEmail;
        User.usersById = new HashMap<>();
        for (User user : usersByEmail.values()) {
            usersById.put(user.getId(), user);
        }
    }

    public static User getByEmail(String email) {
        return usersByEmail.get(email);
    }

    public static void addUser(User user) {
        if (User.usersByEmail.containsKey(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        usersByEmail.put(user.getEmail(), user);
        usersById.put(user.getId(), user);
    }

    static int getNextUserId() {
        return nextUserId++;
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

    public static ArrayList<Option> asOptions(Role role) {
        ArrayList<Option> options = new ArrayList<>();
        for (User user : usersByEmail.values()) {
            if (user.getRole() == role) {
                options.add(user);
            }
        }
        return options;
    }

    private static ArrayList<Option> asOptions() {
        return new ArrayList<>(usersByEmail.values());
    }

    public void assignCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }


    public void assignToPromo(Boolean showConfirmation) {

    }

    @Override
    public String toString() {
        return this.getName() + " - (" + this.getEmail() + ")" + (this.getPromotion() != null ? " - " + this.getPromotion().getName() : "");
    }


    public static void assignPromotion(Role role) {
        int count = Promotion.count();
        if (count == 0) {
            Logger.errorln("No promotions found");
            return;

        }
        ArrayList<Option> usersByRoleAsOptions = asOptions(role);
        int option = CMD.chooseOption(usersByRoleAsOptions, true);
        if (option == 0) {
            Logger.warningln("Cancelled promotion assignment");
            return;
        }
        User user = (User) usersByRoleAsOptions.get(option);
        Promotion.assignPromotion(user, false);
        Logger.successln("Promotion " + user.getPromotion().getName() + " assigned to " + user.getName());
    }

    public Promotion getPromotion() {
        return Promotion.getById(this.getPromoId());
    }

    public static User getById(int id) {
        return usersById.get(id);
    }

}

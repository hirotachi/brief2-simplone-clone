import services.EmailService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class User extends Option implements Commander {
    protected static HashMap<String, User> usersByEmail;
    protected static HashMap<Integer, User> usersById;
    private static int nextUserId = 1;
    private LocalDateTime lastBriefReadDate;

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

    public static ArrayList<Command> getCommands() {
        return new ArrayList<>();
    }

    public static void assignPromotion(Role role) {
        int count = Promotion.count();
        if (count == 0) {
            Logger.errorln("No promotions found, one needs to be created first");
            if (Auth.getAdmin() == null) {
                return; // only admins can create promotions
            }

            boolean createPromotion = CMD.getConfirmation("Create a promotion?");
            if (!createPromotion) {
                return;
            }
            Promotion.create();
        }
        ArrayList<Option> usersByRoleAsOptions = asOptions(role);
        int option = CMD.chooseOption(usersByRoleAsOptions, true);
        if (option == -1) {
            Logger.warningln("Cancelled promotion assignment");
            return;
        }
        User user = (User) usersByRoleAsOptions.get(option);
        Promotion.assignPromotion(user, false);
        Logger.successln("Promotion " + user.getPromotion().getName() + " assigned to " + user.getName());
    }

    public static User getById(int id) {
        return usersById.get(id);
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public void assignToPromo(Boolean showConfirmation) {

    }

    @Override
    public String toString() {
        return getName() + " - (" + getEmail() + ")" + (getPromotion() != null ? " - " + getPromotion().getName() : "");
    }

    public Promotion getPromotion() {
        if (getPromoId() == 0) {
            return null;
        }
        return Promotion.getById(getPromoId());
    }

    public LocalDateTime getLastBriefReadDate() {
        return lastBriefReadDate;
    }

    public void setLastBriefReadDate(LocalDateTime lastBriefReadDate) {
        this.lastBriefReadDate = lastBriefReadDate;
    }

    public void notifyAboutBrief(Brief brief) {
        String body = "A new brief (" + brief.getName() + ") has been published to your promotion. Login to the Simplon app to read it.";
        String subject = "Briefing";
        EmailService.send(getEmail(), subject, body);
    }
}

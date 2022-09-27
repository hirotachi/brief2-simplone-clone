import java.util.ArrayList;
import java.util.Arrays;

public class User extends Option implements Commander {
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
        ArrayList<Option> usersByRoleAsOptions = State.getUsersByRoleAsOptions(role);
        int option = CMD.chooseOption(usersByRoleAsOptions, true);
        if (option == 0) {
            Logger.warningln("Cancelled promotion assignment");
            return;
        }
        User user = (User) usersByRoleAsOptions.get(option - 1);
        Promotion.assignPromotion(user, false);
        Logger.successln("Promotion " + user.getPromotion().getName() + " assigned to " + user.getName());
    }

    public Promotion getPromotion() {
        return State.getPromotionById(this.getPromoId());
    }

}

import java.util.ArrayList;
import java.util.Arrays;

public class User implements Commander {
    private final ArrayList<Command> commands = new ArrayList<Command>();

    private int promoId;
    private String email;

    private int id;
    private String password;
    private String name;

    private Role role;

    public User(String email, int id, String password, String name, Role role, int promoId) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
        this.promoId = promoId;
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
}

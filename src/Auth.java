import models.Admin;
import models.Role;
import models.User;

public class Auth {
    private static User user;
    private static Admin admin;

    private static Role LoggedInRole;

    public static void authenticate() {
        String username = CMD.getInput("Enter your email/Username: ").toLowerCase();
        String password = CMD.getHiddenInput("Enter your password: ");
        admin = Admin.getByUsername(username);
        if (admin != null && !admin.verifyPassword(password)) {
            authenticate(true);
            return;
        }
        if (admin != null) {
            setAdmin(admin);
            setLoggedInRole(Role.ADMIN);
            return;
        }
        user = User.getByEmail(username);
        if (user == null || !user.verifyPassword(password)) {
            authenticate(true);
            return;
        }
        if (user != null) {
            setUser(user);
            setLoggedInRole(user.getRole());
            return;
        }

        authenticate(true);


    }

    public static void authenticate(Boolean showError) {
        if (showError) {
            Logger.errorln("Invalid email or password.");
        }
        authenticate();
    }

    public static void logout() {
        setUser(null);
        setAdmin(null);
        setLoggedInRole(null);
        Logger.logln("You have been logged out!");
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Auth.user = user;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static void setAdmin(Admin admin) {
        Auth.admin = admin;
    }

    public static Role getLoggedInRole() {
        return LoggedInRole;
    }

    public static void setLoggedInRole(Role loggedInRole) {
        LoggedInRole = loggedInRole;
    }
}

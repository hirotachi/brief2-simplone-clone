import models.User;

public class Main {
    public static void main(String[] args) {
        CMD.welcome();
//        State.load(); // Load the state from the permanent storage
//        start();
        User user = User.getByEmail("tester@gmail.com");
        if (user != null) {
            System.out.println(user.getName());
        } else {
            System.out.println("User not found");
        }
    }

    public static void start() {
        //         authenticate -> loop when email or password are wrong
//        Auth.authenticate();
//        boolean isAdmin = Auth.getLoggedInRole() == Role.ADMIN;
////         show welcome message to user by name
//        Logger.successln("Welcome " + (isAdmin ? Auth.getAdmin().getUsername() : Auth.getUser().getName()));
//        Commander commander = isAdmin ? Auth.getAdmin() : Auth.getUser();
////         list all commands
////         listen to command and run command accordingly
//        CMD.listAndListen(Command.getCurrentUserCommands());
    }
}
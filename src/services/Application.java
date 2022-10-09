package services;

public class Application {
    public static void start() {
        //         authenticate -> loop when email or password are wrong
        AuthService.authenticate();
        boolean isAdmin = AuthService.getLoggedInRole() == models.Role.ADMIN;
//         show welcome message to user by name
        Logger.successln("Welcome " + (isAdmin
                ? AuthService.getAdmin().getUsername()
                : AuthService.getUser().getName()));
//         list all commands
//         listen to command and run command accordingly
        CMD.listAndListen(Command.getCurrentUserCommands());
    }
}

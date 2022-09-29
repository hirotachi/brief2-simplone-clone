import java.util.ArrayList;

public class Command {

    private final String description;

    private final Runnable action;

    public Command(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }


    public String getDescription() {
        return description;
    }

    public void run() {
        action.run();
    }


    public static ArrayList<Command> getCurrentUserCommands(){
        return switch (Auth.getLoggedInRole()) {
            case FORMATTEUR -> Formateur.getCommands();
            case APPRENANT -> Apprenant.getCommands();
            case ADMIN -> Admin.getCommands();
        };
    }
}

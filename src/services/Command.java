package services;

import java.util.ArrayList;

public class Command {

    private final String description;

    private final Runnable action;

    public Command(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }

    public static ArrayList<Command> getCurrentUserCommands() {
        return switch (AuthService.getLoggedInRole()) {
            case FORMATTEUR -> FormateurService.getCommands();
            case APPRENANT -> ApprenantService.getCommands();
            case ADMIN -> AdminService.getCommands();
        };
    }

    public String getDescription() {
        return description;
    }

    public void run() {
        action.run();
    }
}

import models.Role;

import java.util.ArrayList;

public class Formateur extends User {

    public Formateur(String email, int id, String password, String name) {
        super(email, id, password, name, Role.FORMATTEUR);
    }

    public static ArrayList<Command> getCommands() {
        if (Auth.getUser().getPromotion() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>() {{
            add(new Command("Add brief", Brief::create));
            add(new Command("Publish a brief", Brief::publish));
            add(new Command("List briefs", Brief::list));
            add(new Command("Assign Apprenant to Promotion", () -> {
                User.assignPromotion(Role.APPRENANT);
            }));
        }};
    }


    public static void create() {
        String email = CMD.getInput("Enter formatteur email:").toLowerCase();
        boolean exists = User.getByEmail(email) != null;
        if (exists) {
            Logger.errorln("Email already used");
            create();
            return;
        }
        String name = CMD.getInput("Enter formatteur name:");
        String password = CMD.getHiddenInput("Enter formatteur password:");
        Formateur formateur = add(email, name, password);
        Promotion.assignPromotion(formateur, true);
        Logger.successln("Formatteur (" + formateur.getName() + ") created successfully");
    }

    public static void list() {
        ArrayList<Option> formatteurs = asOptions();

        int size = formatteurs.size();
        if (size == 0) {
            Logger.warningln("No formatteurs found");
            return;
        }
        Logger.logln("Formatteurs (" + size + "): ****************************************");
        CMD.listOptions(formatteurs);
        Logger.logln("*****************************************************");
    }


    public static Formateur add(String email, String name, String password) {
        Formateur formateur = new Formateur(email, User.getNextUserId(), password, name);
        User.addUser(formateur);
        return formateur;
    }

    private static ArrayList<Option> asOptions() {
        return User.asOptions(Role.FORMATTEUR);
    }
}

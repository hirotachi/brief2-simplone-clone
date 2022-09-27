public class Formateur extends User {

    public Formateur(String email, int id, String password, String name) {
        super(email, id, password, name, Role.FORMATTEUR);
        this.assignCommands(
                new Command("Add brief", () -> {
                    System.out.println("Create brief");
                }),
                new Command("Update brief", () -> {
                    System.out.println("Update brief");
                }));
    }

    @Override
    public String toString() {
        return "Formatteur: " + this.getName() + " " + this.getEmail();
    }

    public static void createFormatteur() {
        String email = CMD.getInput("Enter formatteur email:");
        boolean exists = State.getUserByEmail(email) != null;
        if (exists) {
            Logger.errorln("Email already used");
            createFormatteur();
            return;
        }
        String name = CMD.getInput("Enter formatteur name:");
        String password = CMD.getHiddenInput("Enter formatteur password:");
        Formateur formateur = State.addFormatteur(email, name, password);
        Promotion.assignPromotion(formateur, true);
        Logger.successln("Formatteur (" + formateur.getName() + ") created successfully");
    }

    public static void listFormatteurs() {
        Logger.logln("****************************************");
        CMD.listOptions(State.getUsersByRoleAsOptions(Role.FORMATTEUR));
        Logger.logln("****************************************");
    }


}

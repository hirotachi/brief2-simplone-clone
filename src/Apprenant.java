public class Apprenant extends User {

    public Apprenant(String email, int id, String password, String name) {
        super(email, id, password, name, Role.APPRENANT);
    }

    public static void createApprenant() {
        String email = CMD.getInput("Enter apprenant email:");
        boolean exists = State.getUserByEmail(email) != null;
        if (exists) {
            Logger.errorln("Email already used");
            createApprenant();
            return;
        }
        String name = CMD.getInput("Enter apprenant name:");
        String password = CMD.getHiddenInput("Enter apprenant password:");
        Apprenant apprenant = State.addApprenant(email, name, password);
        Promotion.assignPromotion(apprenant, true);
        Logger.successln("Apprenant (" + apprenant.getName() + ") created successfully");
    }

    public static void listApprenants() {
        Logger.logln("Apprenant: ****************************************");
        CMD.listOptions(State.getUsersByRoleAsOptions(Role.APPRENANT));
        Logger.logln("***************************************************");
    }
}

import java.util.ArrayList;

public class Apprenant extends User {

    public Apprenant(String email, int id, String password, String name) {
        super(email, id, password, name, Role.APPRENANT);
    }

    public static void create() {
        String email = CMD.getInput("Enter apprenant email:");
        boolean exists = User.getByEmail(email) != null;
        if (exists) {
            Logger.errorln("Email already used");
            create();
            return;
        }
        String name = CMD.getInput("Enter apprenant name:");
        String password = CMD.getHiddenInput("Enter apprenant password:");
        Apprenant apprenant = add(email, name, password);
        Promotion.assignPromotion(apprenant, true);
        Logger.successln("Apprenant (" + apprenant.getName() + ") created successfully");
    }

    public static void list() {
        ArrayList<Option> apprenant = User.asOptions(Role.APPRENANT);
        int size = apprenant.size();
        if(size == 0) {
            Logger.warningln("No apprenants found");
            return;
        }
        Logger.logln("Apprenant (" + size + "): ****************************************");
        CMD.listOptions(apprenant);
        Logger.logln("***************************************************");
    }

    public static Apprenant add(String email, String name, String password) {
        Apprenant apprenant = new Apprenant(email, getNextUserId(), password, name);
        addUser(apprenant);
        return apprenant;
    }
}

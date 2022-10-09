package services;

import models.Option;
import models.Role;
import models.User;

import java.util.ArrayList;

public class FormateurService extends MemberService {


    public static ArrayList<Command> getCommands() {
        if (AuthService.getUser().getPromotion() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>() {{
            add(new Command("Add brief", BriefService::create));
            add(new Command("Publish a brief", BriefService::publish));
            add(new Command("List briefs", BriefService::list));
            add(new Command("List Promotions Apprenants", ApprenantService::list));
            add(new Command("My promotion", MemberService::showAssignedPromotion));
            add(new Command("Assign Apprenant to Promotion", () -> MemberService.assignPromotion(Role.APPRENANT)));
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
        User user = add(email, name, password);
        PromotionService.assignPromotion(user, true);
        Logger.successln("Formatteur (" + user.getName() + ") created successfully");
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


    public static User add(String email, String name, String password) {
        return MemberService.addMember(email, password, name, Role.FORMATTEUR);
    }

    private static ArrayList<Option> asOptions() {
        return MemberService.asOptions(Role.FORMATTEUR);
    }
}

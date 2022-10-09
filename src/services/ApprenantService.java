package services;

import models.Option;
import models.Role;
import models.User;

import java.util.ArrayList;

public class ApprenantService extends MemberService {


    public static ArrayList<Command> getCommands() {
        User user = AuthService.getUser();
        if (user.getPromotion() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>() {{
            add(new Command("List Briefs", BriefService::listAssigned));
            add(new Command("My promotion", MemberService::showAssignedPromotion));
        }};
    }

    public static void create() {
        String email = CMD.getInput("Enter user email:").toLowerCase();
        boolean exists = User.getByEmail(email) != null;
        if (exists) {
            Logger.errorln("Email already used");
            create();
            return;
        }
        String name = CMD.getInput("Enter user name:");
        String password = CMD.getHiddenInput("Enter user password:");
        User user = add(email, name, password);
        PromotionService.assignPromotion(user, true);
        Logger.successln("Apprenant (" + user.getName() + ") created successfully");
    }

    public static void list() {
        ArrayList<Option> members = asOptions();
        int size = members.size();
        if (size == 0) {
            Logger.warningln("No apprenants found");
            return;
        }
        Logger.logln("Apprenant (" + size + "): ****************************************");
        CMD.listOptions(members);
        Logger.logln("***************************************************");
    }

    public static User add(String email, String name, String password) {
        return MemberService.addMember(email, password, name, Role.APPRENANT);
    }

    public static ArrayList<Option> asOptions() {
        if (AuthService.getLoggedInRole() == Role.FORMATTEUR) {
            return MemberService.asOptions(Role.APPRENANT, AuthService.getUser().getPromo());
        }
        return MemberService.asOptions(Role.APPRENANT);
    }

}

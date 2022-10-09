package services;

import models.Option;
import models.Promotion;
import models.Role;
import models.User;

import java.util.ArrayList;
import java.util.Collections;

public class MemberService implements Commander, Option {


    public static User addMember(String email, String password, String name, Role role) {
        User user = new User(email, password, name, role.ordinal());
        user.save();
        return user;
    }


    public static ArrayList<Option> asOptions(Role role) {
        return asOptions(User.getAllByRole(role.ordinal()));
    }

    public static ArrayList<Option> asOptions(User[] users) {
        ArrayList<Option> options = new ArrayList<>();
        Collections.addAll(options, users);
        return options;
    }

    public static ArrayList<Option> asOptions(Role role, int promoId) {
        return asOptions(User.getAllByPromoIdAndRole(promoId, role.ordinal()));
    }


    public static void assignPromotion(Role role) {
        int count = Promotion.count();
        if (count == 0) {
            Logger.errorln("No promotions found, one needs to be created first");
            if (AuthService.getAdmin() == null) {
                return; // only admins can create promotions
            }

            boolean createPromotion = CMD.getConfirmation("Create a promotion?");
            if (!createPromotion) {
                return;
            }
            PromotionService.create();
        }
        ArrayList<Option> usersByRoleAsOptions = asOptions(role);
        int option = CMD.chooseOption(usersByRoleAsOptions, true);
        if (option == -1) {
            Logger.warningln("Cancelled promotion assignment");
            return;
        }
        User user = (User) usersByRoleAsOptions.get(option);
        PromotionService.assignPromotion(user, false);
        Logger.successln("Promotion " + user.getPromotion().getName() + " assigned to " + user.getName());
    }

    public static void showAssignedPromotion() {
        Logger.infoln("Assigned promotion: " + AuthService.getUser().getPromotion().getName());
    }


}

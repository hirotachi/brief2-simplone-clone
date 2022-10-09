package services;

import models.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PromotionService implements Option {


    public static ArrayList<Option> asOptions() {
        Promotion[] promotions = Promotion.getAll();
        return asOptions(promotions);
    }

    public static ArrayList<Option> asOptions(Promotion[] promotions) {
        ArrayList<Option> options = new ArrayList<>();
        Collections.addAll(options, promotions);
        return options;
    }


    public static Promotion add(String name, int year) {
        Promotion promotion = new Promotion(name, year);
        promotion.save();
        return promotion;
    }


    public static void assignPromotion(User user, boolean askConfirmation) {
        Promotion[] promotions = Promotion.getAll();
        if (promotions.length == 0 && askConfirmation) {
            return;
        }

        if (askConfirmation) {
            boolean confirmation = CMD.getConfirmation("Would you like to assign a promotion to this user?");
            if (!confirmation) {
                return;
            }
        }


        ArrayList<Option> options = asOptions(promotions);
        int option = CMD.chooseOption(options);
        if (option == -1) {
            return;
        }
        Promotion promotion = (Promotion) options.get(option);
        user.setPromo(promotion.getId());
        user.save();
    }

    public static void list() {
        ArrayList<Option> asOptions = asOptions();
        int size = asOptions.size();
        if (size == 0) {
            Logger.warningln("No promotions found");
            return;
        }
        Logger.logln("Promotions (" + size + "): ****************************************");
        CMD.listOptions(asOptions);
        Logger.logln("****************************************************");
    }

    public static void create() {
        String name = CMD.getInput("Enter promotion name:");
        boolean exists = Promotion.getByName(name) != null;
        if (exists) {
            Logger.errorln("Promotion name already used");
            create();
            return;
        }
        int year = CMD.getIntInput("Enter promotion year:");
        if (year == -1) {
            year = Year.now().getValue();
        }
        Promotion promotion = add(name, year);
        Logger.successln("Promotion (" + promotion.getName() + ") created successfully");
        PromotionService.assignFormatteur(promotion, true);
    }


    public static void assignFormatteur(Promotion promotion, boolean askConfirmation) {
        User[] users = User.getAllByRole(Role.FORMATTEUR.ordinal());
        if (users.length == 0) {
            Logger.errorln("No formatteurs found");
            return;
        }
        if (askConfirmation) {
            boolean confirmation = CMD.getConfirmation("Would you like to assign a formatteur to this promotion?");
            if (!confirmation) {
                return;
            }
        }

        ArrayList<Option> options = MemberService.asOptions(users);
        int option = CMD.chooseOption(options, true);
        if (option == -1) {
            return;
        }
        User user = (User) options.get(option);
        user.setPromo(promotion.getId());
        user.save();
        Logger.successln("Formatteur " + user.getName() + " assigned to " + user.getName());
    }


    public static void notifyPromotion(Brief brief, int promo_id) {
        User[] users = User.getAllByPromoIdAndRole(promo_id, Role.APPRENANT.ordinal());
        if (users.length == 0) {
            Logger.warningln("No apprenants found in this promotion");
            return;
        }
        Arrays.stream(users).parallel()
              .forEach(user -> user.notifyAboutBrief(brief));
        Logger.successln("Promotion has been notified successfully");
    }
}

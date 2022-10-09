package services;

import models.Brief;
import models.Option;
import models.Role;
import models.User;

import java.util.ArrayList;
import java.util.Collections;

public class BriefService {


    public static Brief add(String name, String description) {
        User user = AuthService.getUser();
        Brief brief = new Brief(name, description, user.getId(), user.getPromo());
        brief.save();
        return brief;
    }


    public static void create() {
        create(false);
    }

    public static void create(boolean hidePublishConfirmation) {
        String name = CMD.getInput("Name of the brief");
        String description = CMD.getInput("Description of the brief:");
        Brief brief = add(name, description);

        if (!hidePublishConfirmation) {
            boolean publish = CMD.getConfirmation("Do you want to publish the brief ?");
            brief.publish(publish);
        }
        Logger.successln("services.Brief created successfully");
    }

    public static void publish() {
        Brief[] briefs = Brief.getAllByUserId(AuthService.getUser().getId());
        if (briefs.length == 0) {
            Logger.errorln("You have no briefs to publish");
            boolean wantToCreate = CMD.getConfirmation("Do you want to create a brief ?");
            if (!wantToCreate) {
                return;
            }
            create(true);
            publish();
            return;
        }

        ArrayList<Option> options = asOptions(briefs);
        int option = CMD.chooseOption(options, true);
        if (option == -1) {
            Logger.warningln("Publishing cancelled");
            return;
        }
        Brief brief = briefs[option];
        brief.publish(true);
        Logger.successln("services.Brief" + brief.getName() + "published successfully");
    }


    public static ArrayList<Option> asOptions() {
        Brief[] briefs = AuthService.getLoggedInRole() == Role.FORMATTEUR ?
                Brief.getAllByUserId(AuthService.getUser().getId()) :
                Brief.getAll();
        return asOptions(briefs);
    }

    public static ArrayList<Option> asOptions(Brief[] briefs) {
        ArrayList<Option> options = new ArrayList<>();
        Collections.addAll(options, briefs);
        return options;
    }

    public static ArrayList<Option> asOptions(int promoId) {
        return asOptions(Brief.getAllByPromoId(promoId));
    }

    public static void list() {
        ArrayList<Option> briefs = asOptions();
        list(briefs);
    }

    private static void list(ArrayList<Option> briefs) {
        int size = briefs.size();
        if (size == 0) {
            Logger.warningln("No briefs found");
            return;
        }
        Logger.logln("Briefs (" + size + "): ****************************************");
        CMD.listOptions(briefs);
        Logger.logln("*****************************************************");
    }

    public static void listAssigned() {
        ArrayList<Option> briefs = asOptions(AuthService.getUser().getPromo());
        list(briefs);
    }


}

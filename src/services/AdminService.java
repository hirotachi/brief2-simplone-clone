package services;

import models.Admin;
import models.Role;

import java.util.ArrayList;


public class AdminService implements Commander {


    public static ArrayList<Command> getCommands() {
        return new ArrayList<>() {{
            add(new Command("Create formatteur", FormateurService::create));
            add(new Command("Create Apprenant", ApprenantService::create));
            add(new Command("Create Promotion", PromotionService::create));
            add(new Command("List Formatteurs", FormateurService::list));
            add(new Command("List Apprenant", ApprenantService::list));
            add(new Command("List Promotions", PromotionService::list));
            add(new Command("Assign Formatteur to promotion", () -> {
                MemberService.assignPromotion(Role.FORMATTEUR);
            }));
            add(new Command("Assign Apprenant to promotion", () -> {
                MemberService.assignPromotion(Role.APPRENANT);
            }));

            add(new Command("Send test email", () -> {
                EmailService.send("saidoudouane@gmail.com", "test subject", "test description");
                Logger.successln("Email sent successfully");
            }));

        }};
    }


    public static Admin add(String username, String password) {
        Admin admin = new Admin(username, password);
        admin.save();
        return admin;
    }

}

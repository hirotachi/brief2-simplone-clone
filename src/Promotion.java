import java.util.ArrayList;

public class Promotion extends Option {
    private String name;
    private final int id;

    public Promotion(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<Apprenant> getApprenants() {
        ArrayList<Apprenant> apprenants = new ArrayList<>();
        for (User user : State.getUsersByPromoId(this.getId())) {
            if (user.getRole() == Role.APPRENANT) {
                apprenants.add((Apprenant) user);
            }
        }
        return apprenants;
    }

    public ArrayList<Formateur> getFormatteurs() {
        ArrayList<Formateur> formatteurs = new ArrayList<>();
        for (User user : State.getUsersByPromoId(this.getId())) {
            if (user.getRole() == Role.FORMATTEUR) {
                formatteurs.add((Formateur) user);
            }
        }
        return formatteurs;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static void assignPromotion(User user, boolean askConfirmation) {
        if (askConfirmation) {
            boolean confirmation = CMD.getConfirmation("Would you like to assign a promotion to this user?");
            if (!confirmation) {
                return;
            }
        }


        int option = CMD.chooseOption(State.getPromotionsAsOptions());
        if (option == -1) {
            return;
        }
        Promotion promotion = State.getPromotionById(option);
        user.setPromoId(promotion.getId());
    }

}

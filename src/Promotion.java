import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;

public class Promotion extends Option {
    static HashMap<Integer, Promotion> listById;
    private static int nextId = 1;
    private final int year;
    private String name;
    private final int id;

    public Promotion(String name, int year, int id) {
        this.name = name;
        this.id = id;
        this.year = year;
    }

    private static int getNextId() {
        return nextId++;
    }

    public static ArrayList<Option> getAsOptions() {
        return new ArrayList<>(listById.values());
    }

    public static Promotion getById(int id) {
        return listById.get(id);
    }

    public static Promotion add(String name, int year) {
        Promotion promotion = new Promotion(name, year, getNextId());
        listById.put(promotion.getId(), promotion);
        return promotion;
    }

    public static Promotion getByName(String name) {
        for (Promotion promotion : listById.values()) {
            if (promotion.getName().equals(name)) {
                return promotion;
            }
        }
        return null;
    }

    public static int count() {
        return listById.size();
    }

    public static void parseAndLoad(HashMap<Integer, Promotion> promotionsById) {
        Promotion.listById = promotionsById;
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
        for (User user : User.getUsersByPromoId(this.getId())) {
            if (user.getRole() == Role.APPRENANT) {
                apprenants.add((Apprenant) user);
            }
        }
        return apprenants;
    }

    public ArrayList<Formateur> getFormatteurs() {
        ArrayList<Formateur> formatteurs = new ArrayList<>();
        for (User user : User.getUsersByPromoId(this.getId())) {
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
        if(listById.size() == 0 && askConfirmation) {
            Logger.errorln("No promotion found");
            return;
        }

        if (askConfirmation) {
            boolean confirmation = CMD.getConfirmation("Would you like to assign a promotion to this user?");
            if (!confirmation) return;
        }


        int option = CMD.chooseOption(getAsOptions());
        if (option == -1) return;
        Promotion promotion = getById(option);
        user.setPromoId(promotion.getId());
    }


    public static void list() {
        ArrayList<Option> asOptions = getAsOptions();
        int size = asOptions.size();
        if (size == 0) {
            Logger.warningln("No promotions found");
            return;
        }
        Logger.logln("Promotions (" + size + "): ****************************************");
        CMD.listOptions(asOptions);
        Logger.logln("****************************************************");
    }

    public void assignFormatteur() {
        ArrayList<Option> usersByRoleAsOptions = User.asOptions(Role.FORMATTEUR);
        if (usersByRoleAsOptions.size() == 0) {
            Logger.errorln("No formatteurs found");
            return;
        }
        boolean confirmation = CMD.getConfirmation("Would you like to assign a formatteur to this promotion?");
        if (!confirmation) {
            return;
        }

        int option = CMD.chooseOption(usersByRoleAsOptions, true);
        if (option == -1) {
            return;
        }
        Formateur user = (Formateur) usersByRoleAsOptions.get(option);
        user.setPromoId(this.getId());
        Logger.successln("Formatteur " + user.getName() + " assigned to " + this.getName());
    }

    public static void create() {
        String name = CMD.getInput("Enter promotion name:");
        boolean exists = getByName(name) != null;
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
        promotion.assignFormatteur();
    }

    public int getYear() {
        return year;
    }
}

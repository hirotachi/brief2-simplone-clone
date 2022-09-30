import java.util.ArrayList;
import java.util.HashMap;

public class Brief extends Option {

    private static int nextId = 1;
    private static HashMap<Integer, Brief> listById;
    private static HashMap<Integer, ArrayList<Brief>> listByPromoId;
    private final String name;

    private boolean published;

    private final int id;
    private final String description;
    private final int authorId;
    private final int promoId;

    public Brief(String name, int id, String description, int authorId, int promoId) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.authorId = authorId;
        this.promoId = promoId;
    }

    public static void parseAndLoad(HashMap<Integer, Brief> state) {
        Brief.listById = state;
        Brief.listByPromoId = new HashMap<>();
        for (Brief brief : state.values()) {
            addBriefToPromo(brief);
        }
    }

    private Integer getPromoId() {
        return promoId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Formateur getAuthor() {
        return (Formateur) Formateur.getById(authorId);
    }


    public static Brief add(String name, String description) {
        User user = Auth.getUser();
        Brief brief = new Brief(name, getNextId(), description, user.getId(), user.getPromoId());
        listById.put(brief.getId(), brief);
        addBriefToPromo(brief);
        return brief;
    }

    private static void addBriefToPromo(Brief brief) {
        if (!listByPromoId.containsKey(brief.getPromoId())) {
            listByPromoId.put(brief.getPromoId(), new ArrayList<>());
        }
        listByPromoId.get(brief.getPromoId()).add(brief);
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
            brief.setPublished(publish);
        }
        Logger.successln("Brief created successfully");
    }

    public static void publish() {
        ArrayList<Brief> briefs = getByAuthorId(Auth.getUser().getId());
        if (briefs.size() == 0) {
            Logger.errorln("You have no briefs to publish");
            boolean wantToCreate = CMD.getConfirmation("Do you want to create a brief ?");
            if (!wantToCreate) return;
            create(true);
            publish();
            return;
        }

        ArrayList<Option> options = new ArrayList<>(briefs);
        int option = CMD.chooseOption(options, true);
        if(option == -1) {
            Logger.warningln("Publishing cancelled");
            return;
        }
        Brief brief = briefs.get(option);
        brief.setPublished(true);
        Logger.successln("Brief" + brief.getName() + "published successfully");

    }


    public static Brief getById(int id) {
        return listById.get(id);
    }

    public static ArrayList<Brief> getByAuthorId(int id) {
        ArrayList<Brief> briefs = new ArrayList<>();
        for (Brief brief : listById.values()) {
            if (brief.getAuthor().getId() == id) {
                briefs.add(brief);
            }
        }
        return briefs;
    }


    private static int getNextId() {
        return nextId++;
    }


    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if (isPublished()) {
            Logger.success("(Published)");
        } else {
            Logger.error("(Draft)");
        }
        return " " + getName() + " - " + getDescription();
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
        if (published) this.getPromotion().notifyApprenants(this);
    }


    public static ArrayList<Option> asOptions() {
        return new ArrayList<>(listById.values());
    }

    public static ArrayList<Option> asOptions(int promoId) {
        return new ArrayList<>(getByPromoId(promoId));
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
        ArrayList<Option> briefs = asOptions(Auth.getUser().getPromoId());
        list(briefs);
    }

    private static ArrayList<Brief> getByPromoId(int promoId) {
        return listByPromoId.getOrDefault(promoId, new ArrayList<>());
    }

    public Promotion getPromotion() {
        return Promotion.getById(promoId);
    }
}

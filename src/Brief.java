import java.util.ArrayList;
import java.util.HashMap;

public class Brief extends Option {

    private static int nextId = 1;
    private static HashMap<Integer, Brief> listById;
    private final String name;

    private boolean published;

    private final int id;
    private final String description;
    private final int authorId;

    public Brief(String name, int id, String description, int authorId) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.authorId = authorId;
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
        Brief brief = new Brief(name, getNextId(), description, Auth.getUser().getId());
        listById.put(brief.getId(), brief);
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
            brief.setPublished(publish);
        }
    }

    public static void publish() {
//         list all briefs created by user or show that list is empty
//         get option from user
//        set selected brief to published
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
        if(option == 0) {
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
            Logger.error("(Draft)");
        } else {
            Logger.success("(Published)");
        }
        return " " + getName() + " - " + getDescription();
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }


    public static ArrayList<Option> asOptions() {
        return new ArrayList<>(listById.values());
    }

    public static void list() {
        ArrayList<Option> briefs = asOptions();
        int size = briefs.size();
        if (size == 0) {
            Logger.warningln("No briefs found");
            return;
        }
        Logger.logln("Briefs (" + size + "): ****************************************");
        CMD.listOptions(briefs);
        Logger.logln("*****************************************************");
    }
}

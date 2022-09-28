public class Brief {
    private final String name;
    private final String description;
    private final int authorId;

    public Brief(String name, String description, int authorId) {
        this.name = name;
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
}

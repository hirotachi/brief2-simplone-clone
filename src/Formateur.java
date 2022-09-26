public class Formateur extends User {

    public Formateur(String email, int id, String password, String name, Role role, int promoId) {
        super(email, id, password, name, role, promoId);
        this.assignCommands(
                new Command("create brief", "Add brief", () -> {
                    System.out.println("Create brief");
                }),
                new Command("update brief", "Update brief", () -> {
                    System.out.println("Update brief");
                }));
    }
}

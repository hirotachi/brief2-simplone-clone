import services.Application;
import services.CMD;

public class Main {
    public static void main(String[] args) {
        CMD.welcome();
//        State.load(); // Load the state from the permanent storage
        Application.start();
    }

}
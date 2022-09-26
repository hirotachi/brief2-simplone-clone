import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

public class CMD {

    public static void welcome() {
        System.out.println("Welcome to Simplon");
    }

    private static void listCommands(ArrayList<Command> commands) {
        System.out.println("List of commands:");
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            System.out.println(i + 1 + " - " + command.getName() + " : " + command.getDescription());
        }
        System.out.println(0 + " - " + "Help: Show list of commands.");
    }

    public static void listAndListen(ArrayList<Command> commands) {
        listCommands(commands);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a command number: ");

        int commandIndex = scanner.nextInt();
        if (commandIndex == 0) {
            listAndListen(commands);
            return;
        }
        if (commandIndex < 1 || commandIndex >= commands.size()) {
            Logger.errorln("Invalid command number, please choose from list.");
            listAndListen(commands);
            return;
        }

        Command command = commands.get(commandIndex - 1);
        command.run();
        listAndListen(commands);
    }

    public static String getInput(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static String getHiddenInput(String message) {
        System.out.println(message);
        Console console = System.console();
        if (console == null) {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }
        return new String(console.readPassword());
    }
}

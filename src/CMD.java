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
            System.out.println(i + 1 + " - " + command.getDescription());
        }
        System.out.println(0 + " - " + "Help: Show list of commands.");
        System.out.println(-1 + " - " + "Exit: Quit the program.");
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
        if (commandIndex == -1) {
            Logger.logln("Goodbye!");
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

    public static boolean getConfirmation(String message) {
        System.out.println(message + " (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input.equals("y");
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


    public static void listOptions(ArrayList<Option> options, boolean withCancel) {
        for (int i = 0; i < options.size(); i++) {
            Logger.logln(i + 1 + " - " + options.toArray()[i]);
        }
        if (withCancel) {
            Logger.logln(0 + " - " + "Cancel.");
        }
    }

    public static void listOptions(ArrayList<Option> options) {
        listOptions(options, false);
    }

    public static int chooseOption(ArrayList<Option> options, boolean withCancelOption) {
        listOptions(options, withCancelOption);

        System.out.println("Please choose an option: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option < 1 || option > options.size()) {
            Logger.errorln("Invalid option, please choose from list.");
            return chooseOption(options);
        }
        return option;
    }

    public static int chooseOption(ArrayList<Option> options) {
        return chooseOption(options, false);
    }

}

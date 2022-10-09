package services;

import models.Option;

import java.io.Console;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CMD {

    public static void welcome() {
        Logger.logln("Welcome to Simplon");
    }

    private static void listCommands(ArrayList<Command> commands, boolean hideAll) {
        if (!hideAll) {
            Logger.logln("List of commands:");
            for (int i = 0; i < commands.size(); i++) {
                Command command = commands.get(i);
                Logger.logln(i + 1 + " - " + command.getDescription());
            }
        }

        if (!hideAll) {
            Logger.logln(-1 + " - " + "Exit: Quit the program.");
            Logger.logln(-2 + " - " + "Logout: Logout from the current user.");
            Logger.logln(-3 + " - " + "Clear: Clear the screen.");
        }
        Logger.logln(0 + " - " + "Help: Show list of commands.");

    }

    public static void listAndListen(ArrayList<Command> commands, boolean hideAll) {
        listCommands(commands, hideAll);
        Scanner scanner = new Scanner(System.in);
        Logger.log("Enter a command number: ");

        int commandIndex;
        try {
            commandIndex = scanner.nextInt();
        } catch (InputMismatchException e) {
            Logger.errorln("Invalid command");
            listAndListen(commands, hideAll);
            return;
        }
        switch (commandIndex) {
            case 0 -> {
                listAndListen(commands);
                return;
            }
            case -1 -> {
                Logger.logln("Goodbye!");
                return;
            }
            case -2 -> {
                AuthService.logout();
                Application.start(); // Restart the program
                return;
            }
            case -3 -> {
                clearScreen();
                listAndListen(commands);
                return;
            }
        }
        if (commandIndex < 1 || commandIndex >= commands.size() + 1) {
            Logger.errorln("Invalid command number, please choose from list.");
            listAndListen(commands);
            return;
        }

        Command command = commands.get(commandIndex - 1);
        command.run();
        listAndListen(commands, true);
    }

    public static void listAndListen(ArrayList<Command> commands) {
        listAndListen(commands, false);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String getInput(String message) {
        Logger.logln(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    public static boolean getConfirmation(String message) {
        String input = getInput(message + " (y/n or yes/no) default is no").trim().toLowerCase();
        return input.equals("y") || input.equals("yes");

    }

    public static String getHiddenInput(String message) {
        Logger.logln(message);
        Console console = System.console();
        if (console == null) {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }
        return new String(console.readPassword()).trim();
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

        Logger.logln("Please choose an option: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option == 0 && withCancelOption) {
            return -1;
        }
        if (option < 1 || option > options.size()) {
            Logger.errorln("Invalid option, please choose from list.");
            return chooseOption(options);
        }
        return option - 1;
    }

    public static int chooseOption(ArrayList<Option> options) {
        return chooseOption(options, false);
    }

    public static int getIntInput(String message) {
        Logger.logln(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }


}

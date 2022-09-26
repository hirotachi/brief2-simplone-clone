public class Logger {

    public static void successln(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    public static void errorln(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    public static void success(String message) {
        System.out.print("\u001B[32m" + message + "\u001B[0m");
    }

    public static void error(String message) {
        System.out.print("\u001B[31m" + message + "\u001B[0m");
    }

    public static void info(String message) {
        System.out.print("\u001B[34m" + message + "\u001B[0m");
    }

    public static void infoln(String message) {
        System.out.println("\u001B[34m" + message + "\u001B[0m");
    }

    public static void warning(String message) {
        System.out.print("\u001B[33m" + message + "\u001B[0m");
    }

    public static void warningln(String message) {
        System.out.println("\u001B[33m" + message + "\u001B[0m");
    }

    public static void logln(String message) {
        System.out.println(message);
    }

    public static void log(String message) {
        System.out.print(message);
    }

}

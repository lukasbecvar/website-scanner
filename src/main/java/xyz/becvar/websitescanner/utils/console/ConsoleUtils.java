package xyz.becvar.websitescanner.utils.console;

public class ConsoleUtils {

    // clear console (User after start)
    public void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

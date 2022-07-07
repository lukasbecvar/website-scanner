package xyz.becvar.websitescanner.utils.console;

import xyz.becvar.websitescanner.Main;

public class Logger {

    //Main app prefix string
    public static String Prefix = ConsoleColors.CODES.ANSI_RED + "[" + ConsoleColors.CODES.ANSI_BLUE + Main.APP_NAME + ConsoleColors.CODES.ANSI_RED + "]" + ConsoleColors.CODES.ANSI_YELLOW + ": " + ConsoleColors.CODES.ANSI_CYAN;

    public static void log(String msg) {
        System.out.println(Prefix + msg);
    }
}

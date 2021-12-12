package xyz.becvar.websitescanner.utils.console;

import xyz.becvar.websitescanner.Main;

public class Logger {

    public static String Prefix = ConsoleColors.ANSI_RED + "[" + ConsoleColors.ANSI_BLUE + Main.APP_NAME + ConsoleColors.ANSI_RED + "]" + ConsoleColors.ANSI_YELLOW + ": " + ConsoleColors.ANSI_GREEN;

    public static void log(String msg) {
        System.out.println(Prefix + msg);
    }
}

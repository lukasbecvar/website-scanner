package xyz.becvar.websitescanner.utils.console;

public enum ConsoleColors {

    CODES;

    //Msg color codes
    public String ANSI_RESET = "\u001B[0m";
    public String ANSI_BLACK = "\u001B[30m";
    public String ANSI_RED = "\u001B[31m";
    public String ANSI_GREEN = "\u001B[32m";
    public String ANSI_YELLOW = "\u001B[33m";
    public String ANSI_BLUE = "\u001B[34m";
    public String ANSI_PURPLE = "\u001B[35m";
    public String ANSI_CYAN = "\u001B[36m";
    public String ANSI_WHITE = "\u001B[37m";

    //Msg Background codes
    public String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public String ANSI_RED_BACKGROUND = "\u001B[41m";
    public String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}
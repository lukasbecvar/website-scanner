package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.SystemUtil;
import xyz.becvar.websitescanner.utils.console.ConsoleUtils;

import java.util.Scanner;

public class Main {

    //Define basic vars
    public static final String APP_NAME = "WebsiteScanner";

    //Init main objects
    public static Validator validator = new Validator();
    public static SiteScanner siteScanner = new SiteScanner();
    public static Scanner scanner = new Scanner(System.in);
    public static ConsoleUtils consoleUtils = new ConsoleUtils();

    public static void main(String[] args) {

        //Clear console after start
        consoleUtils.clearConsole();

        siteScanner.scan("https://web.becvar.xyz/");

 /*       //Print ans fro get target url
        System.out.print("Please type target url: ");

        //Save target url to string
        String targetURL = scanner.nextLine();

        //Check if target url is not empty
        if (targetURL.isEmpty()) {
            SystemUtil.kill("error target url is empty!");
        } else {

            //Check if url is valid
            if (!validator.isURL(targetURL)) {
                SystemUtil.kill("error target url is invalid! [https://www.example.com]");
            } else {

                //Set main scan phase
                siteScanner.scan(targetURL);
            }
        }*/
    }
}

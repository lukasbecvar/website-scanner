package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.FileUtils;
import xyz.becvar.websitescanner.utils.SystemUtil;
import xyz.becvar.websitescanner.utils.console.ConsoleUtils;
import xyz.becvar.websitescanner.utils.console.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    //Define basic vars
    public static final String APP_NAME = "WebsiteScanner";

    //Init main objects
    public static Validator validator = new Validator();
    public static SiteScanner siteScanner = new SiteScanner();
    public static Scanner scanner = new Scanner(System.in);
    public static ConsoleUtils consoleUtils = new ConsoleUtils();
    public static FileUtils fileUtils = new FileUtils();
    public static Getter getter = new Getter();

    public static void main(String[] args) {

        //Create log folder
        fileUtils.createLogDir();

        //Clear console after start
        consoleUtils.clearConsole();

        //Print ans for get yes or no (site wordlist scanning)
        System.out.print("Do you want to use site list scan? [YES/NO]: ");

        //save list use ans
        String useSiteList = scanner.nextLine();

        if (useSiteList.equalsIgnoreCase("yes") || useSiteList.isEmpty()) {

            if (!fileUtils.ifFileExist("site.list")) {
                SystemUtil.kill("Error: site.list not exist please create site.list and put inside url list");
            } else {

                //Print ans for get yes or no (delete url from list after scan)
                System.out.print("Do you want to remove the url from the sheet after scanning? [YES/NO]: ");

                //save list use ans
                String deleteafterscan = scanner.nextLine();

                if (!deleteafterscan.equalsIgnoreCase("yes")) {
                    Logger.log("Autoremove: -> no, urls will not be deleted after scanning");
                }

                //Try read site list file
                try (BufferedReader br = new BufferedReader(new FileReader("site.list"))) {

                    String lineContent;

                    //Start scanning for individual sites
                    while ((lineContent = br.readLine()) != null) {

                        //Scanning

                        if (validator.isHttpOrHttpsUrl("https://" + lineContent)) {
                            lineContent = "https://" + lineContent;
                        } else {
                            lineContent = "http://" + lineContent;
                        }

                        if (getter.getIP(lineContent) != null) {
                            siteScanner.scan(lineContent);
                        }

                        //End of scanning functions

                        //Remove url from list after scan (if enabled)
                        if (deleteafterscan.equalsIgnoreCase("yes")) {
                            fileUtils.removeLineFromFile("site.list", lineContent);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {

            //Print ans fro get target url
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
            }

        }
    }
}

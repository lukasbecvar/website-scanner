package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.FileUtils;
import xyz.becvar.websitescanner.utils.ResourcesUtils;
import xyz.becvar.websitescanner.utils.SystemUtil;
import xyz.becvar.websitescanner.utils.console.ConsoleUtils;
import xyz.becvar.websitescanner.utils.console.Logger;
import java.io.*;
import java.util.Scanner;

public class Main {

    // define basic vars
    public static final String APP_NAME = "WS"; // APP prefix in log
    public static int MAX_FOUND = 3000; // maximal found for fake code protection
    public static int MAX_TIMEOUT = 2; // maximal site timeout in seconds

    // init main objects
    public static Validator validator = new Validator();
    public static SiteScanner siteScanner = new SiteScanner();
    public static Scanner scanner = new Scanner(System.in);
    public static ConsoleUtils consoleUtils = new ConsoleUtils();
    public static FileUtils fileUtils = new FileUtils();
    public static Getter getter = new Getter();
    public static String useSiteList;

    public static void main(String[] args) {

        // create log folder
        fileUtils.createLogDir();

        // clear console after start
        consoleUtils.clearConsole();

        // create directory list if not exist
        if (!fileUtils.ifFileExist("directory.list")) {
            ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("directory.list"), "directory.list");
        }

        // create subdomain list if not exist
        if (!fileUtils.ifFileExist("subdomain.list")) {
            ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("subdomain.list"), "subdomain.list");
        }

        // print ans for get yes or no (site wordlist scanning)
        Logger.logPrompt("Do you want to use site list scan? [YES/NO]: ");

        // save list use ans
        useSiteList = scanner.nextLine();

        // check if site list usage enabled
        if (useSiteList.equalsIgnoreCase("yes") || useSiteList.isEmpty()) {

            // create site list if not exist
            if (!fileUtils.ifFileExist("site.list")) {
                ResourcesUtils.copyResource(Main.class.getClassLoader().getResourceAsStream("site.list"), "site.list");
            }

            // check if site.list exist
            if (!fileUtils.ifFileExist("site.list")) {
                SystemUtil.kill("Error: site.list not exist please create site.list and put inside url list");
            } else {

                // print ans for get yes or no (delete url from list after scan)
                Logger.logPrompt("Do you want to remove the url from the list after scanning? [YES/NO]: ");

                // save list use ans
                String deleteafterscan = scanner.nextLine();

                // check if delete scanned sites enabled
                if (!deleteafterscan.equalsIgnoreCase("yes") && !deleteafterscan.isEmpty()) {
                    Logger.log("Autoremove: -> no, urls will not be deleted after scanning");
                }

                // try read site list file
                try (BufferedReader br = new BufferedReader(new FileReader("site.list"))) {

                    // define string for site names
                    String lineContent;

                    // start scanning for individual sites
                    while ((lineContent = br.readLine()) != null) {

                        // scanning
                        String lineContentToScan;

                        // remove protocol from sites
                        if (lineContent.startsWith("http")) {
                            lineContent = lineContent.replace("https://", "");
                            lineContent = lineContent.replace("http://", "");
                        }

                        // check if site running on https or http
                        if (validator.isHttpOrHttpsUrl("https://" + lineContent)) {
                            lineContentToScan = "https://" + lineContent;
                        } else {
                            lineContentToScan = "http://" + lineContent;
                        }

                        // check if site is not null
                        if (getter.getIP(lineContentToScan) != null) {
                            siteScanner.scan(lineContentToScan);
                        }

                        // remove url from list after scan (if enabled)
                        if (deleteafterscan.equalsIgnoreCase("yes") || deleteafterscan.isEmpty()) {
                            fileUtils.removeLineFromFile("site.list", lineContent);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {

            // print ans fro get target url
            Logger.logPrompt("Please type target url: ");

            // save target url to string
            String targetURL = scanner.nextLine();

            // check if target url is not empty
            if (targetURL.isEmpty()) {
                SystemUtil.kill("error target url is empty!");
            } else {

                // check if url is valid
                if (!validator.isURL(targetURL)) {
                    SystemUtil.kill("error target url is invalid! [https://example.com]");
                } else {

                    // set main scan phase
                    siteScanner.scan(targetURL);
                }
            }
        }
    }
}

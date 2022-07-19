package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.FileUtils;
import xyz.becvar.websitescanner.utils.SystemUtil;
import xyz.becvar.websitescanner.utils.TimeUtils;
import xyz.becvar.websitescanner.utils.console.ConsoleColors;
import xyz.becvar.websitescanner.utils.console.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SiteScanner {

    //Init main used objects
    public Getter getter = new Getter();
    public Validator validator = new Validator();
    public FileUtils fileUtils = new FileUtils();

    //Found res
    public int foundDirs = 0;
    public int foundSubs = 0;

    //Main init functions
    public void scan(String url) {
        //Delete old log if exist
        fileUtils.deleteFile("scanned_logs/" + validator.urlStrip(url) + ".log");

        //Create new log file and write title
        fileUtils.saveMessageLog("Information log: " + url + " : " + TimeUtils.getTime() + "\n", "scanned_logs/" + validator.urlStrip(url) + ".log");

        //Print real ip and save to log file
        realIPScan(url);

        //Save to log file
        fileUtils.saveMessageLog("\n\nFound dirs & subdomains " + url, "scanned_logs/" + validator.urlStrip(url) + ".log");

        //Subdomain scan
        subdomainScan(url);

        //File system scan
        fileSystemScan(url);
    }


    //Get real site ip by url
    public void realIPScan(String url) {
        //Get real site ip
        String realIP = getter.getIP(url);

        //Print real ip to console
        Logger.log("Real ip of " + url + " is: " + realIP);

        //Save real ip to log file
        fileUtils.saveMessageLog("REAL IP: " + realIP, "scanned_logs/" + validator.urlStrip(url) + ".log");
    }

    //Scan page subdomain
    public void subdomainScan(String url) {

        int file = 1;

        //init basic protocol
        String protocol = "http://";

        //Get url and remmove /
        url = validator.removeLastSlash(url);

        //Check if subdomain.list file exist
        if (fileUtils.ifFileExist("subdomain.list")) {
            try (BufferedReader br = new BufferedReader(new FileReader("subdomain.list"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {

                        //Remove protocols from urls
                        if (url.startsWith("https://")) {
                            url = url.replace("https://", "");
                        } else {
                            url = url.replace("http://", "");
                        }

                        //Set valid
                        if (validator.isHttpOrHttpsUrl("https://" + url)) {
                            protocol = "https://";
                        }

                        //Disable redirect scann
                        HttpURLConnection.setFollowRedirects(false);

                        //Define http connection
                        HttpURLConnection con = (HttpURLConnection) new URL(protocol + line + "." + url).openConnection();

                        //Set User-agent
                        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
                        con.setRequestProperty("User-Agent", "https://becvold.xyz site scanner bot");

                        //Set request method
                        con.setRequestMethod("HEAD");

                        //Set maximal connection timetou 5000 = 5second
                        con.setConnectTimeout(5000);

                        //Connect to connection define
                        con.connect();

                        //Print urů and code to console
                        if (con.getResponseCode() == 200 || con.getResponseCode() == 202 || con.getResponseCode() == 201) {
                            Logger.log(ConsoleColors.CODES.ANSI_GREEN + file + ":" + protocol + line + "." + url + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 303 || con.getResponseCode() == 304 || con.getResponseCode() == 305 || con.getResponseCode() == 308 || con.getResponseCode() == 307) {
                            Logger.log(ConsoleColors.CODES.ANSI_YELLOW + file + ":" + protocol + line + "." + url + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 400 || con.getResponseCode() == 401 || con.getResponseCode() == 402 || con.getResponseCode() == 403 || con.getResponseCode() == 404 || con.getResponseCode() == 406 || con.getResponseCode() == 409 || con.getResponseCode() == 411 || con.getResponseCode() == 414 || con.getResponseCode() == 423|| con.getResponseCode() == 426 || con.getResponseCode() == 429 || con.getResponseCode() == 451) {
                            Logger.log(ConsoleColors.CODES.ANSI_RED + file + ":" + protocol + line + "." + url + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 500 || con.getResponseCode() == 501 || con.getResponseCode() == 502 || con.getResponseCode() == 503 || con.getResponseCode() == 504 || con.getResponseCode() == 505 || con.getResponseCode() == 506 || con.getResponseCode() == 507 || con.getResponseCode() == 508 || con.getResponseCode() == 509 || con.getResponseCode() == 510|| con.getResponseCode() == 511) {
                            Logger.log(ConsoleColors.CODES.ANSI_PURPLE + file + ":" + protocol + line + "." + url + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else {
                            Logger.log(file + ":" + protocol + line + "." + url + " -> " + new String(String.valueOf(con.getResponseCode())));
                        }
                        //End of path print

                        //file count +1
                        file++;

                        //Save to log file if response code not 404, 403, 400
                        if (con.getResponseCode() != 404 && con.getResponseCode() != 400 && con.getResponseCode() != 403 && con.getResponseCode() != 301 && con.getResponseCode() != 302 && con.getResponseCode() != 308 && con.getResponseCode() != 301 && con.getResponseCode() != 429) {

                            //Found subdomain + 1
                            foundSubs++;

                            //Save found to log file
                            fileUtils.saveMessageLog(protocol + line + "." + url + " - " + new String(String.valueOf(con.getResponseCode())), "scanned_logs/" + validator.urlStrip(url) + ".log");
                        }
                    } catch (Exception e) {

                        //Print not found to console
                        Logger.log(ConsoleColors.CODES.ANSI_RED + e.getMessage() + " -> subdomain not found");
                    }
                }
            } catch (IOException e) {
                Logger.log(e.getMessage());
            }
        } else {

            //Print error if subdomain wordlist not found
            SystemUtil.kill("error subdomain.list not found, please check your file or try reinstall this app");
        }
    }

    //Scan page file system
    public void fileSystemScan(String url) {

        int file = 1;

        //Get url and remmove /
        url = validator.removeLastSlash(url);

        //Check if directory.list file exist
        if (fileUtils.ifFileExist("directory.list")) {
            try (BufferedReader br = new BufferedReader(new FileReader("directory.list"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {

                        //Disable redirect scan
                        HttpURLConnection.setFollowRedirects(false);

                        //Define http connection
                        HttpURLConnection con = (HttpURLConnection) new URL(url + "/" + line).openConnection();

                        //Set User-agent
                        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
                        con.setRequestProperty("User-Agent", "https://becvold.xyz site scanner bot");

                        //Set connection method
                        con.setRequestMethod("HEAD");

                        //Set maximal connection timetou 5000 = 5second
                        con.setConnectTimeout(5000);

                        //Connect to define connection
                        con.connect();

                        //Print path and code to console
                        if (con.getResponseCode() == 200 || con.getResponseCode() == 202 || con.getResponseCode() == 201) {
                            Logger.log(ConsoleColors.CODES.ANSI_GREEN + file + ":" + url + "/" + line + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 303 || con.getResponseCode() == 304 || con.getResponseCode() == 305 || con.getResponseCode() == 308 || con.getResponseCode() == 307) {
                            Logger.log(ConsoleColors.CODES.ANSI_YELLOW + file + ":" + url + "/" + line + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 400 || con.getResponseCode() == 401 || con.getResponseCode() == 402 || con.getResponseCode() == 403 || con.getResponseCode() == 404 || con.getResponseCode() == 406 || con.getResponseCode() == 409 || con.getResponseCode() == 411 || con.getResponseCode() == 414 || con.getResponseCode() == 423|| con.getResponseCode() == 426 || con.getResponseCode() == 429 || con.getResponseCode() == 451) {
                            Logger.log(ConsoleColors.CODES.ANSI_RED + file + ":" + url + "/" + line + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else if (con.getResponseCode() == 500 || con.getResponseCode() == 501 || con.getResponseCode() == 502 || con.getResponseCode() == 503 || con.getResponseCode() == 504 || con.getResponseCode() == 505 || con.getResponseCode() == 506 || con.getResponseCode() == 507 || con.getResponseCode() == 508 || con.getResponseCode() == 509 || con.getResponseCode() == 510|| con.getResponseCode() == 511) {
                            Logger.log(ConsoleColors.CODES.ANSI_PURPLE + file + ":" + url + "/" + line + " -> " + new String(String.valueOf(con.getResponseCode())));
                        } else {
                            Logger.log(file + ":" + url + "/" + line + " code: " + new String(String.valueOf(con.getResponseCode())));
                        }
                        //End of path print

                        //File count + 1
                        file++;

                        //Save to log file if response code not 404, 403, 400
                        if (con.getResponseCode() != 404 && con.getResponseCode() != 400 && con.getResponseCode() != 403 && con.getResponseCode() != 301 && con.getResponseCode() != 302 && con.getResponseCode() != 308 && con.getResponseCode() != 301 && con.getResponseCode() != 429) {

                            //Found dirs + 1
                            foundDirs++;

                            //Save found to log
                            fileUtils.saveMessageLog(url + "/" + line + " - " + new String(String.valueOf(con.getResponseCode())), "scanned_logs/" + validator.urlStrip(url) + ".log");
                        }
                    } catch (Exception e) {

                        //Print error for try catch
                        Logger.log(ConsoleColors.CODES.ANSI_RED + "connction error: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                Logger.log(e.getMessage());
            }

            //Print final found count
            Logger.log("Scanner: exited with " + foundDirs + " found files & " + foundSubs + " subdomains");

            //Check if found is not fakes
            if (foundDirs > 3000 || foundSubs > 3000 || (foundDirs == 0 & foundSubs == 0)) {
                Logger.log("Scanner: the scanned data will not be saved.");
                fileUtils.deleteFile("scanned_logs/" + validator.urlStrip(url) + ".log");
            } else {
                Logger.log("Scanner: the scanned data saved to scanned_logs/" + "scanned_logs/" + validator.urlStrip(url) + ".log");
            }
        } else {
            SystemUtil.kill("error directory.list not found, please check your file or try reinstall this app");
        }
    }
}

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SiteScanner {

    // init main used objects
    public Getter getter = new Getter();
    public Validator validator = new Validator();
    public FileUtils fileUtils = new FileUtils();

    // found res
    public int foundDirs = 0;
    public int foundSubs = 0;

    // main init functions
    public void scan(String url) {
        // felete old log if exist
        fileUtils.deleteFile("scanned_logs/" + validator.urlStrip(url) + ".log");

        // create new log file and write title
        fileUtils.saveMessageLog("Information log: " + url + " : " + TimeUtils.getTime() + "\n", "scanned_logs/" + validator.urlStrip(url) + ".log");

        // print real ip and save to log file
        realIPScan(url);

        // get web server name
        getServer(url);

        // save to log file
        fileUtils.saveMessageLog("\n\nFound dirs & subdomains " + url, "scanned_logs/" + validator.urlStrip(url) + ".log");

        // file system scan
        fileSystemScan(url);

        // subdomain scan
        if (foundDirs < Main.MAX_FOUND) {
            subdomainScan(url);
        }

        // final save scanned data
        finalSave(url);
    }

    // get real site ip by url
    public void realIPScan(String url) {
        // get real site ip
        String realIP = getter.getIP(url);

        // print real ip to console
        Logger.log("Real ip of " + url + " is: " + realIP);

        // save real ip to log file
        fileUtils.saveMessageLog("REAL IP: " + realIP, "scanned_logs/" + validator.urlStrip(url) + ".log");
    }

    public void getServer(String url) {

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        URLConnection conn = null;
        try {
            conn = obj.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get header by 'key'
        String server = conn.getHeaderField("Server");

        // print real ip to console
        Logger.log("Server running on " + url + "is: " + server);

        // save real ip to log file
        fileUtils.saveMessageLog("SERVER: " + server, "scanned_logs/" + validator.urlStrip(url) + ".log");
    }

    // scan page file system
    public void fileSystemScan(String url) {

        int file = 1;

        // gGet url and remmove /
        url = validator.removeLastSlash(url);

        // check if directory.list file exist
        if (fileUtils.ifFileExist("directory.list")) {
            try (BufferedReader br = new BufferedReader(new FileReader("directory.list"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {

                        // disable redirect scan
                        HttpURLConnection.setFollowRedirects(false);

                        // define http connection
                        HttpURLConnection con = (HttpURLConnection) new URL(url + "/" + line).openConnection();

                        // set User-agent
                        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
                        con.setRequestProperty("User-Agent", "https://becvar.xyz site scanner bot");

                        // set connection method
                        con.setRequestMethod("GET");

                        // set maximal connection timetou 5000 = 5 second
                        con.setConnectTimeout(Main.MAX_TIMEOUT * 1000);
                        con.setReadTimeout(Main.MAX_TIMEOUT * 1000);

                        // connect to define connection
                        con.connect();

                        // print path and code to console
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
                        // end of path print

                        // file count + 1
                        file++;

                        // save to log file if response code not 404, 403, 400
                        if (con.getResponseCode() != 404 && con.getResponseCode() != 400 && con.getResponseCode() != 403 && con.getResponseCode() != 301 && con.getResponseCode() != 302 && con.getResponseCode() != 308 && con.getResponseCode() != 301 && con.getResponseCode() != 429) {

                            // found dirs + 1
                            foundDirs++;

                            // save found to log
                            fileUtils.saveMessageLog(url + "/" + line + " - " + new String(String.valueOf(con.getResponseCode())), "scanned_logs/" + validator.urlStrip(url) + ".log");
                        }
                    } catch (Exception e) {

                        // print error for try catch
                        Logger.log(ConsoleColors.CODES.ANSI_RED + file + ":" + "connection error: " + e.getMessage());

                        // file count + 1
                        file++;
                    }
                }
            } catch (IOException e) {
                Logger.log(e.getMessage());
            }

        } else {
            SystemUtil.kill("error directory.list not found, please check your file or try reinstall this app");
        }
    }

    // scan page subdomain
    public void subdomainScan(String url) {

        int file = 1;

        // init basic protocol
        String protocol = "http://";

        // get url and remmove /
        url = validator.removeLastSlash(url);

        // check if subdomain.list file exist
        if (fileUtils.ifFileExist("subdomain.list")) {
            try (BufferedReader br = new BufferedReader(new FileReader("subdomain.list"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {

                        // remove protocols from urls
                        if (url.startsWith("https://")) {
                            url = url.replace("https://", "");
                        } else {
                            url = url.replace("http://", "");
                        }

                        // set valid
                        if (validator.isHttpOrHttpsUrl("https://" + url)) {
                            protocol = "https://";
                        }

                        // disable redirect scann
                        HttpURLConnection.setFollowRedirects(false);

                        // define http connection
                        HttpURLConnection con = (HttpURLConnection) new URL(protocol + line + "." + url).openConnection();

                        // set User-agent
                        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
                        con.setRequestProperty("User-Agent", "https://becvar.xyz site scanner bot");

                        // set request method
                        con.setRequestMethod("GET");

                        // set maximal connection timetou 2000 = 2 seconds
                        con.setConnectTimeout(Main.MAX_TIMEOUT * 1000);
                        con.setReadTimeout(Main.MAX_TIMEOUT * 1000);

                        // connect to connection define
                        con.connect();

                        // print urů and code to console
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
                        // end of path print

                        // file count +1
                        file++;

                        // save to log file if response code not 404, 403, 400
                        if (con.getResponseCode() != 404 && con.getResponseCode() != 400 && con.getResponseCode() != 403 && con.getResponseCode() != 301 && con.getResponseCode() != 302 && con.getResponseCode() != 308 && con.getResponseCode() != 301 && con.getResponseCode() != 429) {

                            // found subdomain + 1
                            foundSubs++;

                            // save found to log file
                            fileUtils.saveMessageLog(protocol + line + "." + url + " - " + new String(String.valueOf(con.getResponseCode())), "scanned_logs/" + validator.urlStrip(url) + ".log");
                        }
                    } catch (Exception e) {

                        // print not found to console
                        Logger.log(ConsoleColors.CODES.ANSI_RED + file + ":" + e.getMessage() + " -> subdomain not found");

                        // file count + 1
                        file++;
                    }
                }
            } catch (IOException e) {
                Logger.log(e.getMessage());
            }

            // print final found count
            Logger.log("Scanner: exited with " + foundDirs + " found files & " + foundSubs + " subdomains");

        } else {

            // print error if subdomain wordlist not found
            SystemUtil.kill("error subdomain.list not found, please check your file or try reinstall this app");
        }
    }

    // final save scanned
    public void finalSave(String url) {

        // check if found is not fakes
        if (Main.useSiteList.equalsIgnoreCase("yes") || Main.useSiteList.isEmpty()) {
            if (foundDirs > Main.MAX_FOUND || foundSubs > Main.MAX_FOUND || (foundDirs == 0 & foundSubs == 0)) {
                Logger.log("Scanner: the scanned data will not be saved.");
                fileUtils.deleteFile("scanned_logs/" + validator.urlStrip(url) + ".log");
            } else {
                Logger.log("Scanner: the scanned data saved to scanned_logs/" + "scanned_logs/" + validator.urlStrip(url) + ".log");
            }
        } else {
            Logger.log("Scanner: the scanned data saved to scanned_logs/" + "scanned_logs/" + validator.urlStrip(url) + ".log");
        }
    }
}

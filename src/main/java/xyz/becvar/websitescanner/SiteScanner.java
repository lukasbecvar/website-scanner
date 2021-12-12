package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.FileUtils;
import xyz.becvar.websitescanner.utils.SystemUtil;
import xyz.becvar.websitescanner.utils.TimeUtils;
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

    public void scan(String url) {
        //Delete old log if exist
        fileUtils.deleteFile(validator.urlStrip(url) + ".log");

        //Create new log file and write title
        fileUtils.saveMessageLog("Information log: " + url + " : " + TimeUtils.getTime() + "\n", validator.urlStrip(url) + ".log");

        //Print real ip and save to log file
        realIPScan(url);

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
        fileUtils.saveMessageLog("REAL IP: " + realIP, validator.urlStrip(url) + ".log");
    }


    //Scan page file system
    public void fileSystemScan(String url) {

        int file = 1;
        int foundFiles = 1;

        url = validator.removeLastSlash(url);

        //Save to log file
        fileUtils.saveMessageLog("\n\n\nFiles and directoryes found on " + url + "\n", validator.urlStrip(url) + ".log");

        if (fileUtils.ifFileExist("word.list")) {
            try (BufferedReader br = new BufferedReader(new FileReader("word.list"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection con = (HttpURLConnection) new URL(url + "/" + line).openConnection();
                        con.setRequestMethod("HEAD");
                        Logger.log(file + ":" + url + "/" + line + " code: " + new String(String.valueOf(con.getResponseCode())));
                        file++;
                        //Save to log file if response code not 404
                        if (con.getResponseCode() != 404 && con.getResponseCode() != 400) {
                            foundFiles++;
                            fileUtils.saveMessageLog(url + "/" + line + " - " + new String(String.valueOf(con.getResponseCode())), validator.urlStrip(url) + ".log");
                        }
                    }
                    catch (Exception e) {
                        SystemUtil.kill(e.getMessage());
                    }
                }
            } catch (IOException e) {
                SystemUtil.kill(e.getMessage());
            }
            Logger.log("Scanner exited with " + foundFiles + " found files.");
        } else {
            SystemUtil.kill("error word.list not found, please check your file or try reinstall this app");
        }
    }
}

package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.FileUtils;
import xyz.becvar.websitescanner.utils.TimeUtils;
import xyz.becvar.websitescanner.utils.console.Logger;

public class SiteScanner {

    //Init main used objects
    public static Getter getter = new Getter();
    public static Validator validator = new Validator();
    public static FileUtils fileUtils = new FileUtils();

    public void scan(String url) {
        //Delete old log if exist
        fileUtils.deleteFile(validator.urlStrip(url) + ".log");

        //Create new log file and write title
        fileUtils.saveMessageLog("Information log: " + url + " : " + TimeUtils.getTime() + "\n", validator.urlStrip(url) + ".log");

        //Print real ip and save to log file
        realIPScan(url);
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
}

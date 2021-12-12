package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.console.Logger;

public class SiteScanner {

    //Init main used objects
    public static Getter getter = new Getter();

    public void scan(String url) {

        //Init basic informations
        String realIP = getter.getIP(url);

        //Print basic information
        Logger.log("Real ip of " + url + " is: " + realIP);
    }
}

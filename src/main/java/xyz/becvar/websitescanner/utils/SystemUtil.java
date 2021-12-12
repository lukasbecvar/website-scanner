package xyz.becvar.websitescanner.utils;

import xyz.becvar.websitescanner.utils.console.Logger;

public class SystemUtil {

    //kill app function with print error msg
    public static void kill(String errorMSG) {
        Logger.log(errorMSG);
        System.exit(0);
    }
}

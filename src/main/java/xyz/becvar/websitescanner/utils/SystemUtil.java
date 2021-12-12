package xyz.becvar.websitescanner.utils;

import xyz.becvar.websitescanner.utils.console.Logger;

public class SystemUtil {

    public static void kill(String errorMSG) {
        Logger.log(errorMSG);
        System.exit(0);
    }
}

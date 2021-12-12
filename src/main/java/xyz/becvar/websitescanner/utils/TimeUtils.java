package xyz.becvar.websitescanner.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * The time utils
 * Get actual time
 */

public class TimeUtils {

    //Get actual time in string
    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}

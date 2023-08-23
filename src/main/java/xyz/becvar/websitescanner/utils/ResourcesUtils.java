package xyz.becvar.websitescanner.utils;

import xyz.becvar.websitescanner.utils.console.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourcesUtils {

    // function for copy resource to working directory
    public static void copyResource(InputStream source, String destination) {
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
            Logger.log("file: " + destination + " created!");
        } catch (IOException e) {
            SystemUtil.kill("error create file: " + destination);
        }
    }
}

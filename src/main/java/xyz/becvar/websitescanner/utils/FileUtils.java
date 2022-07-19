package xyz.becvar.websitescanner.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    //Create directory for log files
    public void createLogDir() {
        try {
            Files.createDirectories(Paths.get("scanned_logs/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Write to file
    public void saveMessageLog(String line, String file) {
        try {
            if (Files.notExists(Paths.get(file))) {
                File f = new File(String.valueOf(Paths.get(file)));
                f.createNewFile();
            }
            try(PrintWriter output = new PrintWriter(new FileWriter(file,true))) {
                output.printf("%s\r\n",line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Delete file
    public void deleteFile(String name) {
        File fileOBJ = new File(name);
        if (fileOBJ.exists()) {
            fileOBJ.delete();
        }
    }

    //Check if file exist
    public boolean ifFileExist(String name) {
        File fileOBJ = new File(name);
        if (fileOBJ.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //Remove line from file
    public void removeLineFromFile(String file, String lineToRemove) {
        try {
            File inFile = new File(file);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (!line.trim().equals(lineToRemove)) {

                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile)) {
                System.out.println("Could not rename file");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

package de.kosmos_lab.utils;

import java.io.*;
import java.nio.file.Files;

public class KosmosFileUtils {
    public static String readFile(File f) {
        StringBuilder contents = new StringBuilder();
        try {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(f), "UTF8"));
            try {
                String line = null; // not declared within while loop
                /*
                 * readLine is a bit quirky : it returns the content of a line
                 * MINUS the newline. it returns null only for the END of the
                 * stream. it returns an empty String if two newlines appear in
                 * a row.
                 */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }
    
    public static void writeToFile(File f, byte[] bytes) {
        
        try {
            Files.write(f.toPath(), bytes);
            
            
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
    public static void writeToFile(File f, String text) {
        java.io.FileWriter myWriter = null;
        try {
            myWriter = new java.io.FileWriter(f);
            myWriter.write(text);
            
            
        } catch (IOException e) {
            
            e.printStackTrace();
        } finally {
            if (myWriter != null) {
                try {
                    myWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

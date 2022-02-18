package de.kosmos_lab.utils;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;


public class FFMPEGWrapper {
    public static final String timePattern = "HH:mm:ss";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("FFMPEGWrapper");
    
    
    public static void cutVideo(File finalName, File tmpName, long skip, long duration) {
        
        String starttime = "";
        String endtime = "";
        
        
        if (skip > 0 || duration > 0) {
            SimpleDateFormat timeOnly = new SimpleDateFormat(timePattern);
            if (skip > 0) {
                starttime = timeOnly.format(new Date(skip));
            }
            if (duration > 0) {
                endtime = timeOnly.format(new Date(skip + duration));
            }
        }
        try {
            
            String cmd = String.format("ffmpeg -ss %s -to %s -i %s -c copy %s", starttime, endtime, tmpName.getCanonicalPath(), finalName.getCanonicalPath());
            logger.info("executing: {}", cmd);
            Process pr = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String line;
            
            while ((line = in.readLine()) != null) {
                
                logger.warn("stderr:{}", line);
            }
            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            
            while ((line = in.readLine()) != null) {
                
                logger.info("stdout:{}", line);
            }
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public static void mergeVideos(Collection<String> files, File finalName, long skip, long duration) throws IOException {
        
        StringBuilder mergeFileContent = new StringBuilder();
        for (String f : files) {
            mergeFileContent.append(String.format("file '%s'\n", f));
        }
        
        String mergeFileName = FileUtils.replaceEnding(finalName.getCanonicalPath(), "txt");
        FileUtils.writeToFile(mergeFileName, mergeFileContent.toString());
        File tmpFile = finalName;
        if (skip > 0 || duration > 0) {
            tmpFile = new File(finalName.getParentFile().getCanonicalPath() + "/tmp_" + finalName.getName());
        }
        
        try {
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            String cmd = String.format("ffmpeg -f concat -safe 0 -i %s -c copy %s", mergeFileName, tmpFile.getCanonicalPath());
            logger.info("executing: {}", cmd);
            Process pr = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String line;
            
            while ((line = in.readLine()) != null) {
                
                logger.warn("stderr:{}", line);
            }
            in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            
            while ((line = in.readLine()) != null) {
                
                logger.info("stdout:{}", line);
            }
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (skip > 0 || duration > 0) {
            cutVideo(finalName, tmpFile, skip, duration);
            try {
                tmpFile.delete();
            } catch (SecurityException ex) {
                logger.warn("could not delete temp file:{}", ex.getMessage());
            }
        }
        try {
            new File(mergeFileName).delete();
        } catch (SecurityException ex) {
            logger.warn("could not delete temp file:{}", ex.getMessage());
        }
    }
    
    
}

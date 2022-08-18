package de.kosmos_lab.utils;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.nio.charset.StandardCharsets;


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

    public static class FFMPPEGRecording {
        private final File file;
        private final String input;
        private Process process = null;

        public FFMPPEGRecording(File file, String input) {
            this.file = file;
            this.input = input;
        }

        public void start() throws IOException {
            String cmd = String.format("ffmpeg -i %s -acodec copy -vcodec copy %s", input, file.getCanonicalPath());
            logger.info("executing: {}", cmd);
            this.process = Runtime.getRuntime().exec(cmd);
            (new Thread() {
                public void run() {
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
                        String line;

                        while ((line = in.readLine()) != null) {

                            logger.warn("FFMPEG stderr:{}", line);
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

                        while ((line = in.readLine()) != null) {

                            logger.warn("FFMPEG stdout:{}", line);
                        }
                        try {
                            process.waitFor();
                        } catch (InterruptedException e) {
                            logger.error("could not wait for ffmpeg", e);
                        }


                        in.close();

                    } catch (IOException e) {
                        logger.error("could not wait execute ffmpeg", e);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        logger.error("could not sleep?", e);
                    }
                }
            }).start();
        }

        public void stop() {
            if (this.process != null) {
                try {
                    if (this.process.isAlive()) {

                        long pid = process.pid();
                        Runtime.getRuntime().exec(String.format("kill -s QUIT %d", pid));
                        logger.info("sending quit signal to proccess {}", pid);
                        long untilMax = System.currentTimeMillis() + 10000;
                        while (process.isAlive()) {
                            if (System.currentTimeMillis() > untilMax) {
                                break;
                            }
                        }
                    }

                } catch (Exception e) {

                } finally {
                    try {
                        if (this.process.isAlive()) {
                            this.process.destroy();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

    }


}

package de.kosmos_lab.utils;

import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Helper Class that is used to handle File based tasks
 */
public class FileUtils {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("FileUtils");
    
    
    /**
     * deletes a directory, recursive!
     *
     * @param directory the directory to be deleted
     * @return
     */
    public static boolean deleteDirectory(@Nonnull File directory) throws IllegalArgumentException {
        if (directory == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directory.delete();
    }
    
    @Nonnull
    private static Charset getCharset(@Nonnull String charset) {
        if (charset == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "charset"));
        }
        return Charset.forName(charset);
    }
    
    @Nonnull
    private static File getFile(@Nonnull String filename) {
        if (filename == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "filename"));
        }
        return new File(filename);
    }
    
    /**
     * create all parent directories for the current file
     * <p>
     * {@code /test/foo/bar.txt -> /test/ and /test/foo will be created - if they are needed}
     *
     * @param file the file for which to create the parent structure
     * @return
     */
    public static boolean makeDirsForFile(@Nonnull File file) {
        if (file == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        if ( file.getParentFile() != null ) {
            if (!file.getParentFile().exists()) {
                return file.getParentFile().mkdirs();
            }
        }
        return true;
        
        
    }
    
    /**
     * reads a binary file
     *
     * @param file the file to read
     * @return
     */
    @Nonnull
    public static byte[] readBinary(@Nonnull File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("could not find file %s", file.getPath()));
        }
        byte[] result = new byte[(int) file.length()];
        
        BufferedInputStream input = null;
        
        try {
            int totalBytesRead = 0;
            input = new BufferedInputStream(new FileInputStream(file));
            
            while (totalBytesRead < result.length) {
                int bytesRemaining = result.length - totalBytesRead;
                int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                if (bytesRead > 0) {
                    totalBytesRead += bytesRead;
                }
            }
        } finally {
            if (input != null) {
                input.close();
            }
        }
        
        
        return result;
    }
    
    /**
     * read a binary file
     *
     * @param filename the filename
     * @return the binary content of the file
     * @throws FileNotFoundException if the file could not be found
     */
    public static byte[] readBinary(@Nonnull String filename) throws IOException {
        return readBinary(getFile(filename));
    }
    
    /**
     * reads a text based file
     *
     * @param file    the file to read
     * @param charset the charset to use
     * @return a String containing the contents of the File
     */
    public static String readFile(@Nonnull File file, @Nonnull Charset charset) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("could not find file %s", file.getPath()));
        }
        StringBuilder contents = new StringBuilder();
        
        
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        
        try {
            String line = null;
            line = input.readLine();
            
            while (line != null) {
                contents.append(line);
                line = input.readLine();
                if (line != null) {
                    contents.append(System.getProperty("line.separator"));
                }
            }
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        return contents.toString();
    }
    
    /**
     * @param filename
     * @return
     * @throws FileNotFoundException
     */
    public static String readFile(@Nonnull String filename) throws IOException {
        
        return readFile(getFile(filename));
    }
    
    public static String readFile(@Nonnull File file) throws IOException {
        
        return readFile(file, StandardCharsets.UTF_8);
    }
    
    public static String readFile(@Nonnull String filename, @Nonnull String charset) throws IOException {
        
        return readFile(getFile(filename), getCharset(charset));
    }
    
    public static String readFile(@Nonnull String filename, @Nonnull Charset charset) throws IOException {
        if (filename == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "filename"));
        }
        return readFile(getFile(filename), charset);
    }
    public static String replaceEnding(File file, String s) throws IOException {
        return replaceEnding(file.getCanonicalPath(),s);
        
    }
    public static String replaceEnding(String path, String s) {
        int index = path.lastIndexOf('.');
        if ( index > -1) {
            return String.format("%s.%s",path.substring(0,index),s);
        }
        return path;
        
    }
    
    /**
     * Write the given bytes to the file, and use the charset
     *
     * @param file
     * @param bytes
     */
    public static void writeToFile(@Nonnull File file, @Nonnull byte[] bytes) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        if (bytes == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "bytes"));
        }
        
        Files.write(file.toPath(), bytes);
        
        
    }
    
    public static void writeToFile(@Nonnull File file, @Nonnull String text, @Nonnull Charset charset) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "file"));
        }
        if (text == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "text"));
        }
        if (charset == null) {
            throw new IllegalArgumentException(String.format(Constants.PARAMATER_WAS_NULL, "charset"));
        }
        makeDirsForFile(file);
        BufferedWriter out = null;
        
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
            out.append(text);
            out.flush();
            
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("IOException!", e);
                
            }
            
        }
    }
    
    public static void writeToFile(@Nonnull File filename, @Nonnull String content, @Nonnull String charset) throws IOException {
        writeToFile(filename, content, getCharset(charset));
        
    }
    
    /**
     * writes the text to the file, uses UTF-8
     *
     * @param filename
     * @param text
     */
    public static void writeToFile(@Nonnull File filename, @Nonnull String text) throws IOException {
        writeToFile(filename, text, StandardCharsets.UTF_8);
    }
    
    /**
     * writes the bytes to a given filename, encoded in UTF-8
     *
     * @param filename
     * @param bytes
     */
    public static void writeToFile(@Nonnull String filename, @Nonnull byte[] bytes) throws IOException {
        
        writeToFile(getFile(filename), bytes);
    }
    
    /**
     * writes the text to a given filename, encoded in UTF-8
     *
     * @param filename
     * @param text
     */
    
    public static void writeToFile(@Nonnull String filename, @Nonnull String text) throws IOException {
        
        writeToFile(getFile(filename), text);
    }
    
    /**
     * writes the bytes to a given filename, with a given charset
     *
     * @param filename
     * @param text
     */
    public static void writeToFile(@Nonnull String filename, @Nonnull String text, @Nonnull Charset charset) throws IOException {
        
        writeToFile(getFile(filename), text, charset);
    }
    
    /**
     * writes the bytes to a given filename, with a given charset
     *
     * @param filename
     * @param text
     */
    
    public static void writeToFile(@Nonnull String filename, @Nonnull String text, @Nonnull String charset) throws IOException {
        
        writeToFile(getFile(filename), text, charset);
    }
}

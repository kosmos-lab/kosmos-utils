
import de.kosmos_lab.utils.FileUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class FileUtilsTest {
    private static final String tmpdir = System.getProperty("java.io.tmpdir");
    
    private static final String DIR_PATTERN = tmpdir+"/%s";
    private static final String DIR_PATTERN_4 = tmpdir+"/%s/%s/%s/%s";
    private static String mydir = String.format(DIR_PATTERN, UUID.randomUUID().toString());
    
    @Test
    public void testFileDir() {
        File testDir = new File(String.format(DIR_PATTERN, UUID.randomUUID().toString()));
        while (testDir.exists()) {
            //should NEVER happen - technically...
            testDir = new File(String.format(DIR_PATTERN, UUID.randomUUID().toString()));
        }
        
        Assert.assertFalse(testDir.exists(), "Test file did already exist...wtf");
        //create a multilayer directory structure - that does NOT exist
        File testFile2 = new File(String.format(DIR_PATTERN_4, testDir.getName(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        Assert.assertEquals(testFile2.getParentFile().getParentFile().getParentFile(),testDir, "directory structure does not match...");
        Assert.assertFalse(testFile2.exists(),"Test file did already exist");
        Assert.assertFalse(testFile2.getParentFile().getParentFile().exists(), "Parent² of Test file did already exist");
        Assert.assertFalse(testFile2.getParentFile().exists(), "Parent of Test file did already exist");
        try {
            FileUtils.readBinary(testFile2);
            Assert.fail("did not throw exception?");
        } catch (IOException e) {
            //e.printStackTrace();
        }
        try {
            FileUtils.readFile(testFile2);
            Assert.fail("did not throw exception?");
        } catch (IOException e) {
            //e.printStackTrace();
        }
    
        try {
            FileUtils.writeToFile(testFile2, UUID.randomUUID().toString());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    
        Assert.assertTrue(testDir.exists(), "Test file did NOT exist after creation");
        Assert.assertTrue(testFile2.getParentFile().getParentFile().exists(), "Parent² of Test file did NOT exist");
        Assert.assertTrue(testFile2.getParentFile().exists(), "Parent of Test file did NOT exist");
        Assert.assertTrue(testFile2.exists(), "Test file did NOT exist");
        
        Assert.assertTrue(FileUtils.deleteDirectory(testDir),"delete returned false");
        Assert.assertFalse(testDir.exists(), "Test folder did still exist");
        Assert.assertFalse(testFile2.getParentFile().getParentFile().exists(), "Parent² of Test file did still exist");
        Assert.assertFalse(testFile2.getParentFile().exists(), "Parent of Test file did still exist");
        Assert.assertFalse(testFile2.exists(), "Test file did still exist");
        
        
        
        
    }
    
    //these tests should NOT raise an error for spotbugs
    @SuppressFBWarnings({"NP_NONNULL_PARAM_VIOLATION", "NP_NONNULL_PARAM_VIOLATION"})
    @Test void testNullResponses() {
        try {
            try {
                FileUtils.readFile((File) null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.fail("did not throw exception?");
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        }
        try {
            try {
                FileUtils.readFile((String) null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.fail("did not throw exception?");
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        }
    
        try {
            try {
                FileUtils.readBinary((File) null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.fail("did not throw exception?");
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        }
        try {
            try {
                FileUtils.readBinary((String) null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.fail("did not throw exception?");
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
        }
    }
    
    
    @Test void testIOException() {
        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("windows")) {
            try {
        
                File myFile = new File(mydir + "/locked.file");
                FileUtils.writeToFile(myFile, "locked file");
                final RandomAccessFile raFile = new RandomAccessFile(myFile, "rw");
                FileLock lock = raFile.getChannel().lock();
        
                try {
                    FileUtils.readFile(myFile);
                    Assert.fail("did not throw IOException?");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
        
        
                try {
                    FileUtils.readBinary(myFile);
                    Assert.fail("did not throw IOException?");
                } catch (IOException e) {
                    //e.printStackTrace();
                }
        
                lock.close(); //close the lock (also releases it)
        
                raFile.close(); //close the file
        
        
                Assert.assertTrue(myFile.delete(), "could not delete lock file!"); //get rid of the file!
        
            } catch (IllegalArgumentException e) {
                //e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    
        }
    }
    @Test
    public void testFileRead() {
        Assert.assertNotNull(mydir);
        String expectedText = UUID.randomUUID().toString()+System.getProperty("line.separator")+UUID.randomUUID().toString();
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText);
            String actualText = FileUtils.readFile(mydir + "/TestFileRead.txt");
            Assert.assertEquals(actualText, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        expectedText = UUID.randomUUID().toString();
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText, "ASCII");
            String text2 = FileUtils.readFile(mydir + "/TestFileRead.txt", "ASCII");
            Assert.assertEquals(text2, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        expectedText = "äöü#+aadsœ¼";
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText);
            String text2 = FileUtils.readFile(mydir + "/TestFileRead.txt");
            Assert.assertEquals(text2, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        expectedText = "äöü#+aads¼";
        
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText, StandardCharsets.ISO_8859_1);
            String text2 = FileUtils.readFile(mydir + "/TestFileRead.txt", StandardCharsets.ISO_8859_1);
            Assert.assertEquals(text2, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        
        expectedText = "äöü#+aadsœ¼";
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText.getBytes(StandardCharsets.UTF_8));
            String text2 = FileUtils.readFile(mydir + "/TestFileRead.txt");
            Assert.assertEquals(text2, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        expectedText = "äöü#+aads¼";
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.txt", expectedText.getBytes(StandardCharsets.ISO_8859_1));
            String text2 = FileUtils.readFile(mydir + "/TestFileRead.txt", StandardCharsets.ISO_8859_1);
            Assert.assertEquals(text2, expectedText, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        
        
        byte[] expectedBytes = expectedText.getBytes(StandardCharsets.ISO_8859_1);
        
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.bin", expectedBytes);
    
            byte[] actualBytes = FileUtils.readBinary(mydir + "/TestFileRead.bin");
            Assert.assertEquals(actualBytes, expectedBytes, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        
        expectedBytes = new byte[1024 * 10];
    
    
        Random random = new Random();
        random.nextBytes(expectedBytes);
        
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.bin", expectedBytes);
    
            byte[] actualBytes = FileUtils.readBinary(mydir + "/TestFileRead.bin");
            Assert.assertEquals(actualBytes, expectedBytes, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        random.nextBytes(expectedBytes);
    
        try {
            FileUtils.writeToFile(mydir + "/TestFileRead.bin", expectedBytes);
    
            byte[] actualBytes = FileUtils.readBinary(mydir + "/TestFileRead.bin");
            Assert.assertEquals(actualBytes, expectedBytes, "Files did NOT match up");
        } catch (FileNotFoundException e) {
            Assert.fail("could not find file again!?");
            e.printStackTrace();
        }catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        
        
    }
    
    @AfterTest
    public void cleanup() {
        File f = new File(mydir);
        // FileUtils.deleteDirectory(f);
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
            Assert.assertFalse(f.exists(), "folder did still exist?!");
        }
    }
    
    @BeforeTest
    public static void setUp() {
        File f = new File(mydir);
        
        while (f.exists()) {
            mydir = String.format(DIR_PATTERN, UUID.randomUUID().toString());
            f = new File(mydir);
        }
        
    }
    
}

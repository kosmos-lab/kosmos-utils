
import de.kosmos_lab.utils.StringFunctions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class StringFunctionsTest {

    @Test
    public void testFormat() {
        HashMap values = new HashMap<String,Object>();
        values.put("user","Karl");
        Assert.assertEquals(StringFunctions.format("Hallo {user}",values),"Hallo Karl");
        values.put("user","Herbert");
        Assert.assertEquals(StringFunctions.format("Hallo {user}",values),"Hallo Herbert");
        Assert.assertEquals(StringFunctions.format("Hallo {} {user}",values),"Hallo {} Herbert");

        try {
            Assert.assertEquals(StringFunctions.format("Hallo {s} {user}", values), "Hallo {} Herbert");
            Assert.fail("should have failed!");
        } catch (IllegalArgumentException ex) {

        }
        Assert.assertEquals(StringFunctions.format("Hallo {user}",new Object[][]{{"user","Anna"}}),"Hallo Anna");
        Assert.assertEquals(StringFunctions.format("Hallo {user}, ich bin {user2}",new Object[][]{{"user","Anna"},{"user2","Karl"}}),"Hallo Anna, ich bin Karl");
        Assert.assertEquals(StringFunctions.format("Hallo {user}, ich bin {user2}", Map.of("user","Anna","user2","Karl")),"Hallo Anna, ich bin Karl");



    }
    
    @Test
    public void testRandomKey() {
        
        Assert.assertEquals(StringFunctions.generateRandomKey().length(),26,1,"the length of the default key was wrong");
        //it miiight happen that we get only 63 chars here, thats okay
        Assert.assertEquals(StringFunctions.generateRandomKey(64).length(),64,1,"the generated key did not have 64 chars");
        Assert.assertNotEquals(StringFunctions.generateRandomKey(),StringFunctions.generateRandomKey(),"two random strings should be the same!");
    }
    @Test
    public void testTrim() {
        Assert.assertEquals(StringFunctions.trim(" test"),"test");
        Assert.assertEquals(StringFunctions.trim(" test "),"test");
        Assert.assertEquals(StringFunctions.trim("test "),"test");
    }
    @Test
    public void testDoubleTrim() {
        Assert.assertEquals(StringFunctions.trimDoubleSpaces(" te  st")," te st");
        Assert.assertEquals(StringFunctions.trimDoubleSpaces(" test  ")," test ");
        Assert.assertEquals(StringFunctions.trimDoubleSpaces("  test ")," test ");
    }
    
    
    @Test
    public void testReplaceEverythingButNumbers() {
        
        Assert.assertEquals(StringFunctions.replaceEverythingButNumbers("12412asdaf11241"),"1241211241");
        
    }
    @Test
    public void testCamelCase() {
        
        Assert.assertEquals(StringFunctions.toCamelCase("this is a test"),"thisIsATest");
        
    }
    
    @Test
    public void testFilterFilename() {
        
        Assert.assertEquals(StringFunctions.filterFilename("this is a test.txt"),"this is a test.txt");
        Assert.assertEquals(StringFunctions.filterFilename("/this is a test.txt"),"this is a test.txt");
        
    }
    
    @Test
    public void testfilterName() {
        Assert.assertEquals(StringFunctions.filterName("this is a test.txt"),"this is a testtxt");
        
        
    }
}

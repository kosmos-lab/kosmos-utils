
import de.kosmos_lab.utils.StringFunctions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StringFunctionsTest {
    
    
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

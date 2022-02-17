
import de.kosmos_lab.utils.JSONFunctions;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONFunctionTests {
    @Test
    public void testHasWithPathing() {
        JSONObject json = new JSONObject();
        
        Assert.assertFalse(JSONFunctions.has(json,"/test"));
        json.put("test","1");
        Assert.assertTrue(JSONFunctions.has(json,"/test"));
        Assert.assertTrue(JSONFunctions.has(json,"test"));
        Assert.assertFalse(JSONFunctions.has(json,"test2"));
        json.put("test",new JSONObject().put("test2",1));
        Assert.assertTrue(JSONFunctions.has(json,"/test/test2"));
        Assert.assertFalse(JSONFunctions.has(json,"/test/test"));
        Assert.assertTrue(JSONFunctions.has(json,"///test///test2"));
    
    }
    
    
    @Test
    public void testGetWithPathing() {
        JSONObject json = new JSONObject();
        
        Object obj = JSONFunctions.get(json,"/test");
        Assert.assertNull(obj);
        json.put("test","1");
        obj = JSONFunctions.get(json,"/test");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj,"1");
        obj = JSONFunctions.get(json,"test");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj,"1");
        obj = JSONFunctions.get(json,"test2");
        Assert.assertNull(obj);
        json.put("test",new JSONObject().put("test2",1));
        obj = JSONFunctions.get(json,"/test/test2");
        Assert.assertNotNull(obj);
        obj = JSONFunctions.get(json,"/test/test");
        Assert.assertNull(obj);
        obj = JSONFunctions.get(json,"///test///test2");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj,1);
        
    }
}


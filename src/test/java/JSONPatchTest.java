
import de.kosmos_lab.utils.JSONChecker;
import de.kosmos_lab.utils.JSONPatch;
import de.kosmos_lab.utils.exceptions.CompareException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONPatchTest {
    protected static final Logger logger = LoggerFactory.getLogger("JSONPatchTest");
    
    /**
     * Used for internal Testing, will create a patch, compare it with what should have been the patch.
     * Afterwards it applies the patch and checks if the resulting object is the correct object
     *
     * @param current       the current JSONObject
     * @param target        the target JSONObject we want to reach
     * @param expectedPatch the expected JSONArray patch
     */
    private static void testPatch(JSONObject current, JSONObject target, JSONArray expectedPatch) {
        logger.info("creating patch for {} to become {}", current, target);
        JSONArray patch = JSONPatch.getPatch(current, target);
        Assert.assertEquals(patch.length(), expectedPatch.length(), "patch differed!" + patch.toString() + " vs " + expectedPatch.toString());
        Assert.assertEquals(patch.toString(), expectedPatch.toString());
        JSONObject patched = JSONPatch.apply(current, patch);
        patch = JSONPatch.getPatch(patched, target);
        Assert.assertEquals(patch.length(), 0);
        try {
            JSONChecker.compare(patched, target);
        } catch (CompareException e) {
            Assert.fail("Patch failed!" + e.getMessage());
        }
    }
    
    @Test
    public void testAddAndRemove() {
        testPatch(new JSONObject().put("b", "b"), new JSONObject().put("a", new JSONObject().put("b", "c")), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":{\"b\":\"c\"}},{\"op\":\"remove\",\"path\":\"/b\"}]"));
        
    }
    
    @Test
    public void testReplace1() {
        testPatch(new JSONObject().put("a", "c"), new JSONObject().put("a", "b"), new JSONArray("[{\"op\":\"replace\",\"path\":\"/a\",\"value\":\"b\"}]"));
    }
    
    @Test
    public void testReplace2() {
        testPatch(new JSONObject().put("a", "b"), new JSONObject().put("a", new JSONObject().put("b", "c")), new JSONArray("[{\"op\":\"replace\",\"path\":\"/a\",\"value\":{\"b\":\"c\"}}]"));
    }
    
    @Test
    public void testReplace3() {
        testPatch(new JSONObject().put("a", "c"), new JSONObject().put("a", 1), new JSONArray("[{\"op\":\"replace\",\"path\":\"/a\",\"value\":1}]"));
    }
    
    @Test
    public void testAddBoolean() {
        testPatch(new JSONObject(), new JSONObject().put("a", true), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":true}]"));
    }
    
    @Test
    public void testAddDouble() {
        testPatch(new JSONObject(), new JSONObject().put("a", 1.1d), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":1.1}]"));
    }
    
    @Test
    public void testAddInt() {
        testPatch(new JSONObject(), new JSONObject().put("a", 1), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":1}]"));
    }
    
    @Test
    public void testAddLong() {
        testPatch(new JSONObject(), new JSONObject().put("a", 1000000000000l), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":1000000000000}]"));
    }
    
    @Test
    public void testEmptyJSONObject() {
        testPatch(new JSONObject(), new JSONObject(), new JSONArray());
    }
    
    @Test
    public void testEmptySourceJSONObject() {
        testPatch(new JSONObject(), new JSONObject().put("b", new JSONObject().put("b", "b")), new JSONArray("[{\"op\":\"add\",\"path\":\"/b\",\"value\":{\"b\":\"b\"}}]"));
    }
    
    @Test
    public void testEmptySourceString1() {
        testPatch(new JSONObject(), new JSONObject().put("a", "b"), new JSONArray("[{\"op\":\"add\",\"path\":\"/a\",\"value\":\"b\"}]"));
        
    }
    
    @Test
    public void testEmptySourceString2() {
        testPatch(new JSONObject(), new JSONObject().put("b", "b"), new JSONArray("[{\"op\":\"add\",\"path\":\"/b\",\"value\":\"b\"}]"));
    }
    
    @Test
    public void testReplaceAndAddJSONObject() {
        testPatch(new JSONObject().put("a", new JSONObject().put("b", "c")), new JSONObject().put("a", new JSONObject().put("a", "c").put("b", "d")), new JSONArray("[{\"op\":\"add\",\"path\":\"a/a\",\"value\":\"c\"},{\"op\":\"replace\",\"path\":\"a/b\",\"value\":\"d\"}]"));
        
    }
    
    @Test
    public void testReplaceAndAddString() {
        testPatch(new JSONObject().put("a", new JSONObject().put("b", "c")), new JSONObject().put("b", true), new JSONArray("[{\"op\":\"add\",\"path\":\"/b\",\"value\":true},{\"op\":\"remove\",\"path\":\"/a\"}]"));
        
    }
    
    @Test
    public void testReplaceJSONObject() {
        testPatch(new JSONObject().put("a", false), new JSONObject().put("a", true), new JSONArray("[{\"op\":\"replace\",\"path\":\"/a\",\"value\":true}]"));
        
    }
    
    @Test
    public void testSameJSON1() {
        testPatch(new JSONObject().put("a", "b"), new JSONObject().put("a", "b"), new JSONArray());
        
    }
    
    @Test
    public void testSameJSON2() {
        
        testPatch(new JSONObject().put("a", 0), new JSONObject().put("a", 0), new JSONArray());
        
    }
    
    @Test
    public void testSameJSON3() {
        testPatch(new JSONObject().put("a", 0l), new JSONObject().put("a", 0l), new JSONArray());
        
    }
    
    @Test
    public void testSameJSON4() {
        testPatch(new JSONObject().put("a", 0), new JSONObject().put("a", 0l), new JSONArray());
        
    }
    
    @Test
    public void testSameJSON5() {
        
        testPatch(new JSONObject().put("a", 0l), new JSONObject().put("a", 0), new JSONArray());
    }
}

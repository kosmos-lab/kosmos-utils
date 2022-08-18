
import de.kosmos_lab.utils.JSONFunctions;
import de.kosmos_lab.utils.StringFunctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JSONFunctionTests {
    @Test
    public void testHasWithPathing() {
        JSONObject json = new JSONObject();

        Assert.assertFalse(JSONFunctions.has(json, "/test"));
        json.put("test", "1");
        Assert.assertTrue(JSONFunctions.has(json, "/test"));
        Assert.assertTrue(JSONFunctions.has(json, "test"));
        Assert.assertFalse(JSONFunctions.has(json, "test2"));
        json.put("test", new JSONObject().put("test2", 1));
        Assert.assertTrue(JSONFunctions.has(json, "/test/test2"));
        Assert.assertFalse(JSONFunctions.has(json, "/test/test"));
        Assert.assertTrue(JSONFunctions.has(json, "///test///test2"));

    }

    @Test
    public void testKeys() {
        JSONObject json = new JSONObject();
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{}));
        json.put("test",1);
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{"test"}));
        json.put("a-test",1);
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{"a-test","test"}));
        json.put("b-test",1);
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{"a-test","b-test","test"}));
        json.put("arr",new JSONArray().put(1).put(2));
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{"arr[0]","arr[1]","a-test","b-test","test"}));
        json.put("arr2",new JSONArray().put(new JSONArray().put(1).put(2)));
        Assert.assertTrue(StringFunctions.identical(JSONFunctions.keys(json,null), new String[]{"arr2[0]","arr2[0][0]","arr2[0][1]","arr[0]","arr[1]","a-test","b-test","test"}));
    }
    @Test
    public void testGetWithPathing() {
        JSONObject json = new JSONObject();

        Object obj = JSONFunctions.get(json, "/test");
        Assert.assertNull(obj);
        json.put("test", "1");
        obj = JSONFunctions.get(json, "/test");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj, "1");
        obj = JSONFunctions.get(json, "test");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj, "1");
        obj = JSONFunctions.get(json, "test2");
        Assert.assertNull(obj);
        json.put("test", new JSONObject().put("test2", 1));
        obj = JSONFunctions.get(json, "/test/test2");
        Assert.assertNotNull(obj);
        obj = JSONFunctions.get(json, "/test/test");
        Assert.assertNull(obj);
        obj = JSONFunctions.get(json, "///test///test2");
        Assert.assertNotNull(obj);
        Assert.assertEquals(obj, 1);
        json = new JSONObject();
        json.put("test", new JSONArray().put(0).put(1).put(3));
        Assert.assertEquals(JSONFunctions.get(json, "test[0]"), 0);
        Assert.assertEquals(JSONFunctions.get(json, "test[1]"), 1);
        Assert.assertEquals(JSONFunctions.get(json, "test[2]"), 3);
        JSONObject test2 = new JSONObject().put("test2", json);
        Assert.assertEquals(JSONFunctions.get(test2, "test2/test[0]"), 0);
        Assert.assertEquals(JSONFunctions.get(test2, "test2/test[1]"), 1);
        Assert.assertEquals(JSONFunctions.get(test2, "test2/test[2]"), 3);
        JSONObject test3 = new JSONObject().put("test3", new JSONArray().put(json));
        Assert.assertEquals(JSONFunctions.get(test3, "test3[0]/test[0]"), 0);
        Assert.assertEquals(JSONFunctions.get(test3, "test3[0]/test[1]"), 1);
        Assert.assertEquals(JSONFunctions.get(test3, "test3[0]/test[2]"), 3);
        JSONObject weather = new JSONObject("{\"coord\":{\"lon\":8.8078,\"lat\":53.0752},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"base\":\"stations\",\"main\":{\"temp\":19.44,\"feels_like\":19.55,\"temp_min\":17.12,\"temp_max\":19.89,\"pressure\":1015,\"humidity\":81},\"visibility\":10000,\"wind\":{\"speed\":2.06,\"deg\":250},\"clouds\":{\"all\":100},\"dt\":1656330604,\"sys\":{\"type\":1,\"id\":1281,\"country\":\"DE\",\"sunrise\":1656298811,\"sunset\":1656359711},\"timezone\":7200,\"id\":2944388,\"name\":\"Bremen\",\"cod\":200}");
        Assert.assertEquals(JSONFunctions.get(weather, "coord/lon"), 8.8078);
        Assert.assertEquals(JSONFunctions.get(weather, "weather[0]/id"), 804);
        Assert.assertEquals(JSONFunctions.get(weather, "clouds/all"), 100);
    }
}


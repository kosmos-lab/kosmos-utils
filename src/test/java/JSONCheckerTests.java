import de.kosmos_lab.utils.JSONChecker;
import de.kosmos_lab.utils.exceptions.CompareException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONCheckerTests {
    protected static final Logger logger = LoggerFactory.getLogger("JSONCheckerTests");

    public boolean doCompare(Object value, Object expected) {

        try {
            Assert.assertTrue(JSONChecker.equals(value, expected));
            return true;
        } catch (CompareException e) {
            //e.printStackTrace();
        }
        return false;
    }

    public boolean doCompare(JSONObject value, JSONObject expected, String[] skippedKeys) {

        try {
            Assert.assertTrue(JSONChecker.compare(value, expected, skippedKeys));
            return true;
        } catch (CompareException e) {
            //e.printStackTrace();
        }
        return false;
    }

    @Test
    public void testDiff() {

        Object[][] dataset = {
                /*{
                        new JSONObject().put("a", "a"),
                        new JSONObject().put("a", "a"),
                        new JSONArray()
                },*/
                {
                        new JSONObject().put("a", "a"),
                        new JSONObject().put("a", "a"),
                        new HashSet<String>()
                },

                /*{
                        new JSONObject().put("a", "a"),
                        new JSONObject().put("a", "b"),
                        new JSONArray().put("a")
                },*/
                {
                        new JSONObject().put("a", "a"),
                        new JSONObject().put("a", "b"),
                        Sets.newHashSet("a")
                },
                /*{
                        new JSONObject().put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("a", new JSONObject().put("a", "b")),
                        new JSONArray().put("a/a")
                },*/
                {
                        new JSONObject().put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("a", new JSONObject().put("a", "b")),
                        Sets.newHashSet("a/a")
                },
                /*{
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("a", new JSONObject().put("a", "b")),
                        new JSONArray().put("a/a").put("c")
                },*/
                {
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("a", new JSONObject().put("a", "b")),
                        Sets.newHashSet("a/a", "c")
                },
                /*{
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "b")),
                        new JSONArray().put("a/a")
                },*/
                {
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "b")),
                        Sets.newHashSet("a/a")
                },
/*                {
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new JSONArray()
                },*/
                {
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new HashSet<String>()
                },
                /*{
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "d").put("a", new JSONObject().put("a", "a")))),
                        new JSONArray().put("a/a/c")
                },*/
                {
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "c").put("a", new JSONObject().put("a", "a")))),
                        new JSONObject().put("c", "c").put("a", new JSONObject().put("a", new JSONObject().put("c", "d").put("a", new JSONObject().put("a", "a")))),
                        Sets.newHashSet("a/a/c")
                }
        };
        for (Object[] set : dataset) {
            try {
                if (set[1] instanceof JSONObject && set[0] instanceof JSONObject) {
                    if (set[2] instanceof JSONArray) {
                        Set<String> dset = JSONChecker.diff((JSONObject) set[0], (JSONObject) set[1]);

                        JSONArray diff = new JSONArray(dset);
                        logger.info("diff {} vs {} = {}=={}", set[0], set[1], set[2], dset);
                        JSONChecker.equals(diff, set[2]);
                    } else if (set[2] instanceof HashSet) {
                        Set<String> dset = JSONChecker.diff((JSONObject) set[0], (JSONObject) set[1]);
                        logger.info("diff  {} vs {} = {}=={}", set[0], set[1], set[2], dset);
                        if (!dset.equals(set[2])) {
                            Assert.fail("did not have correct value for diff");
                        }
                    }
                    else {
                        Assert.fail(String.format("could not understand compare example %s ", set[2].getClass()));
                    }

                } else {
                    Assert.fail(String.format("could not compare %s and %s", set[0].getClass(), set[1].getClass()));

                }


            } catch (CompareException e) {
                Assert.fail("did not have corrent value for diff", e);
            }
        }


    }

    @Test
    public void testContains() {
        Assert.assertFalse(JSONChecker.contains(null, null));
        Assert.assertFalse(JSONChecker.contains(null, new JSONObject()));
        Assert.assertFalse(JSONChecker.contains(new JSONArray(), null));
        Assert.assertFalse(JSONChecker.contains(new JSONArray(), new JSONObject()));

        Assert.assertFalse(JSONChecker.contains(new JSONArray().put(new JSONObject().put("name", "test")), new JSONObject().put("name", "test2")));
        Assert.assertTrue(JSONChecker.contains(new JSONArray().put(new JSONObject().put("name", "test")), new JSONObject().put("name", "test")));
        Assert.assertTrue(JSONChecker.contains(new JSONArray().put(new JSONObject().put("name", "test3").put("name", "test")), new JSONObject().put("name", "test")));
        Assert.assertFalse(JSONChecker.contains(new JSONArray().put(new JSONObject().put("name", "test3").put("name", "test")), new JSONObject().put("asd", "test")));

    }

    @Test
    public void testNull() {
        Assert.assertTrue(doCompare(null, null));
        Assert.assertFalse(doCompare("10", null));
        Assert.assertFalse(doCompare("ten", null));
        Assert.assertFalse(doCompare(null, "10"));

    }

    @Test
    public void testNumbers() {
        Assert.assertTrue(doCompare(10, 10));
        Assert.assertTrue(doCompare("10", 10));
        Assert.assertTrue(doCompare(10l, 10l));
        Assert.assertTrue(doCompare(10d, 10d));
        Assert.assertTrue(doCompare("10", 10l));
        Assert.assertTrue(doCompare("10", 10d));
        Assert.assertFalse(doCompare("ten", 10d));
        Assert.assertFalse(doCompare("ten", 10l));
        Assert.assertFalse(doCompare("ten", 10f));
        Assert.assertFalse(doCompare("ten", 10));
        Assert.assertFalse(doCompare("10", 11l));
        Assert.assertFalse(doCompare("10", 11f));

        Assert.assertFalse(doCompare("10", 113));
    }

    @Test
    public void testBoolean() {
        Assert.assertTrue(doCompare(true, true));
        Assert.assertTrue(doCompare(false, false));

        Assert.assertTrue(doCompare("true", true));
        Assert.assertTrue(doCompare("false", false));

        Assert.assertTrue(doCompare("true", "true"));
        Assert.assertFalse(doCompare("true", false));
        Assert.assertFalse(doCompare("truee", true));
        Assert.assertFalse(doCompare("true", "false"));
    }

    @Test
    public void testJSONObject() {
        Assert.assertTrue(doCompare(new JSONObject(), new JSONObject()));
        Assert.assertTrue(doCompare(new JSONObject().put("a", "a").put("b", "b"), new JSONObject().put("b", "b").put("a", "a")));
        Assert.assertTrue(doCompare(new JSONObject(), new JSONObject()));
        Assert.assertTrue(doCompare(new JSONObject().put("a", "a").put("b", "b"), new JSONObject().put("b", "b").put("a", "a")));


        Assert.assertFalse(doCompare(new JSONObject(), new JSONArray().put(1).put(2).put(3)));
        Assert.assertFalse(doCompare(new JSONObject().put("a", "b").put("b", "a"), new JSONObject().put("b", "b").put("a", "a")));
        Assert.assertFalse(doCompare(new JSONObject().put("a", "a").put("b", "b").put("c", "c"), new JSONObject().put("b", "b").put("a", "a")));

        Assert.assertFalse(doCompare(new JSONObject().put("b", "b").put("a", "a"), new JSONObject().put("a", "a").put("b", "b").put("c", "c")));

        Assert.assertFalse(doCompare(new JSONObject().put("a", "a").put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test2"))), new JSONObject().put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test"))).put("a", "a")));
        Assert.assertFalse(doCompare(new JSONObject().put("a", "a").put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false))), new JSONObject().put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test"))).put("a", "a")));
        Assert.assertFalse(doCompare(new JSONObject().put("a", "a").put("c", "c").put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put("test"))), new JSONObject().put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test"))).put("a", "a")));

        Assert.assertTrue(doCompare(new JSONObject().put("a", "a").put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test"))), new JSONObject().put("b", new JSONObject().put("c", "c").put("d", new JSONArray().put(1).put(false).put("test"))).put("a", "a")));
    }

    @Test
    public void testJSONObjectWithSkipped() {
        Assert.assertTrue(doCompare(new JSONObject().put("a", "a").put("b", "b").put("c", "b"), new JSONObject().put("b", "b").put("a", "a"), new String[]{"c"}));
        Assert.assertFalse(doCompare(new JSONObject().put("a", "a").put("b", "b").put("c", "b"), new JSONObject().put("b", "b").put("a", "a"), new String[]{"d"}));

    }

    @Test
    public void testJSONArray() {
        Assert.assertTrue(doCompare(new JSONArray(), new JSONArray()));
        Assert.assertTrue(doCompare(new JSONArray().put(1).put(2).put(3), new JSONArray().put(1).put(2).put(3)));
        Assert.assertFalse(doCompare(new JSONArray().put(3).put(2).put(1), new JSONArray().put(1).put(2).put(3)));
        Assert.assertFalse(doCompare(new JSONArray().put(3).put(2).put(1), new JSONArray().put(1).put(2).put(3)));
        Assert.assertFalse(doCompare(new JSONArray(), new JSONArray().put(1).put(2).put(3)));
    }


}

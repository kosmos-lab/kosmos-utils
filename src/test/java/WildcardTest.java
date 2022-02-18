
import de.kosmos_lab.utils.Wildcard;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WildcardTest {
    
    @SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
    @Test
    public void testWildCardNull() {
        Assert.assertFalse(Wildcard.matches(null,"test"));
        Assert.assertTrue(Wildcard.matches(null,null));
        Assert.assertFalse(Wildcard.matches("test",null));
    }
    @Test
    public void testWildCardStar() {
        Assert.assertTrue(Wildcard.matches("te*t","test"));
        Assert.assertTrue(Wildcard.matches("te*t","tesst"));
        Assert.assertTrue(Wildcard.matches("t**t","test"));
        Assert.assertTrue(Wildcard.matches("t**t","tst"));
        Assert.assertTrue(Wildcard.matches("127.0.0.*","127.0.0.1"));
        Assert.assertTrue(Wildcard.matches("127.0.0.*","127.0.0.15"));
        Assert.assertFalse(Wildcard.matches("t**t","Test"));
        Assert.assertFalse(Wildcard.matches("127.0.0.*","127.0.1.15"));
    }
    @Test
    public void testWildCardQuestionMark(){
        
    
        Assert.assertTrue(Wildcard.matches("te?t","test"));
        Assert.assertFalse(Wildcard.matches("te?t","tesst"));
        Assert.assertTrue(Wildcard.matches("t??t","test"));
        Assert.assertFalse(Wildcard.matches("t??t","tst"));
        Assert.assertTrue(Wildcard.matches("127.0.0.?","127.0.0.1"));
        
        Assert.assertFalse(Wildcard.matches("127.0.0.?","127.0.0.15"));
        Assert.assertTrue(Wildcard.matches("127.0.0.??","127.0.0.15"));
        Assert.assertFalse(Wildcard.matches("t**t","Test"));
        Assert.assertFalse(Wildcard.matches("127.0.0.*","127.0.1.15"));
    }
}

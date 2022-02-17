
import de.kosmos_lab.utils.DoubleFunctions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DoubleFunctionsTest {
    
    @SuppressFBWarnings("BX_UNBOXING_IMMEDIATELY_REBOXED")
    @Test
    public void testRound() {
        Assert.assertEquals(DoubleFunctions.roundToNearest(0.8,0.25).doubleValue(),0.75);
        Assert.assertEquals(DoubleFunctions.roundToNearest(1,0.25).doubleValue(),1.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(1.1,0.25).doubleValue(),1.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(0.25,0.25).doubleValue(),0.25);
        Assert.assertEquals(DoubleFunctions.roundToNearest(100,5).doubleValue(),100.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(102,5).doubleValue(),100.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(107,5).doubleValue(),105.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(107,10).doubleValue(),110.0);
        Assert.assertEquals(DoubleFunctions.roundToNearest(105,10).doubleValue(),100.0);
        
    }
}

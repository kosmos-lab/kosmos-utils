
import de.kosmos_lab.utils.IntegerFunctions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IntegerFunctionsTest {
    
    @Test
    public void testRound() {

        Assert.assertEquals(IntegerFunctions.roundToNearest(100,5),100);
        Assert.assertEquals(IntegerFunctions.roundToNearest(102,5),100);
        Assert.assertEquals(IntegerFunctions.roundToNearest(107,5),105);
        Assert.assertEquals(IntegerFunctions.roundToNearest(107,10),110);
        Assert.assertEquals(IntegerFunctions.roundToNearest(105,10),100);
        
    }
}

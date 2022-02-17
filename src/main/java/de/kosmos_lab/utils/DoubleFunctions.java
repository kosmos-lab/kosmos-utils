package de.kosmos_lab.utils;

/**
 * This class is used to implement Features for doubles that one might need.
 */
public class DoubleFunctions {
    
    /**
     * round to nearest sensible double
     *
     * @param value      the input value
     * @param multiplier the multiplier to check
     * @return the nearest value defined by multiplier ( roundToNearest(8,5) = 10,roundToNearest(6,10) = 10,roundToNearest(0.8,0.25) = 0.75)
     */
    public static Double roundToNearest(double value, double multiplier) {
        
        if ((value % multiplier) > multiplier / 2) {
            return value + multiplier - value % multiplier;
            
        }
        return value - value % multiplier;
        
    }
}

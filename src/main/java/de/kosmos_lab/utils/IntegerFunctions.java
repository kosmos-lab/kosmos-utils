package de.kosmos_lab.utils;

public class IntegerFunctions {
    /**
     * round to nearest sensible int
     *
     * @param value      the input value
     * @param multiplier the multiplier to check
     * @return the nearest value defined by multiplier ( roundToNearest(8,5) = 10,roundToNearest(6,10) = 10)
     */
    public static int roundToNearest(int value, int multiplier) {
        int a = (value / multiplier) * multiplier;
        int b = a + multiplier;
        
        if ((b - value) >= (value - a)) {
            return a;
        }
        return b;
    }
    
}

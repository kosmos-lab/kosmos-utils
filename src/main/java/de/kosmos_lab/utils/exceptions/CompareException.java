package de.kosmos_lab.utils.exceptions;

/**
 * this exception is thrown if a comparison failed for some reason
 */
public class CompareException extends Exception {
    public CompareException(String path, Object value, Object expected) {
        super(((path.length() > 0) ? (path + ": ") : ("")) + "Comparison failed: expected:'" + expected + "' vs found:'" + value + "'");
    }
    
    public CompareException(String path, String text) {
        super(((path.length() > 0) ? (path + ": ") : ("")) + "Comparison failed: " + text);
    }
    
    public CompareException(String text) {
        super(text);
    }
}

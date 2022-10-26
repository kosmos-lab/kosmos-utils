package de.kosmos_lab.utils.exceptions;

/**
 * this exception is thrown if a comparison failed for some reason
 */
public class CompareException extends Exception {
    public final String path;

    public CompareException(String path, Object value, Object expected) {

        super(((path.length() > 0) ? (path + ": ") : ("")) + "Comparison failed: expected:'" + expected + "' vs found:'" + value + "'");
        this.path = path;
    }
    
    public CompareException(String path, String text) {
        super(((path.length() > 0) ? (path + ": ") : ("")) + "Comparison failed: " + text);
        this.path = path;
    }
    
    public CompareException(String text) {
        super(text);
        this.path = "";
    }
}

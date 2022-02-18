package de.kosmos_lab.utils;

import javax.annotation.Nonnull;

/**
 * this class is used to parse
 */
public class Wildcard {
    
    /**
     * matches a string to pattern
     * where * can be 0-n unknown characters
     * and ? can be exactly 1 character
     *
     * <pre>
     * {@code
     * Wildcard.matches("Te?t","Test") -> true
     * Wildcard.matches("Te?t","Tesst") -> false
     * Wildcard.matches("Te*t","Tesst") -> true
     * Wildcard.matches("Te*t","Test") -> true
     * Wildcard.matches("Tet","Test") -> true
     * }
     * </pre>
     *
     * @param pattern the pattern to use
     * @param text    the text to match against
     * @return
     */
    public static boolean matches(@Nonnull String pattern, @Nonnull String text) {
        
        if (pattern == null) {
            return text == null; //technically weird but true
        }
        if (text == null) {
            return false;
        }
        // add sentinel so don't need to worry about *'s at end of pattern
        text += '\0';
        pattern += '\0';
        
        int N = pattern.length();
        
        boolean[] states = new boolean[N + 1];
        boolean[] old = new boolean[N + 1];
        old[0] = true;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            states = new boolean[N + 1];       // initialized to false
            for (int j = 0; j < N; j++) {
                char p = pattern.charAt(j);
                
                // hack to handle *'s that match 0 characters
                if (old[j] && (p == '*')) old[j + 1] = true;
                
                if (old[j] && (p == c)) states[j + 1] = true;
                if (old[j] && (p == '?')) states[j + 1] = true;
                if (old[j] && (p == '*')) states[j] = true;
                if (old[j] && (p == '*')) states[j + 1] = true;
            }
            old = states;
        }
        return states[N];
    }
}

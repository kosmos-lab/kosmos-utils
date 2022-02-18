package de.kosmos_lab.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;

public class HashFunctions {
    /**
     * Gets the SHA3.
     *
     * @param input the input
     * @return the SHA3
     */
    @Nonnull
    public static String getSHA3(@Nonnull final String input) {
        return DigestUtils.sha3_256Hex(input);
        
    }
    
    /**
     * gets a salted and peppered hash of a given input
     *
     * @param input  the input to hash
     * @param salt   the salt to use
     * @param pepper the pepper to use
     * @return a HEX Represenation of a SHA3
     */
    public static String getSaltedAndPepperdHash(final String input, String salt, String pepper) {
        return getSHA3(getSaltedHash(input, salt) + pepper);
    }
    
    /**
     * gets a salted hash of the given input
     *
     * @param input the input to hash
     * @param salt  the salt to use
     * @return a HEX Represenation of a SHA3
     */
    @Nonnull
    public static String getSaltedHash(final String input, String salt) {
        return getSHA3(getSHA3(input) + salt);
    }
    
}
package de.kosmos_lab.utils;

import org.json.JSONArray;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringFunctions {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("StringFunctions");

    private static final SecureRandom random = new SecureRandom();

    public static String format(String format, Map<String, Object> values) {
        StringBuilder formatter = new StringBuilder(format);
        List<Object> valueList = new ArrayList<Object>();

        Matcher matcher = Pattern.compile("\\{(\\w+)}").matcher(format);

        while (matcher.find()) {
            String key = matcher.group(1);

            String formatKey = String.format("{%s}", key);
            int index = formatter.indexOf(formatKey);

            if (index != -1) {
                formatter.replace(index, index + formatKey.length(), "%s");
                Object v = values.get(key);
                if (v != null) {
                    valueList.add(v);
                } else {
                    throw new IllegalArgumentException("value with the key " + key + " is not filled!");
                }
            }
        }

        return String.format(formatter.toString(), valueList.toArray());
    }

    public static String format(String format, Object[][] values) {
        HashMap<String, Object> vals = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            Object v = values[i][0];
            if (v instanceof String) {
                vals.put((String) values[i][0], values[i][1]);
            } else {
                vals.put(String.valueOf(values[i][0]), values[i][1]);
            }
        }
        return format(format,vals);
    }

    @Nullable
    public static String decompose(@CheckForNull final String s) {
        if (s == null) {
            return null;
        }
        return java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @Nullable
    public static String filterFilename(@CheckForNull String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^A-Za-z0-9 \\.\\-_]", "");
    }

    /**
     * removes anything from the text that is not alphanumeric
     *
     * @param input any inputtext
     *
     * @return the text with everything but Alphanumerics and spaces stripped
     */
    @Nonnull
    public static String filterName(@Nonnull final String input) {

        return trimDoubleSpaces(input.replaceAll("[^a-zA-Z0-9 ]", ""));

    }

    //static Pattern numPattern = Pattern.compile("([0-9])");

    /**
     * Generate random key.
     *
     * @return the string
     */
    @Nonnull
    public static String generateRandomKey() {

        return generateRandomKey(26);
    }

    /**
     * Generate random key.
     *
     * @return the string
     */
    @Nonnull
    public static String generateRandomKey(int len) {

        String s = "";
        while (s.length() != len) {
            s = new BigInteger(len * 5, random).toString(32);
        }
        return s;
    }

    @Nonnull
    public static HashSet<String> getListOfString(@Nonnull final LinkedList<HashSet<String>> wordList) {
        if (wordList.size() > 0) {
            HashSet<String> words = wordList.get(0);
            for (int i = 1; i < wordList.size(); i++) {
                final HashSet<String> w = wordList.get(i);
                final HashSet<String> twords = new HashSet<>();
                for (final String s : words) {

                    for (final String wo : w) {
                        twords.add(s + " " + wo);
                    }
                }
                words = twords;
            }
            return words;
        }
        return new HashSet<>();

    }

    @Nonnull
    public static String replaceEverythingButNumbers(@Nonnull String text) {
        return text.replaceAll("[^\\d.\\.]", "");

    }

    /**
     * camelcases the input, ie "to camel case" will yield "toCamelCase"
     *
     * @param input the string to camelcase
     *
     * @return the camelcased string
     */
    @Nullable
    public static String toCamelCase(@CheckForNull String input) {
        if (input == null) {
            return null;
        }
        // dont do it for the name ID
        if (input.equalsIgnoreCase("ID")) {
            return "ID";
        }
        input = trim(input);
        final StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (final String tok : input.split(" ")) {
            if (!tok.isEmpty()) {
                if (!first) {
                    ret.append(tok.substring(0, 1).toUpperCase(Locale.ENGLISH));
                    ret.append(tok.substring(1).toLowerCase(Locale.ENGLISH));
                } else {
                    first = false;
                    ret.append(tok.toLowerCase(Locale.ENGLISH));
                }
            }
        }

        return ret.toString();
    }

    /**
     * trims away space and comma at the beginning and end and also removes double spacing
     *
     * @param input the input to trim
     *
     * @return the trimmed input
     */
    @Nullable
    public static String trim(@CheckForNull final String input) {

        return trim(input, new char[]{',', ' ', (char) 160, ' '});

    }

    /**
     * trims away any given characters, also removes double spaces
     *
     * @param input the input
     * @param chrs  the array of characters to remove
     *
     * @return the trimmed input
     */
    @Nullable
    public static String trim(@CheckForNull String input, final char[] chrs) {
        if (input == null) {
            return null;
        }
        boolean changed = false;
        if (input.length() > 0) {
            for (final char c : chrs) {

                if (input.charAt(0) == c) {
                    input = input.substring(1);
                    changed = true;
                    break;
                }
                final int end = input.length() - 1;
                if (input.charAt(end) == c) {
                    input = input.substring(0, end);
                    changed = true;
                    break;
                }
            }
            if (changed) {
                return trim(input, chrs);
            }
        }

        return trimDoubleSpaces(input);

    }

    /**
     * trims away double spaces
     *
     * @param input the input to trim
     *
     * @return the trimmed string
     */
    @Nonnull
    public static String trimDoubleSpaces(@Nonnull String input) {
        String n = input.replace("  ", " ");
        while (!n.equals(input)) {
            input = n;
            n = input.replace("  ", " ");
        }
        return n;

    }

    public static boolean identical(Set<String> set, String[] list) {
        //we create a secondary set here with all entries
        logger.info("set: {}",new JSONArray(set).toString());
        logger.info("list: {}",list);
        Set<String> listSet = new HashSet<>();
        for (String e : list) {
            if (!set.contains(e)) {
                logger.warn("set did not contain element {}",e);
                return false;
            }
            listSet.add(e);
        }
        for ( String e : set) {
            //test against the secondary set
            if (!listSet.contains(e)) {
                logger.warn("array did not contain element {}",e);
                return false;
            }
        }
        return true;





    }
}
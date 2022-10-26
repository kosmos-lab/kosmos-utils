package de.kosmos_lab.utils;

import de.kosmos_lab.utils.exceptions.CompareException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class JSONChecker {
    public static boolean contains(JSONArray array, JSONObject jsonObject) {
        if (array != null && jsonObject != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject o = array.getJSONObject(i);

                    if (compare(jsonObject, o)) {
                        return true;
                    }
                } catch (CompareException ex) {
                    //    ex.printStackTrace();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean checkValue(JSONObject device, String key, Object expected) throws Exception {
        CompareException laste = null;
        if (device != null && device.has(key)) {
            try {
                if (JSONChecker.equals(device.get(key), expected)) {
                    return true;
                }
            } catch (CompareException ex) {
                //ex.printStackTrace();
            }
            if (expected instanceof Integer) {
                try {
                    int v = device.getInt(key);
                    //dont use assert here because we are allowed to fail a couple of times, but not forever
                    if (v == (Integer) expected) {
                        //get out
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    //e.printStackTrace();
                }
            } else if (expected instanceof Double) {
                try {
                    double v = device.getDouble(key);
                    //dont use assert here because we are allowed to fail a couple of times, but not forever
                    if (v == (Double) expected) {
                        //get out
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    //e.printStackTrace();
                }
            } else if (expected instanceof Long) {
                try {
                    long v = device.getLong(key);
                    //dont use assert here because we are allowed to fail a couple of times, but not forever
                    if (v == (Long) expected) {
                        //get out
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    //e.printStackTrace();
                }
            } else if (expected instanceof Float) {
                try {
                    float v = (float) device.getDouble(key);
                    //dont use assert here because we are allowed to fail a couple of times, but not forever
                    if (v == (Float) expected) {
                        //get out
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    //e.printStackTrace();
                }
            } else if (expected instanceof String) {
                try {
                    String v = device.getString(key);
                    if (v.equals(expected)) {
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    //e.printStackTrace();
                }
                Object v = device.opt(key);
                if (v != null) {
                    if (String.valueOf(v).equals(expected)) {
                        return true;
                    }
                }

            } else if (expected instanceof JSONObject) {
                try {
                    JSONObject o = (JSONObject) expected;
                    JSONObject v = device.getJSONObject(key);
                    if (o.toMap().equals(v.toMap())) {
                        return true;
                    }

                } catch (org.json.JSONException e) {
                    // e.printStackTrace();
                }
            } else if (expected instanceof JSONArray) {
                try {
                    JSONArray o = (JSONArray) expected;
                    JSONArray v = device.getJSONArray(key);
                    if (v.length() != o.length()) {
                        return false;
                    }
                    for (int i = 0; i < o.length(); i++) {
                        if (!o.get(i).equals(v.get(i))) {
                            return false;
                        }

                    }
                    return true;

                } catch (org.json.JSONException e) {
                    // e.printStackTrace();
                }
            } else if (expected instanceof Boolean) {
                Boolean o = (Boolean) expected;
                Boolean v = device.getBoolean(key);
                return o.equals(v);

            } else {
                throw new Exception("COULD NOT COMPARE " + expected.getClass());
            }

            return (expected.toString().equals(device.get(key)));

        }

        return false;
    }

    public static boolean compare(@CheckForNull JSONObject a, @CheckForNull JSONObject b, @Nonnull Set<String> skippedKeys) throws CompareException {
        if (a == null) {
            return b == null;
        }
        if (b == null) {
            return false;
        }
        for (String k : a.keySet()) {
            if (!skippedKeys.contains(k)) {
                try {
                    if (!equals(a.get(k), b.get(k))) {
                        return false;
                    }
                } catch (JSONException ex) {
                    throw new CompareException("Missing key: " + k);
                }
            }
        }
        return true;
    }

    public static boolean compare(@CheckForNull JSONObject a, @CheckForNull JSONObject b, @Nonnull String[] skippedKeys) throws CompareException {
        Set<String> keys = new HashSet<>();
        for (String k : skippedKeys) {
            keys.add(k);
        }

        return compare(a, b, keys);
    }

    public static boolean compare(@CheckForNull JSONObject a, @CheckForNull JSONObject b) throws CompareException {
        return compare(a, b, new HashSet<String>());
    }

    /**
     * checks if both inputs are comparable, checks based of the type of expected
     * <p>
     * Supports: int,double,float,boolean,String, JSONObject,JSONArray
     *
     * @param value
     * @param expected
     *
     * @return
     *
     * @throws CompareException throws exception if both of them mismatche, with a reason why it mismatched
     */
    public static boolean equals(@CheckForNull Object value, @CheckForNull Object expected) throws CompareException {
        return equals(value, expected, "");

    }

    public static boolean equals(@CheckForNull Object value, @CheckForNull Object expected, @Nonnull String path) throws CompareException {
        return equals(value, expected, path, ".");
    }

    public static boolean equals(@CheckForNull Object value, @CheckForNull Object expected, @Nonnull String path, String divider) throws CompareException {
        if (value == null) {
            if (expected == null) {
                return true;
            }
            throw new CompareException(path, null, expected);
        }
        if (expected == null) {
            throw new CompareException(path, value, null);
        }
        if (expected instanceof Integer) {
            if (!(value instanceof Integer)) {
                try {
                    value = Integer.parseInt(value.toString());
                } catch (NumberFormatException ex) {
                    try {
                        value = ((Double) Double.parseDouble(value.toString())).intValue();
                    } catch (NumberFormatException ex2) {
                        throw new CompareException("NumberFormatException: " + ex.getMessage());
                    }
                    //throw new CompareException("NumberFormatException: " + ex.getMessage());
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof Long) {
            if (!(value instanceof Long)) {
                try {
                    value = Long.parseLong(value.toString());
                } catch (NumberFormatException ex) {
                    //throw new CompareException("NumberFormatException: " + ex.getMessage());
                    try {
                        value = ((Double) Double.parseDouble(value.toString())).longValue();
                    } catch (NumberFormatException ex2) {
                        throw new CompareException("NumberFormatException: " + ex.getMessage());
                    }
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof Double) {
            if (!(value instanceof Double)) {
                try {
                    value = Double.parseDouble(value.toString());
                } catch (NumberFormatException ex) {
                    throw new CompareException("NumberFormatException: " + ex.getMessage());
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof BigDecimal) {
            if (!(value instanceof BigDecimal)) {
                try {
                    value = BigDecimal.valueOf(Double.parseDouble(value.toString()));
                } catch (NumberFormatException ex) {
                    throw new CompareException("NumberFormatException: " + ex.getMessage());
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof Float) {
            if (!(value instanceof Float)) {
                try {
                    value = Float.parseFloat(value.toString());
                } catch (NumberFormatException ex) {
                    throw new CompareException("NumberFormatException: " + ex.getMessage());
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof BigInteger) {

            if (!(value instanceof BigInteger)) {
                try {
                    value = BigInteger.valueOf(Long.parseLong(value.toString()));
                } catch (NumberFormatException ex) {
                    throw new CompareException("NumberFormatException: " + ex.getMessage());
                }
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof Boolean) {
            if (!(value instanceof Boolean)) {
                value = Boolean.parseBoolean(value.toString());
            }
            if (value.equals(expected)) {
                return true;
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof String) {
            if (value != null) {
                if (value.equals(expected)) {
                    return true;
                }


                if (String.valueOf(value).equals(expected)) {
                    return true;
                }
            }
            throw new CompareException(path, value, expected);
        }
        if (expected instanceof JSONObject) {
            JSONObject o2 = null;
            if (value instanceof JSONObject) {
                o2 = (JSONObject) value;
            } else if (value instanceof String) {
                o2 = new JSONObject(value);
            }
            if (o2 == null) {

                throw new CompareException(path, "value could not be converted to JSONObject");

            }
            JSONObject o1 = (JSONObject) expected;
            Set<String> keys1 = o1.keySet();
            Set<String> keys2 = o2.keySet();
            if (keys1.containsAll(keys2) && keys2.containsAll(keys1)) {
                for (String k : keys1) {

                    if (!equals(o1.get(k), o2.get(k), ((path.length() > 0) ? (path) : ("$")) + divider + k, divider)) {
                        return false;
                    }

                }
                return true;
            } else {
                LinkedList<String> missingin1 = new LinkedList<>();
                LinkedList<String> missingin2 = new LinkedList<>();
                for (String k : keys1) {
                    if (!keys2.contains(k)) {
                        missingin2.add(k);
                    }
                }
                for (String k : keys2) {
                    if (!keys1.contains(k)) {
                        missingin1.add(k);
                    }
                }
                throw new CompareException(path, "Missing keys: [" + String.join(",", missingin2) + "] extra keys: [" + String.join(",", missingin1) + "]");

            }

        }
        if (expected instanceof JSONArray) {
            JSONArray o2 = null;
            if (value instanceof JSONArray) {
                o2 = (JSONArray) value;
            } else if (value instanceof String) {
                o2 = new JSONArray(String.valueOf(value));
            }
            if (o2 == null) {

                throw new CompareException(path, "value could not be converted to JSONArray");
            }
            JSONArray o1 = (JSONArray) expected;
            if (o1.length() == o2.length()) {
                for (int i = 0; i < o1.length(); i++) {
                    if (!equals(o1.get(i), o2.get(i), (path.length() > 0) ? (path) : ("$") + "[" + i + "]")) {
                        return false;
                    }
                }

                return true;

            }

            throw new CompareException(path, "Mismatched length of JSONArray");
        }
        if (expected.equals(value)) {
            return true;
        }
        throw new CompareException(path, "No comparison known for " + expected.getClass());


    }

    public static Set<String> diff(JSONObject json1, JSONObject json2) {
        return diff(json1, json2, "");
    }

    public static Set<String> diff(JSONObject json1, JSONObject json2, String path) {
        Set<String> set = new HashSet<>();
        Set<String> keys = new HashSet<>();
        keys.addAll(json1.keySet());
        keys.addAll(json2.keySet());

        for (String name : keys) {
            try {
                equals(json1.opt(name), json2.opt(name), (path.length() == 0) ? (name) : (path + "." + name), "/");
            } catch (CompareException e) {
                set.add(e.path);
                //throw new RuntimeException(e);
                /*if (path.length()==0) {
                    set.add(name);
                }
                else {
                    set.add(path + "/" + name);
                }*/
            }
        }

        return set;
    }
}

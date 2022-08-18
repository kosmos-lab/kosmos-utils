package de.kosmos_lab.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONFunctions {
    final static Pattern jsonArrayPattern = Pattern.compile("(.*?)\\[(\\d*?)\\](.*)");
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("JSONFunctions");

    public static boolean hasEntries(JSONArray jsonArray, String[] keys) {
        HashSet<String> set = new HashSet<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                set.add(jsonArray.getString(i));
            }
        } catch (Exception ex) {

            return false;
        }
        for (String k : keys) {
            if (!set.contains(k)) {

                return false;

            }
        }

        return true;
    }

    public static boolean hasEntries(JSONObject json, String[] keys) {

        for (String k : keys) {
            if (!json.has(k)) {

                return false;
            }
        }
        return true;
    }

    public static Set<String> keys(JSONArray a, String path, String key) {
        Set<String> keys = new TreeSet<String>();
        String myPath = null;
        if (path == null) {
            myPath = "";
        } else {
            myPath = String.format("%s/", path);
        }

        for (int i = 0; i < a.length(); i++) {
            String keyName = String.format("%s[%d]", path,  i);
            keyName = keyName.replace("/[","[");
            logger.info("adding key in keys(arr) {}",keyName);
            keys.add(keyName);

            Object o = a.get(i);
            if ( o instanceof JSONArray || o instanceof  JSONObject) {
                parseSubSet(o, myPath, String.format("[%d]",i), keys);
            }

        }

        return keys;
    }
    private static void parseSubSet(Object o,String myPath,String key,Set<String> keys) {
        String oPath = String.format("%s%s", myPath, key);
        if (o instanceof JSONObject) {
            logger.info("adding key in parseSubSet {}",oPath);
            keys.add(oPath);
            keys.addAll(keys((JSONObject) o, oPath));
        } else if (o instanceof JSONArray) {
            keys.addAll(keys((JSONArray) o, oPath, key));

        } else {
            logger.info("adding key in parseSubSet else {}",oPath);
            keys.add(oPath);
        }

    }
    public static Set<String> keys(JSONObject json, String path) {
        Set<String> keys = new TreeSet<String>();
        String myPath = null;
        if (path == null) {
            myPath = "";
        } else {
            myPath = String.format("%s/", path);
        }
        myPath = myPath.replace("/[","[");

        for (String key : json.keySet()) {
            Object o = json.get(key);
            parseSubSet(o,myPath,key,keys);
        }

        return keys;
    }

    public static Object get(@CheckForNull JSONObject json, @CheckForNull String path) {
        if (path == null || json == null) {
            return false;
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        int idx = path.indexOf("/");
        if (idx == -1) {
            Matcher m = jsonArrayPattern.matcher(path);
            if (m.matches()) {
                JSONArray a = json.optJSONArray(m.group(1));
                if (a != null) {
                    try {
                        return a.get(Integer.parseInt(m.group(2)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                try {
                    return json.get(path);
                } catch (JSONException e) {

                }
            }
            return null;
        }
        String p = path.substring(0, idx);
        Matcher m = jsonArrayPattern.matcher(path);
        if (m.matches()) {

            JSONArray a = json.optJSONArray(m.group(1));
            if (a != null) {
                try {

                    Object o = a.get(Integer.parseInt(m.group(2)));
                    if (o instanceof JSONObject) {
                        if (m.groupCount() > 2) {

                            return get((JSONObject) o, m.group(3));
                        }
                        return get((JSONObject) o, null);
                    }
                    return o;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        if (json.has(p)) {
            return get(json.getJSONObject(p), path.substring(idx + 1));
        }
        return null;
    }

    public static boolean has(@CheckForNull JSONObject json, @CheckForNull String path) {
        if (path == null || json == null) {
            return false;
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        int idx = path.indexOf("/");
        if (idx == -1) {
            return json.has(path);
        }
        String p = path.substring(0, idx);
        if (json.has(p)) {
            return has(json.getJSONObject(p), path.substring(idx + 1));
        }
        return false;
    }


}

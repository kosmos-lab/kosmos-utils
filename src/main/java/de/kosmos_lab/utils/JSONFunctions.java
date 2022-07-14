package de.kosmos_lab.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.CheckForNull;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONFunctions {
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

    final static Pattern jsonArrayPattern = Pattern.compile("(.*?)\\[(\\d*?)\\](.*)");

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
            if ( m.matches()) {
                JSONArray a = json.optJSONArray(m.group(1));
                if ( a != null ) {
                    try {
                        return a.get(Integer.parseInt(m.group(2)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            else {
                try {
                    return json.get(path);
                } catch (JSONException e) {

                }
            }
            return null;
        }
        String p = path.substring(0, idx);
        Matcher m = jsonArrayPattern.matcher(path);
        if ( m.matches()) {

            JSONArray a = json.optJSONArray(m.group(1));
            if ( a != null ) {
                try {

                    Object o = a.get(Integer.parseInt(m.group(2)));
                    if ( o instanceof JSONObject) {
                        if ( m.groupCount()>2) {

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

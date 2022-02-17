package de.kosmos_lab.utils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.CheckForNull;

public class JSONFunctions {
    public static Object get(@CheckForNull JSONObject json, @CheckForNull String path) {
        if (path == null || json == null) {
            return false;
        }
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        int idx = path.indexOf("/");
        if (idx == -1) {
            try {
                return json.get(path);
            } catch (JSONException e) {
            
            }
            return null;
        }
        String p = path.substring(0, idx);
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

package de.kosmos_lab.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public class JSONPatch {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("JSONPatch");
    
    private static void apply(JSONObject from, JSONObject patch, JSONObject to, int fromIndex) {
        String path = patch.getString("path");
        int idx = path.indexOf('/', fromIndex);
        if (idx != -1) {
            String key = path.substring(0, idx);
            //logger.trace("key: {}",key);
            JSONObject inner = to.optJSONObject(key);
            if (inner == null) {
                inner = new JSONObject();
                to.put(key, inner);
            }
            JSONObject fromInner = from.optJSONObject(key);
            if (fromInner == null) {
                fromInner = new JSONObject();
                
            }
            apply(fromInner, patch, inner, idx + 1);
        } else {
            String op = patch.getString("op");
            String key = path.substring(fromIndex);
            
            if (op.equalsIgnoreCase("remove")) {
                to.remove(key);
                
            } else if (op.equalsIgnoreCase("add")) {
                to.put(key, patch.get("value"));
            } else if (op.equalsIgnoreCase("replace")) {
                to.put(key, patch.get("value"));
            }
        }
        
    }
    
    /**
     * applies a very simple json-patch based on RFC 6902 https://tools.ietf.org/html/rfc6902
     * <p>
     * ATTENTION:
     * ONLY! works for add/update/remove and does not work for JSONArrays yet
     *
     * @param from  the original jsonobject
     * @param patch the patch to be applied
     * @return
     */
    public static JSONObject apply(JSONObject from, JSONArray patch) {
        JSONObject to = new JSONObject(from.toMap());
        for (int i = 0; i < patch.length(); i++) {
            apply(from, patch.getJSONObject(i), to, 1);
        }
        return to;
        
        
    }
    
    /**
     * create a very simple json-patch based on RFC 6902 https://tools.ietf.org/html/rfc6902
     * <p>
     * ATTENTION:
     * ONLY! works for add/update/remove and does not work for JSONArrays yet
     *
     * @param from the original jsonobject
     * @param to   the jsonobject we want to get to
     * @return the patch as a JSONArray
     */
    public static JSONArray getPatch(JSONObject from, JSONObject to) {
        return getPatch(from, to, "");
    }
    
    /**
     * create a very simple json-patch based on RFC 6902 https://tools.ietf.org/html/rfc6902
     * <p>
     * ATTENTION:
     * ONLY! works for add/update/remove and does not work for JSONArrays yet
     *
     * @param from the original jsonobject
     * @param to   the jsonobject we want to get to
     * @param path the initial path, default = ""
     * @return the patch as a JSONArray
     */
    private static JSONArray getPatch(JSONObject from, JSONObject to, String path) {
        JSONArray patch = new JSONArray();
        for (String key : to.keySet()) {
            if (!from.has(key)) {
                Object o = to.get(key);
                if (o instanceof Long || o instanceof Integer || o instanceof String || o instanceof Double || o instanceof Boolean || o instanceof JSONObject) {
                    JSONObject op = new JSONObject();
                    
                    op.put("op", "add");
                    op.put("path", path + "/" + key);
                    op.put("value", o);
                    patch.put(op);
                    
                } else {
                    System.err.println("WHAT TO DO WITH " + o.getClass());
                }
            }
        }
        for (String key : from.keySet()) {
            if (!to.has(key)) {
                JSONObject op = new JSONObject();
                op.put("op", "remove");
                op.put("path", path + "/" + key);
                patch.put(op);
            } else {
                Object objF = from.opt(key);
                Object objT = to.opt(key);
                if (objF == null) {
                    if (objT instanceof JSONObject) {
                        objF = new JSONObject();
                    }
                }
                
                if (objF instanceof JSONObject && objT instanceof JSONObject) {
                    JSONArray patches = getPatch((JSONObject) objF, (JSONObject) objT, key);
                    for (int j = 0; j < patches.length(); j++) {
                        patch.put(patches.getJSONObject(j));
                    }
                    
                } else if (
                        objT instanceof Long ||
                                objT instanceof Boolean ||
                                objT instanceof String ||
                                objT instanceof Integer ||
                                objT instanceof Double || objT instanceof JSONObject
                ) {
                    if (objF == null) {
                        JSONObject op = new JSONObject();
                        op.put("op", "add");
                        op.put("path", path + "/" + key);
                        op.put("value", objT);
                        patch.put(op);
                    } else if (!objF.equals(objT)) {
                        if (!JSONObject.wrap(objT).toString().equals(JSONObject.wrap(objF).toString())) {
                            JSONObject op = new JSONObject();
                            op.put("op", "replace");
                            op.put("path", path + "/" + key);
                            op.put("value", objT);
                            
                            patch.put(op);
                        }
                    }
                }
            }
        }
        return patch;
    }
    
    
}

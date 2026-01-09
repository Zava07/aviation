package com.itu.aviation.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class SearchUtils {

    public static boolean matches(Object obj, Map<String, String> filters) {
        if (filters == null || filters.isEmpty()) return true;
        // global text search
        String q = filters.get("q");
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
                String name = pd.getName();
                if ("class".equals(name)) continue;
                Object value = null;
                try { value = pd.getReadMethod().invoke(obj); } catch (Exception e) { value = null; }
                String sval = value == null ? "" : String.valueOf(value);
                if (q != null && !q.isBlank()) {
                    if (sval.toLowerCase().contains(q.toLowerCase())) return true;
                }
            }
            // specific field filters: each entry must be contained in corresponding property string
            for (Map.Entry<String, String> e : filters.entrySet()) {
                String key = e.getKey();
                String v = e.getValue();
                if (v == null || v.isBlank() || "q".equals(key)) continue;
                boolean matched = false;
                for (PropertyDescriptor pd : Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors()) {
                    if (pd.getName().equalsIgnoreCase(key)) {
                        Object value = null;
                        try { value = pd.getReadMethod().invoke(obj); } catch (Exception ex) { value = null; }
                        String sval = value == null ? "" : String.valueOf(value);
                        if (sval.toLowerCase().contains(v.toLowerCase())) matched = true;
                        break;
                    }
                }
                if (!matched) return false;
            }
        } catch (IntrospectionException ie) {
            return true;
        }
        // if q was present but not matched earlier, allow only if some field matched above; otherwise all specific filters passed
        if (filters.containsKey("q") && filters.get("q") != null && !filters.get("q").isBlank()) {
            // if q present, we already returned true on match; otherwise no match
            return false;
        }
        return true;
    }
}

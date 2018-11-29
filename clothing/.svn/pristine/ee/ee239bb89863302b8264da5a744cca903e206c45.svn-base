package com.pyjh.clothing.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.pyjh.clothing.util.Tools;

public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    /*
     * 返回Timestamp，Date，Object
     *
     */
    @Override
    public Object get(Object key) {
        Object obj = null;
        Object value = map.get(key);

        if (value instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = value;
        }
        if (value instanceof Timestamp) {
            obj = Tools.timestamp2Str((Timestamp) value);
        }
        if (value instanceof Date) {
            obj = Tools.sqlDate2Str((Date) value);
        }

        return obj;
    }

    public String getString(Object key) {
        if ("USER_ID".equals(key)) {
            return get(key) + "";
        }
        return (String) get(key);
    }

    public Long getLong(Object key) {
        return Long.valueOf(String.valueOf(get(key)));
    }

    public Integer getInteger(Object key) {
        Object value = get(key);
        if (value == null || "".equals(value)) {
            return 0;
        }

        return Integer.valueOf(String.valueOf(value));
    }

    public Double getDouble(Object key) {
        Object value = get(key);
        if (value == null || "".equals(value)) {
            return 0.0;
        }

        return Double.valueOf(String.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        if (value instanceof Timestamp) {
            String stringValue = Tools.timestamp2Str((Timestamp) value);
            return map.put(key, stringValue);
        }
        if (value instanceof Date) {
            String stringValue = Tools.sqlDate2Str((Date) value);
            return map.put(key, stringValue);
        }

        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        map.putAll(t);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    private static final String AUDIT_STATUS = "audit_status";

    private static final String REVIEW_STATUS = "reaudit_status";

    /**
     * 为了审核和复审专门加的代码
     *
     * @return
     */
    // public boolean getCanAudit(){
    // if(!map.containsKey(AUDIT_STATUS)){
    // return false;
    // }
    // int value = getInteger(AUDIT_STATUS);
    //
    // return value == 0 || value == StoreOrderEnum.STATUS_AUDIT_NOT.getKey() ;
    // }
    //
    // public boolean getReview(){
    // if(!map.containsKey(AUDIT_STATUS) || !map.containsKey(REVIEW_STATUS)){
    // return false;
    // }
    //
    // int value = getInteger(AUDIT_STATUS);
    // if(value != StoreOrderEnum.STATUS_AUDIT.getKey()){
    // return false;
    // }
    //
    // int review = getInteger(REVIEW_STATUS);
    // return review == 0 || review == StoreOrderEnum.STATUS_REVIEW_WAIT.getKey() ;
    // }

}

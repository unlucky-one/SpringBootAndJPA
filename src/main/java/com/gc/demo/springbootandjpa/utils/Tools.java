package com.gc.demo.springbootandjpa.utils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/15
 */
public class Tools {

    public final static int ERROR = -1;
    public final static int SUCCESSFUL = 200;

    public static Map<String, Object> getResult(int status, String message, Object data, Map<String, Object> map) {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("status", status);
        mapResult.put("message", message);
        if (data != null && !"".equals(data))
            if (data instanceof org.springframework.data.domain.Page) {
                org.springframework.data.domain.Page page = (org.springframework.data.domain.Page) data;
                return getSuccessResult(page);
            } else
                mapResult.put("data", data);
        if (map != null && map.size() > 0)
            mapResult.putAll(map);
        return mapResult;
    }

    public static Map<String, Object> getSuccessResult(Object obj) {
        if (obj instanceof MyAppResult)
            return getResult((MyAppResult) obj);
        return getResult(SUCCESSFUL, null, obj, null);
    }

    public static Map<String, Object> getResult(MyAppResult result) {
        if (result != null)
            return getResult(result.getStatus(), result.getMessage(), result.getData(), null);
        else
            return getResult(SUCCESSFUL, "result data is null!", null, null);

    }

    public static Map<String, Object> getSuccessResult(Object obj, Map<String, Object> map) {
        return getResult(SUCCESSFUL, null, obj, map);
    }

    public static Map<String, Object> getSuccessResult(org.springframework.data.domain.Page page) {
        if (page == null)
            return getResult(ERROR, "result data is null!", null, null);

        List data = new ArrayList();
        if (page.hasContent()) {
            Iterator iterator = page.iterator();
            while (iterator.hasNext())
                data.add(iterator.next());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("page", page.getNumber());
        map.put("pages", page.getTotalPages());
        map.put("total", page.getTotalElements());
        return getResult(SUCCESSFUL, null, data, map);
    }

    public static Map<String, Object> getFaildResult(int code, String msg) {
        return getResult(code, msg, null, null);
    }

    public static MyAppResult getSuccessData() {
        MyAppResult result = new MyAppResult();
        result.setStatus(SUCCESSFUL);
        return result;
    }

    public static MyAppResult getSuccessData(Object obj) {
        MyAppResult result = new MyAppResult();
        result.setStatus(SUCCESSFUL);
        result.setData(obj);
        return result;
    }

    public static MyAppResult getFaildData(int status, String message) {
        MyAppResult result = new MyAppResult();
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    public static MyAppResult getFaildData(String message) {
        MyAppResult result = new MyAppResult();
        result.setStatus(ERROR);
        result.setMessage(message);
        return result;
    }
}

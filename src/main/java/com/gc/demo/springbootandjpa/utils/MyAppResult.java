package com.gc.demo.springbootandjpa.utils;

public class MyAppResult {
    private int status;
    private Object data;
    private String message;

    public MyAppResult(int status, Object data, String msg) {
        this.status = status;
        this.data = data;
        this.message = msg;
    }

    public MyAppResult() {
    }

    public int getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}

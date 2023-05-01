package com.invoice.util;

import java.util.HashMap;
import java.util.Map;

public class HTTPResponse
{


    private Object  data;
    private String message;
    private short status;

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public HTTPResponse() {

    }
    public HTTPResponse(Object data, String message, short status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
    public Map<String,Object> getHttpResponse(Object data, String message, short status)
    {
        Map<String,Object> map = new HashMap<>();
        map.put("data",data);
        map.put("message",message);
        map.put("status",status);
        return map;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
package com.uploader;

import org.json.JSONObject;

public class VimeoResponse {
    private JSONObject json;
    private int statusCode;

    public VimeoResponse(JSONObject json, int statusCode) {
        this.json = json;
        this.statusCode = statusCode;
    }

    public JSONObject getJson() {
        return json;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String toString() {

        StringBuilder buff = new StringBuilder();
        String str = new String();

        try {
            buff.append("HTTP Status Code: \n").append(getStatusCode()).append("\nJson: \n").append(getJson().toString(2)).toString();
            str = buff.toString();
        }   catch(Exception e) {
            System.out.println("Err in output HTTP Status Code");
        }

        return str;
    }
}

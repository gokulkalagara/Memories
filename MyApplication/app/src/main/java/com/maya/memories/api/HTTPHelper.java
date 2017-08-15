package com.maya.memories.api;

import android.content.SharedPreferences;
import android.util.Log;

import com.maya.memories.utils.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;




public class HTTPHelper {

    public int response_code;
    boolean useHeader;
    SharedPreferences prefs;
    public String url,data;
    public HTTPHelper() {
        this.response_code = -1;
        this.useHeader = true;
    }
    public HTTPHelper(SharedPreferences prefs){
        this.response_code = -1;
        this.useHeader = true;
        this.prefs = prefs;
    }
    public HTTPHelper(boolean useHeader) {
        this.response_code = -1;
        this.useHeader = useHeader;
    }

    /* do a post request */
    public String post(String url, String data) throws Exception {
        this.url = url;
        this.data = data;
        Log.d("[Request]", url+"?"+data);
        byte[] postData = data.getBytes(Charset.forName("UTF-8"));
        URL urlobj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlobj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        if (useHeader) conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setUseCaches(false);
        conn.getOutputStream().write(postData);
        this.response_code = conn.getResponseCode();
        Reader r = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        int ch = r.read();
        String response = "";
        while (ch != -1) {
            response += (char)ch;
            ch = r.read();
        }
        this.response_code = conn.getResponseCode();
        return response;
    }
    public String get(String url, String data) throws Exception {
        this.url = url;
        this.data = data;
        Logger.d("[Request]", url+"?"+data);
        URL urlobj = new URL(url+"?"+data);
        HttpURLConnection conn = (HttpURLConnection) urlobj.openConnection();
        conn.setRequestMethod("GET");
        if (useHeader) conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setUseCaches(false);
        this.response_code = conn.getResponseCode();
        Reader r = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        int ch = r.read();
        String response = "";
        while (ch != -1) {
            response += (char)ch;
            ch = r.read();
        }
        this.response_code = conn.getResponseCode();
        return response;
    }
}
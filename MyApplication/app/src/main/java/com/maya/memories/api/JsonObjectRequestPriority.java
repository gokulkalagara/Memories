package com.maya.memories.api;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Gokul on 16-Jun-16.
 */
public class JsonObjectRequestPriority extends JsonObjectRequest{
    private Priority mPriority =Priority.LOW;
    public JsonObjectRequestPriority(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JsonObjectRequestPriority(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority){
        mPriority = priority;
    }
}
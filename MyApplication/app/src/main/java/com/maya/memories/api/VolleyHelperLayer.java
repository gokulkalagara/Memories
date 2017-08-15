package com.maya.memories.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.applications.MemoriesApplication;
import com.maya.memories.constants.Constants;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONObject;



/**
 * Created by Gokul on 14-Jun-16.
 */
public class VolleyHelperLayer {
    public void startHandlerVolley(final Context context, String URL, JSONObject input, Response.Listener<JSONObject> listener, final ProgressDialog progressDialog, Request.Priority priority)
    {
        try {
            String tag_json_obj = "json_obj_req";
            Logger.d("[Request]", URL);
            Logger.d("[INPUT]", input.toString());
            JsonObjectRequestPriority jsonReq = new JsonObjectRequestPriority(Request.Method.POST,
                    URL,
                    input,
                    listener,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Utility.showToast(context, "network_error", Constants.CONNECTION_ERROR, true);
                            if (progressDialog != null&&progressDialog.isShowing()) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            //Adding request to request QUEUE
            jsonReq.setPriority(priority);
            jsonReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MemoriesApplication.getInstance().addToRequestQueue(jsonReq, tag_json_obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void startHandlerVolley(String URL, JSONObject input, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Request.Priority priority){
        try {
            String tag_json_obj = "json_obj_req";
            Logger.d("[Request]", URL);
            if(input!=null)
            Logger.d("[INPUT]", input.toString());
            JsonObjectRequestPriority jsonObjectRequest = new JsonObjectRequestPriority(Request.Method.POST, URL, input, listener, errorListener);
            jsonObjectRequest.setPriority(priority);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MemoriesApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //for Custom request type POST,GET,PUT,DELETE
    public void startHandlerVolley(final Context context, String URL, JSONObject input, Response.Listener<JSONObject> listener, final ProgressDialog progressDialog, Request.Priority priority, int requestType) {
        try {
            String tag_json_obj = "json_obj_req";
            Logger.d("[Request]", URL);
            if (input != null) {
                Logger.d("[INPUT]", input.toString());
            }
            JsonObjectRequestPriority jsonReq = new JsonObjectRequestPriority(requestType,
                    URL,
                    input,
                    listener,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Utility.showToast(context, "network_error", Constants.CONNECTION_ERROR, true);
                            if (progressDialog != null) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            //Adding request to request QUEUE
            jsonReq.setPriority(priority);
            jsonReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MemoriesApplication.getInstance().addToRequestQueue(jsonReq, tag_json_obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void startHandlerVolley(String URL, JSONObject input, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Request.Priority priority, int requestType){
        try {
            String tag_json_obj = "json_obj_req";
            Logger.d("[Request]", URL);
            if (input != null) {
                Logger.d("[INPUT]", input.toString());
            }
            JsonObjectRequestPriority jsonObjectRequest = new JsonObjectRequestPriority(requestType, URL, input, listener, errorListener);
            jsonObjectRequest.setPriority(priority);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MemoriesApplication.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
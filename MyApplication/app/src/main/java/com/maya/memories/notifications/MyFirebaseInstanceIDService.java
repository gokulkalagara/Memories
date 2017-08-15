package com.maya.memories.notifications;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Maya on 11/30/2016.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(Constants.USER_FCM_ID, "Refreshed token: " + refreshedToken);
        if (refreshedToken!=null&&refreshedToken.length()>10)
        {
            try
            {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, 0);
                Utility.set(sharedPreferences, Constants.USER_FCM_ID, refreshedToken);
                if(sharedPreferences.contains(Constants.USER_UUID))
                {
                    addFcm(sharedPreferences);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Logger.d(TAG,e.getMessage());
            }

        }
    }
    private void addFcm(SharedPreferences PREFS)
    {
        String URL = Constants.URL_ADD_FCM;
        JSONObject input = new JSONObject();
        try
        {
            input.put(Constants.USER_UUID,PREFS.getString(Constants.USER_UUID,""));
            input.put(Constants.USER_FCM_ID,PREFS.getString(Constants.USER_FCM_ID,""));
            input.put("platform","A");
            input.put(Constants.USER_IMEI,PREFS.getString(Constants.USER_IMEI,""));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            boolean success;
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Logger.d("[response]",jsonObject.toString());

                success = Utility.isSuccessful(jsonObject.toString());

                try {
                    if (success)
                    {

                    }
                    else
                    {

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]","Unable to contact server");

            }
        };
        volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);

    }

}

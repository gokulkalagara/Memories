package com.maya.memories.applications;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.maya.memories.api.LruBitmapCache;

/**
 * Created by home on 7/26/2017.
 */

public class MemoriesApplication extends Application {
    public static final String TAG = MemoriesApplication.class.getSimpleName();
    private static MemoriesApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static boolean ChatActivityFlag=false;

    public static boolean MainActivityFlag=false;

    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        mInstance = this;
    }

    public static synchronized MemoriesApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        try {
            if (mRequestQueue == null) {
                  mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }
        }
        catch (Exception e)
        {

        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        StringRequest request;
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}

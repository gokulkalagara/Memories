package com.maya.memories.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.adapters.ClassmatePhotoAdapter;
import com.maya.memories.adapters.TrendingVideosAdapter;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.models.ClassItem;
import com.maya.memories.models.VideoItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrendingVideosActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageView imgPerson;
    ProgressBar progressBar;
    SharedPreferences PREFS;
    TrendingVideosActivity trendingVideosActivity;
    List<VideoItem> list;
    SwipeRefreshLayout refresher;
    FrameLayout content;
    public int openFlag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        trendingVideosActivity=this;
        setContentView(R.layout.activity_trending_videos);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        imgPerson=(ImageView) findViewById(R.id.imgPerson);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        refresher=(SwipeRefreshLayout)findViewById(R.id.refresher);
        content=(FrameLayout)findViewById(R.id.content);
        if(Utility.isNetworkAvailable(trendingVideosActivity))
        {
            progressBar.setVisibility(View.VISIBLE);
            fetchTrendingVideos();
        }
        else
        {
            Utility.showToast(trendingVideosActivity,"Error", Constants.CONNECTION_ERROR,false);
        }


        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkAvailable(trendingVideosActivity))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    fetchTrendingVideos();
                    refresher.setRefreshing(false);
                }
                else
                {
                    refresher.setRefreshing(false);
                }
            }
        });

    }



    public void fetchTrendingVideos()
    {
        String URL = Constants.CLASSMATE_VIDEOS;
        JSONObject input = new JSONObject();
        try {
            input.put("u_uid",PREFS.getString(Constants.USER_UUID,""));//PREFS.getString(Constants.USER_UUID,"")
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
                progressBar.setVisibility(View.GONE);
                try {
                    if (success)
                    {
                        initializeVideos(jsonObject.getString(Constants.DATA));
                        initViews();
                    }
                    else
                    {
                        Utility.showToast(trendingVideosActivity,"error","",true,jsonObject.toString());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.d("[response]","Unable to contact server");
                Utility.showToast(trendingVideosActivity,"error",Constants.CONNECTION_ERROR,true);
                progressBar.setVisibility(View.GONE);

            }
        };
        if (Utility.isNetworkAvailable(trendingVideosActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(trendingVideosActivity,"Error",Constants.CONNECTION_ERROR,true);
    }



    public void initializeVideos(String data)
    {
        list=new ArrayList<VideoItem>();
        try
        {
            JSONArray jsonArray=new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                list.add(new VideoItem(jsonObject.getString("roll_number"),Constants.URL_VIDEOS+jsonObject.getString("video_url"),Constants.URL_VIDEOS+jsonObject.getString("photo_url")));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initViews()
    {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(layoutManager);
        if(list!=null)
        {
            RecyclerView.Adapter adapter = new TrendingVideosAdapter(list, trendingVideosActivity);
            recyclerView.setAdapter(adapter);
        }
    }

}

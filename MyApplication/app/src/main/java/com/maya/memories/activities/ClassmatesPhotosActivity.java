package com.maya.memories.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.fragments.ImageViewFragment;
import com.maya.memories.interfaces.InterfaceToClassmatesPhotoActivity;
import com.maya.memories.models.ClassItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassmatesPhotosActivity extends AppCompatActivity implements InterfaceToClassmatesPhotoActivity {

    ClassItem classItem;
    RecyclerView recyclerView;
    ImageView imgPerson;
    ProgressBar progressBar;
    SharedPreferences PREFS;
    ClassmatesPhotosActivity classmatesPhotosActivity;
    List<String> list;
    SwipeRefreshLayout refresher;
    FrameLayout content;
    public int openFlag=0;
    InterfaceToClassmatesPhotoActivity interfaceToClassmatesPhotoActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        classmatesPhotosActivity=this;
        interfaceToClassmatesPhotoActivity=(InterfaceToClassmatesPhotoActivity)classmatesPhotosActivity;
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        classItem=(ClassItem) getIntent().getSerializableExtra("ClassItem");
        setContentView(R.layout.activity_classmates_photos);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        imgPerson=(ImageView) findViewById(R.id.imgPerson);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        refresher=(SwipeRefreshLayout)findViewById(R.id.refresher);
        content=(FrameLayout)findViewById(R.id.content);
        Picasso
                .with(this)
                .load(classItem.profilePic)
                .into((ImageView) imgPerson);


        if(Utility.isNetworkAvailable(classmatesPhotosActivity))
        {
            progressBar.setVisibility(View.VISIBLE);
            fetchClassmatePhotos();
        }
        else
        {
            Utility.showToast(classmatesPhotosActivity,"Error",Constants.CONNECTION_ERROR,false);
        }


        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkAvailable(classmatesPhotosActivity))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    fetchClassmatePhotos();
                    refresher.setRefreshing(false);
                }
                else
                {
                    refresher.setRefreshing(false);
                }
            }
        });
    }







    public void fetchClassmatePhotos()
    {
        String URL = Constants.CLASSMATE_PHOTO;
        JSONObject input = new JSONObject();
        try {
            input.put("roll_number",classItem.rollNo);//PREFS.getString(Constants.USER_UUID,"")
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
                        initializeImages(jsonObject.getString(Constants.DATA));
                        initViews();
                    }
                    else
                    {
                        Utility.showToast(classmatesPhotosActivity,"error","",true,jsonObject.toString());
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
                Utility.showToast(classmatesPhotosActivity,"error",Constants.CONNECTION_ERROR,true);
                progressBar.setVisibility(View.GONE);

            }
        };
        if (Utility.isNetworkAvailable(classmatesPhotosActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(classmatesPhotosActivity,"Error",Constants.CONNECTION_ERROR,true);
    }



    private void initializeImages(String data)
    {
        list=new ArrayList<String>();
        try
        {
            JSONArray jsonArray=new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                list.add(Constants.URL_IMAGE+jsonObject.getString("photourl"));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initViews(){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(layoutManager);
        if(list!=null)
        {
            RecyclerView.Adapter adapter = new ClassmatePhotoAdapter(list, classmatesPhotosActivity, interfaceToClassmatesPhotoActivity);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void addImageFrame(Bitmap bitmap)
    {
        openFlag=1;
        content.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ImageViewFragment.newInstance(null,null,bitmap,interfaceToClassmatesPhotoActivity)).commit();

    }

    @Override
    public void onClose()
    {
        openFlag=0;
        content.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed()
    {
        if(openFlag==0)
        {
            super.onBackPressed();
        }
        else
        {
            openFlag=0;
            content.setVisibility(View.GONE);
        }
    }
}

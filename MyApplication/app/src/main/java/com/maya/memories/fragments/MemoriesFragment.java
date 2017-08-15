package com.maya.memories.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.activities.TrendingVideosActivity;
import com.maya.memories.activities.UserListActivity;
import com.maya.memories.activities.VideoViewActivity;
import com.maya.memories.adapters.MemoriesItemsAdapter;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.models.ClassItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MemoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoriesFragment newInstance(String param1, String param2) {
        MemoriesFragment fragment = new MemoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    List<ClassItem> list;
    SwipeRefreshLayout refresher;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ProgressBar progressBar;
    SharedPreferences PREFS;
    Activity memoriesActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_memories, container, false);
        memoriesActivity=getActivity();
        PREFS=getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        refresher=(SwipeRefreshLayout)v.findViewById(R.id.refresher);
        recyclerView=(RecyclerView) v.findViewById(R.id.recyclerView);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Utility.isNetworkAvailable(getActivity()))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    fetchClassmates();
                    refresher.setRefreshing(false);
                }
                else
                {
                    refresher.setRefreshing(false);
                }
            }
        });



        fab=(FloatingActionButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Utility.isNetworkAvailable(getActivity()))
                {
                    Intent intent = new Intent(getActivity(), TrendingVideosActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Utility.showToast(getActivity(),"Error",Constants.CONNECTION_ERROR,false);
                }
            }
        });


        if(PREFS.contains(Constants.CLASSMATES_DATA))
        {
            initializeImages();
            initViews();
        }
        else
        {
            if(Utility.isNetworkAvailable(getActivity()))
            {
                progressBar.setVisibility(View.VISIBLE);
                fetchClassmates();
            }
            else
            {
                Utility.showToast(getActivity(),"Error",Constants.CONNECTION_ERROR,false);
            }


        }

        return v;
    }



    private void initializeImages()
    {
        Logger.d(Constants.CLASSMATES_DATA,PREFS.getString(Constants.CLASSMATES_DATA,""));
        list=new ArrayList<ClassItem>();
        try
        {
            JSONArray jsonArray=new JSONArray(PREFS.getString(Constants.CLASSMATES_DATA,""));
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                list.add(new ClassItem(jsonObject.getString("roll_number"),Constants.URL_IMAGE+jsonObject.getString("profile_pic"),7,2));
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
            RecyclerView.Adapter adapter = new MemoriesItemsAdapter(list, getActivity());
            recyclerView.setAdapter(adapter);
        }
    }


    public void fetchClassmates()
    {
        String URL = Constants.URL_GET_CLASSMATES;
        JSONObject input = new JSONObject();
        try {
            input.put(Constants.USER_UUID,PREFS.getString(Constants.USER_UUID,""));//PREFS.getString(Constants.USER_UUID,"")
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
                        Utility.set(PREFS,Constants.CLASSMATES_DATA,jsonObject.getString(Constants.DATA));
                        initializeImages();
                        initViews();
                    }
                    else
                    {
                        Utility.showToast(memoriesActivity,"error","",true,jsonObject.toString());
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
                Utility.showToast(memoriesActivity,"error",Constants.CONNECTION_ERROR,true);
                progressBar.setVisibility(View.GONE);

            }
        };
        if (Utility.isNetworkAvailable(memoriesActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(memoriesActivity,"Error",Constants.CONNECTION_ERROR,true);
    }

}

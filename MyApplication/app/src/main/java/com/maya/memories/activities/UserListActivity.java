package com.maya.memories.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.adapters.UserListAdapter;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.interfaces.InterfaceToUserListActivity;
import com.maya.memories.models.UserItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements InterfaceToUserListActivity{

    UserListActivity userListActivity;
    SwipeRefreshLayout refresher;
    RecyclerView recyclerView;
    SharedPreferences PREFS;
    List<UserItem> list;
    InterfaceToUserListActivity interfaceToUserListActivity;
    UserListAdapter userListAdapter;
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userListActivity=this;
        interfaceToUserListActivity=(InterfaceToUserListActivity)this;
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_user_list);
        refresher=(SwipeRefreshLayout)findViewById(R.id.refresher);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        etSearch=(EditText) findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userListAdapter!=null)
                {
                    userListAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Utility.hideKeyboard(userListActivity);
                fetchUsers();
                refresher.setRefreshing(false);
            }
        });

        fetchUsers();
    }



    private void fetchUsers()
    {
        String URL = Constants.URL_GET_USERS;
        JSONObject input = new JSONObject();
        try {
            input.put(Constants.USER_UUID,PREFS.getString(Constants.USER_UUID,""));//PREFS.getString(Constants.USER_UUID,"")
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final ProgressDialog progressDialog = Utility.generateProgressDialog(userListActivity);
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            boolean success;
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Logger.d("[response]",jsonObject.toString());
                success = Utility.isSuccessful(jsonObject.toString());
                if(progressDialog!=null&&progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                try {
                    if (success)
                    {
                        list=new ArrayList<UserItem>();

                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++)
                        {
                         JSONObject jsonObject1=jsonArray.getJSONObject(i);
                         list.add(new UserItem(
                                 jsonObject1.getString(Constants.USER_UUID),
                                 jsonObject1.getString(Constants.FIRST_NAME),
                                 jsonObject1.getString(Constants.LAST_NAME),
                                 jsonObject1.getString(Constants.USER_EMAIL),
                                 jsonObject1.getString(Constants.LOGIN_ID),
                                 jsonObject1.getString(Constants.USER_PHOTO_URL)

                                 ));


                        }


                        if(list.size()>0)
                        {
                            userListAdapter=new UserListAdapter(list,userListActivity,interfaceToUserListActivity);
                            recyclerView.setAdapter(userListAdapter);
                        }





                    }
                    else
                    {
                        Utility.showToast(userListActivity,"error","",true,jsonObject.toString());
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
                Utility.showToast(userListActivity,"error",Constants.CONNECTION_ERROR,true);
                if(progressDialog!=null&&progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

            }
        };
        if (Utility.isNetworkAvailable(userListActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(userListActivity,"Error",Constants.CONNECTION_ERROR,true);


    }

    @Override
    public void clickUser(UserItem userItem)
    {
        Intent intent=new Intent(userListActivity,ChatActivity.class);
        intent.putExtra("type","1");
        intent.putExtra("UserItem",userItem);
        startActivity(intent);

    }
}

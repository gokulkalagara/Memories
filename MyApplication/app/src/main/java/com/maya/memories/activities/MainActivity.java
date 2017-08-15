package com.maya.memories.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.applications.MemoriesApplication;
import com.maya.memories.constants.Constants;
import com.maya.memories.databases.DataInOut;
import com.maya.memories.dialogs.WelcomeUserDialog;
import com.maya.memories.fragments.ChatBoxFragment;
import com.maya.memories.fragments.MemoriesFragment;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private TextView tvUserName,tvUserEmail;
    ImageView profile_pic;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, MemoriesFragment.newInstance(null,null)).commit();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, ChatBoxFragment.newInstance(null,null)).commit();
                    return true;
            }
            return false;
        }

    };


    SharedPreferences PREFS;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=this;
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Logger.d(Constants.USER_FCM_ID,PREFS.getString(Constants.USER_FCM_ID,""));
//       DataInOut dataInOut=new DataInOut(this);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_MALE,"Hi maya",1,1);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_MALE,"Hi maya",0,1);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",PREFS.getString(Constants.USER_PHOTO_URL,""),"Hi maya",0,1);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"Hi maya",1,1);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"Hi",0,1);
//        dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"how are u",1,1);
//       dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"good what about u?",0,1);
//       dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"Android developer u?",1,1);
//       dataInOut.insertChatMessage("34DB6C6E0839D4B453E774B57E6296F5","Asia Redeem",Constants.AVATOR_IMAGE_FEMALE,"searching  for job...",0,0);
//       dataInOut.close();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#5C5A6A"));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View headerLayout = navigationView.getHeaderView(0);
        tvUserEmail = (TextView) headerLayout.findViewById(R.id.tvUserEmail);
        tvUserName = (TextView) headerLayout.findViewById(R.id.tvUserName);
        profile_pic = (ImageView) headerLayout.findViewById(R.id.profile_pic);


        Picasso
                .with(this)
                .load(PREFS.getString(Constants.USER_PHOTO_URL,""))
                .fit()
                .into((ImageView) profile_pic);

        tvUserEmail.setText(PREFS.getString(Constants.USER_EMAIL,""));
        tvUserName.setText(Utility.getCamelCase(PREFS.getString(Constants.USER_NAME,"")));


        if(!PREFS.contains(Constants.FIRST_TIME_LOGIN))
        {
            WelcomeUserDialog a = new WelcomeUserDialog(mainActivity, PREFS);
            a.setCancelable(true);
            a.requestWindowFeature(Window.FEATURE_NO_TITLE);
            a.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            a.show();
            notifyAllUser();
        }

        Intent intent=getIntent();
        try
        {
            if(getIntent().getStringExtra("message").equals("1"))
            {
                navigation.setSelectedItemId(R.id.navigation_dashboard);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, ChatBoxFragment.newInstance(null, null)).commit();
            }
            else
            getSupportFragmentManager().beginTransaction().replace(R.id.content, MemoriesFragment.newInstance(null, null)).commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, MemoriesFragment.newInstance(null, null)).commit();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }



    public void notifyAllUser()
    {
        String URL = Constants.URL_NOTIFY_ALL_USER;
        JSONObject input = new JSONObject();
        try
        {
            input.put(Constants.USER_UUID,PREFS.getString(Constants.USER_UUID,""));//PREFS.getString(Constants.USER_UUID,"")
            input.put(Constants.TYPE,"111");
            input.put(Constants.TAG_URL,PREFS.getString(Constants.USER_PHOTO_URL,""));
            input.put(Constants.BIG_ALLOW_ICON,"1");
            input.put(Constants.NOTIFICATION_ID,"14");
            input.put(Constants.BIG_TEXT,PREFS.getString(Constants.USER_NAME,""));
            input.put(Constants.SUMMARY,"");
            input.put(Constants.COLOR,"#FF5656");
            input.put(Constants.MESSAGE,PREFS.getString(Constants.USER_NAME,"")+" is just logged in");
            input.put(Constants.TITLE,"Newbie in active");

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
                        Utility.set(PREFS,Constants.FIRST_TIME_LOGIN,true);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MemoriesApplication.MainActivityFlag=false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        MemoriesApplication.MainActivityFlag=true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        MemoriesApplication.MainActivityFlag=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoriesApplication.MainActivityFlag=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MemoriesApplication.MainActivityFlag=true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MemoriesApplication.MainActivityFlag=true;
    }
}

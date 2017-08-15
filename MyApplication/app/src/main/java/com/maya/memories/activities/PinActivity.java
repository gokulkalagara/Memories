package com.maya.memories.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.maya.memories.R;
import com.maya.memories.adapters.LockNumberAdapter;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;
import com.maya.memories.interfaces.PinActivityInterface;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PinActivity extends AppCompatActivity implements PinActivityInterface,Animation.AnimationListener{

    List<String> mList;
    RecyclerView rvNumbers;
    TextView tvPin[];
    PinActivityInterface pinActivityInterface;
    Animation anim;
    int currentPinCounter = 0;
    char pin[];
    LinearLayout llNumber;
    PinActivity pinActivity;
    SharedPreferences PREFS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        Utility.set(PREFS,Constants.USER_IMEI, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        pinActivity=this;
        setContentView(R.layout.activity_pin);
        pinActivityInterface=(PinActivityInterface)this;
        generateNumberList();
        rvNumbers=(RecyclerView)findViewById(R.id.rvNumbers);
        rvNumbers.setLayoutManager(new GridLayoutManager(this,3));
        llNumber=(LinearLayout) findViewById(R.id.llNumber);
        rvNumbers.setAdapter(new LockNumberAdapter(mList,pinActivityInterface));
        anim = AnimationUtils.loadAnimation(this, R.anim.vibrate);
        anim.setAnimationListener(this);
        pin = new char[4];
        tvPin = new TextView[4];
        tvPin[0] = (TextView)findViewById(R.id.tvPin1);
        tvPin[1] = (TextView)findViewById(R.id.tvPin2);
        tvPin[2] = (TextView)findViewById(R.id.tvPin3);
        tvPin[3] = (TextView)findViewById(R.id.tvPin4);



        Logger.d(Constants.USER_NAME,PREFS.getString(Constants.USER_NAME,""));
        Logger.d(Constants.LAST_NAME,PREFS.getString(Constants.LAST_NAME,""));
        Logger.d(Constants.FIRST_NAME,PREFS.getString(Constants.FIRST_NAME,""));
        Logger.d(Constants.USER_PHOTO_URL,PREFS.getString(Constants.USER_PHOTO_URL,""));
        Logger.d(Constants.USER_EMAIL,PREFS.getString(Constants.USER_EMAIL,""));
        Logger.d(Constants.USER_DOB,PREFS.getString(Constants.USER_DOB,""));
        Logger.d(Constants.USER_GENDER,PREFS.getString(Constants.USER_GENDER,""));
        Logger.d(Constants.USER_ID,PREFS.getString(Constants.USER_ID,""));


        if(Utility.isNetworkAvailable(pinActivity))
        {
            if(!PREFS.contains(Constants.USER_FCM_ID))
            onTokenRefresh();
        }

    }

    public void generateNumberList()
    {
        mList=new ArrayList<String>();
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
        mList.add("5");
        mList.add("6");
        mList.add("7");
        mList.add("8");
        mList.add("9");
        mList.add(" ");
        mList.add("0");
        mList.add(" ");

    }

    @Override
    public void numberClick(String number)
    {
        if(Utility.isNetworkAvailable(pinActivity)) {
            if (currentPinCounter >= 3) {
                if (currentPinCounter == 3) {
                    pin[currentPinCounter] = number.toCharArray()[0];
                    tvPin[currentPinCounter].setText("");
                    tvPin[currentPinCounter].setBackgroundResource(R.drawable.fill_password);
                    Logger.d("Pin is " + Utility.getStringFromCharArray(pin));
                    if (Constants.LOGIN_PIN.equals(Utility.getStringFromCharArray(pin))) {
                        currentPinCounter = 0;
                        tvPin[0].setBackgroundResource(R.drawable.green_circle);
                        tvPin[1].setBackgroundResource(R.drawable.green_circle);
                        tvPin[2].setBackgroundResource(R.drawable.green_circle);
                        tvPin[3].setBackgroundResource(R.drawable.green_circle);
                        //Utility.showToast(pinActivity,"Error","Pin Correct",false);
                        addUser();
                    } else {
                        currentPinCounter = 0;
                        tvPin[0].setBackgroundResource(R.drawable.green_circle);
                        tvPin[1].setBackgroundResource(R.drawable.green_circle);
                        tvPin[2].setBackgroundResource(R.drawable.green_circle);
                        tvPin[3].setBackgroundResource(R.drawable.green_circle);
                        llNumber.startAnimation(anim);
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(300);
                    }

                } else {
                    pin[currentPinCounter] = number.toCharArray()[0];
                    tvPin[currentPinCounter].setText("");
                    tvPin[currentPinCounter].setBackgroundResource(R.drawable.fill_password);
                    currentPinCounter++;

                }

            } else {
                pin[currentPinCounter] = number.toCharArray()[0];
                tvPin[currentPinCounter].setText("");
                tvPin[currentPinCounter].setBackgroundResource(R.drawable.fill_password);
                currentPinCounter++;
            }
        }
        else
        {
            Utility.showToast(pinActivity,"Error",Constants.CONNECTION_ERROR,true);
        }

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



    private void addUser()
    {
        String URL = Constants.URL_ADD_USER;
        JSONObject input = new JSONObject();
        try {
            input.put(Constants.FIRST_NAME,PREFS.getString(Constants.FIRST_NAME,""));
            input.put(Constants.LAST_NAME,PREFS.getString(Constants.LAST_NAME,""));
            input.put(Constants.USER_EMAIL,PREFS.getString(Constants.USER_EMAIL,""));
            input.put(Constants.LOGIN_ID,PREFS.getString(Constants.USER_ID,""));
            input.put(Constants.USER_PHOTO_URL,PREFS.getString(Constants.USER_PHOTO_URL,""));
            input.put(Constants.USER_GENDER,PREFS.getString(Constants.USER_GENDER,""));
            input.put(Constants.USER_DOB,PREFS.getString(Constants.USER_DOB,""));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        VolleyHelperLayer volleyHelperLayer = new VolleyHelperLayer();
        final ProgressDialog progressDialog = Utility.generateProgressDialog(pinActivity);
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
                        JSONObject jsonObject1=jsonObject.getJSONObject(Constants.DATA);
                        Utility.set(PREFS,Constants.USER_UUID,jsonObject1.getString(Constants.USER_UUID));
                        addFcm();
                    }
                    else
                    {
                        Utility.showToast(pinActivity,"error","",true,jsonObject.toString());
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
                Utility.showToast(pinActivity,"error",Constants.CONNECTION_ERROR,true);
                if(progressDialog!=null&&progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

            }
        };
        if (Utility.isNetworkAvailable(pinActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(pinActivity,"Error",Constants.CONNECTION_ERROR,true);


    }


    private void addFcm()
    {
        String URL = Constants.URL_ADD_FCM;
        JSONObject input = new JSONObject();
        try
        {
            if(!PREFS.contains(Constants.USER_FCM_ID))
                onTokenRefresh();
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
        final ProgressDialog progressDialog = Utility.generateProgressDialog(pinActivity);
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
                        Utility.set(PREFS, Constants.PIN_FLAG, true);
                        Intent intent = new Intent(pinActivity, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Utility.showToast(pinActivity,"error","",true,jsonObject.toString());
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
                Utility.showToast(pinActivity,"error",Constants.CONNECTION_ERROR,true);
                if(progressDialog!=null&&progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }

            }
        };
        if (Utility.isNetworkAvailable(pinActivity))
            volleyHelperLayer.startHandlerVolley(URL,input,listener,errorListener, Request.Priority.NORMAL);
        else
            Utility.showToast(pinActivity,"Error",Constants.CONNECTION_ERROR,true);

    }



    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(Constants.USER_FCM_ID, "Refreshed token: " + refreshedToken);
        if (refreshedToken!=null&&refreshedToken.length()>10)
        {
            Utility.set(PREFS, Constants.USER_FCM_ID, refreshedToken);

        }
    }



}

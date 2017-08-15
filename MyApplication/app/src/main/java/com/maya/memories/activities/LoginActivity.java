package com.maya.memories.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.maya.memories.R;
import com.maya.memories.adapters.TabAdapter;
import com.maya.memories.constants.Constants;
import com.maya.memories.others.ViewBackground;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    TabAdapter adapter;
    ViewPager pager;
    TabLayout tabDots;
    Button gmail;
    LoginButton fblogin;

    private List<String> mPermissions = Arrays.asList("public_profile", "email");
    String profilePicUrl;
    CallbackManager callbackManager;
    String id,name,email;
    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    SharedPreferences PREFS;

    LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        loginActivity=this;
        setContentView(R.layout.activity_login);
        pager=(ViewPager)findViewById(R.id.view_pager);
        tabDots=(TabLayout) findViewById(R.id.tab_layout);
        tabDots.setupWithViewPager(pager);
        adapter=new TabAdapter(getSupportFragmentManager());
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setPageTransformer(false,new DepthPageTransformer());
        pager.setAdapter(adapter);
        gmail=(Button)findViewById(R.id.gmail);
        fblogin=(LoginButton)findViewById(R.id.fblogin_button);
        gmail=(Button)findViewById(R.id.gmail);
        fblogin=(LoginButton)findViewById(R.id.fblogin_button);
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        fblogin.setBackgroundResource(R.drawable.fb);
        fblogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        fblogin.setText("");
        fblogin.setCompoundDrawablePadding(0);
        fblogin.setPadding(ViewBackground.dpSize(this, 15), ViewBackground.dpSize(this, 15), ViewBackground.dpSize(this, 16), ViewBackground.dpSize(this, 16));



        fblogin.setReadPermissions(mPermissions);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                final String[] name = new String[1];
                final String[] email = new String[1];

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject json = response.getJSONObject();
                        Logger.d("Facebook Response",""+json.toString());
                        try
                        {
                            if (json != null)
                            {
                                id = "FACEBOOK:"+json.getString("id");

                                Utility.set(PREFS,Constants.USER_ID,id);

                                Utility.set(PREFS,Constants.LAST_NAME,json.getString("last_name"));

                                Utility.set(PREFS,Constants.FIRST_NAME,json.getString("first_name"));

                                if(json.has("email"))
                                    Utility.set(PREFS,Constants.USER_EMAIL,json.getString("email"));
                                else
                                    Utility.set(PREFS,Constants.USER_EMAIL,"XXXX@XXXX.XXX");

                                Utility.set(PREFS,Constants.USER_NAME,json.getString("name"));


                                if(json.has("email"))
                                    Utility.set(PREFS,Constants.USER_GENDER,json.getString("gender"));
                                else
                                    Utility.set(PREFS,Constants.USER_GENDER,"XXXX");


                                if(json.has("birthday"))
                                    Utility.set(PREFS,Constants.USER_DOB,json.getString("birthday"));
                                else
                                    Utility.set(PREFS,Constants.USER_DOB,"XX-XX-XXXX");



                                if (json.has("picture"))
                                {
                                    profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                                }
                                else
                                {
                                    if(json.get("gender").equals("male"))
                                    {
                                        profilePicUrl=Constants.AVATOR_IMAGE_MALE;
                                    }
                                    else
                                    {
                                        profilePicUrl=Constants.AVATOR_IMAGE_FEMALE;
                                    }

                                }
                                Utility.set(PREFS,Constants.USER_PHOTO_URL,profilePicUrl);
                                //LoginManager.getInstance().logOut();
                                Utility.set(PREFS,Constants.LOGIN_FLAG,true);
                                Intent intent=new Intent(loginActivity,PinActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,cover,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                showAlert();
            }
            @Override
            public void onError(FacebookException e) {
                showAlert();
            }
            private void showAlert() {
                Toast.makeText(loginActivity, "Not Granted Permission", Toast.LENGTH_LONG).show();
            }
        };

        fblogin.registerCallback(loginActivity.callbackManager, callback);




        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    // Google Details

    private void handleSignInResult(GoogleSignInResult result)
    {
        //If the login succeed result.getStatus().getStatusCode();
        Logger.d("Google Response Status",""+result.getStatus().toString());
        if (result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            id = "GOOGLE:"+acct.getId();
            Utility.set(PREFS,Constants.USER_ID,id);
            Utility.set(PREFS,Constants.LAST_NAME,acct.getFamilyName());
            Utility.set(PREFS,Constants.FIRST_NAME,acct.getGivenName());
            Utility.set(PREFS,Constants.USER_EMAIL,acct.getEmail());
            Utility.set(PREFS,Constants.USER_NAME,acct.getDisplayName());
            try {
                if (!acct.getPhotoUrl().toString().isEmpty()) {
                    Utility.set(PREFS, Constants.USER_PHOTO_URL, acct.getPhotoUrl().toString());
                }
                else
                {
                    Utility.set(PREFS,Constants.USER_PHOTO_URL,Constants.AVATOR_IMAGE_FEMALE);
                }
            }
            catch (Exception e)
            {
                Utility.set(PREFS,Constants.USER_PHOTO_URL,Constants.AVATOR_IMAGE_FEMALE);
            }



            Utility.set(PREFS,Constants.USER_GENDER,"XXXX");
            Utility.set(PREFS,Constants.USER_DOB,"XX-XX-XXXX");
            signOut();
            Utility.set(PREFS,Constants.LOGIN_FLAG,true);
            Intent intent=new Intent(loginActivity,PinActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.3f;
        private static final float MIN_SCALE1 = 0.8f;


        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            float absPosition = Math.abs(position);
            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0);
            }
            else if (position <= 0)
            { // [-1,0]

                final View bike = view.findViewById(R.id.imageBackground);
                if (bike != null)
                {
                    bike.setAlpha(1 - position);
                    bike.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE1
                            + (1 - MIN_SCALE1) * (1 - Math.abs(position));
                    bike.setScaleX(scaleFactor);
                    bike.setScaleY(scaleFactor);
                }


                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
                view.bringToFront();

                float scaleFactor = MIN_SCALE
                       + (1 - MIN_SCALE) * (1 - Math.abs(position));

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.

                final View bike = view.findViewById(R.id.imageBackground);
                if (bike != null)
                {
                    bike.setAlpha(1 - position);
                    bike.setTranslationX(pageWidth * -position);
                    float scaleFactor = MIN_SCALE1
                            + (1 - MIN_SCALE1) * (1 - Math.abs(position));
                    bike.setScaleX(scaleFactor);
                    bike.setScaleY(scaleFactor);
                }


                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(scaleFactor);


            }
            else
            { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);


            }
        }
    }
}

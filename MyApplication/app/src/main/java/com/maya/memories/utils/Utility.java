package com.maya.memories.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormatSymbols;

/**
 * Created by Maya on 4/3/2017.
 */

public class Utility
{

    private static String exactPhoneNumber;


    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            /* show a toast, we cannot use the internet right now */
            //showToast(activity, "network_unavailable_message", Constants.NO_INTERNET_CONNECTION, true);
            /*if(HomeScreenActivity.mHomeScreenActivity != null){
                HomeScreenActivity.mHomeScreenActivity.tvInternetStatus.setText(Constants.NO_INTERNET_CONNECTION);
            }*/
            return false;
            /* aka, do nothing */
        }
        return true;
    }

    public static void set(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void set(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void del(SharedPreferences sharedPreferences, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static String getCamelCase(String name) {
        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();

        while(flag==1439)
        {

            if(name.contains("  "))
            {

                name=name.replaceAll("  "," ");

            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString());
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return name;
        }
        return sb.toString();

    }

    public static String getShortName(String name) {
        int flag=1439;
        name=name.trim();
        name=name.toLowerCase();
        while(flag==1439)
        {

            if(name.contains("  "))
            {

                name=name.replaceAll("  "," ");

            }
            else
            {
                flag=3914;
            }


        }

        StringBuilder sb = new StringBuilder();
        try{
            String[] words = name.toString().split(" ");
            if (words[0].length() > 0)
            {
                sb.append(Character.toUpperCase(words[0].charAt(0)));
                for (int i = 1; i < words.length; i++)
                {
                    sb.append(Character.toLowerCase(words[i].charAt(0)));
                }
            }
        }
        catch(Exception e){
            //System.out.println(e.toString());
            e.printStackTrace();
            return (""+name.charAt(0)).toUpperCase();
        }

        if(sb.length()>2)
        {
            return (sb.toString()).substring(0,2);
        }
        else
        {
            return sb.toString();
        }
    }

    public static boolean isSuccessful(String s)
    {
        try {
            JSONObject jsonObject = new JSONObject(s);
            return jsonObject.getBoolean("status");
        } catch (Exception e)
        {
            return false;
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static ProgressDialog generateProgressDialog(Activity activity)
    {
        try {

            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.show();
            progressDialog.setContentView(R.layout.progressdialog);
            return progressDialog;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeProgressDialog(ProgressDialog progressDialog)
    {
        try
        {
            if(progressDialog!=null&&progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void showToast(Context c, String t, String s, boolean duration) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        tst.setText(s);
        tst.show();
    }

    public static void showToast(Context c, String t, String s, boolean duration, String response) {
        if (c == null) return;
        Toast tst = Toast.makeText(c, t, duration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("error")) {
                tst.setText(jsonObject.getString("error").toString());
            }
            else if(jsonObject.has("message")){
                tst.setText(jsonObject.getString("message").toString());
            }
            else {
                tst.setText(s);
            }
            tst.show();
        } catch (Exception e) {
            Logger.d("[Exception]", e.toString());
        }
    }

    public static void displayError(JSONObject jsonObject,Context context)
    {
        try {
            Utility.showToast(context,"Error",jsonObject.getString("error"),true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static String makeJSDateReadable(String s)
    {
        try {
            String ret = "";
            String[] arr = s.split("-");
            ret = new DateFormatSymbols().getMonths()[Integer.parseInt(arr[1]) - 1] + " " + arr[0] + ret;
            ret = arr[2].split("T")[0] + " " + ret;
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }







    @Nullable
    public static Bitmap urlToBitMap(String urlLink){
        try {
            URL url = new URL(urlLink);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static String getPhoneNumber(String name, Context context)
    {
        try {
            String ret = null;
            String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "='" + name + "'";
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, selection, null, null);
            if (c.moveToFirst()) {
                ret = c.getString(0);
            }
            c.close();
            if (ret == null)
                ret = "Unsaved";
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public static String getExactPhoneNumber(String number)
    {
        exactPhoneNumber = number.substring(number.length()-10,number.length());
        return exactPhoneNumber;
    }
    public static String removeSpaces(String number){
        String ans="";
        for(int i=0;i<number.length();i++){
            if(number.charAt(i) == ' ' || number.charAt(i) == '-'){
                continue;
            }
            ans = ans +number.charAt(i);
        }
        return ans;
    }


    public static String getStringFromCharArray(char[] pin)
    {
        return ""+pin[0]+pin[1]+pin[2]+pin[3];
    }


    public static String getTimeAgo(long time)
    {
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < Constants.MINUTE_MILLIS)
        {
            return "just now";
        }
        else if (diff < 2 * Constants.MINUTE_MILLIS)
        {
            return "a minute ago";
        }
        else if (diff < 50 * Constants.MINUTE_MILLIS)
        {
            return diff / Constants.MINUTE_MILLIS + " minutes ago";
        }
        else if (diff < 90 * Constants.MINUTE_MILLIS) {
            return "an hour ago";
        }
        else if (diff < 24 * Constants.HOUR_MILLIS) {
            return diff / Constants.HOUR_MILLIS + " hours ago";
        }
        else if (diff < 48 * Constants.HOUR_MILLIS) {
            return "yesterday";
        }
        else
        {
            return diff / Constants.DAY_MILLIS + " days ago";
        }
    }



    public static void addlogs(String rollNumber,String type,String u_uid)
    {
        String URL = Constants.URL_LOG_ADD;
        JSONObject input = new JSONObject();
        try
        {
            input.put(Constants.USER_UUID,u_uid);
            input.put("roll_number",rollNumber);
            input.put("type",type);
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

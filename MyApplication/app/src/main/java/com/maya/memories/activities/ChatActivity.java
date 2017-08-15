package com.maya.memories.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maya.memories.R;
import com.maya.memories.adapters.ChatAdapter;
import com.maya.memories.api.VolleyHelperLayer;
import com.maya.memories.applications.MemoriesApplication;
import com.maya.memories.constants.Constants;
import com.maya.memories.databases.DataInOut;
import com.maya.memories.fragments.ChatBoxFragment;
import com.maya.memories.interfaces.InterfaceToChatAdapter;
import com.maya.memories.models.ChatBoxItem;
import com.maya.memories.models.ChatItem;
import com.maya.memories.models.UserItem;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ChatBoxItem chatBoxItem;
    UserItem userItem;
    RecyclerView recyclerView;
    EditText etMessageBox;
    FloatingActionButton send;
    ImageView imgPerson;

    public DataInOut dataInOut;
    public ChatAdapter chatAdapter;
    public static ChatActivity chatActivity;
    public String u_uid,userName,profilePic;
    List<ChatItem> list;
    LinearLayoutManager mLayoutManager;
    public InterfaceToChatAdapter interfaceToChatAdapter;
    public SharedPreferences PREFS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataInOut=new DataInOut(this);
        PREFS=getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        chatActivity=this;
        if(getIntent().getStringExtra("type").equals("0"))
        {
            chatBoxItem = (ChatBoxItem) getIntent().getSerializableExtra("ChatBoxItem");
            u_uid=chatBoxItem.u_uid;
            userName=chatBoxItem.userName;
            profilePic=chatBoxItem.profilePic;
        }
        else
        {
            userItem = (UserItem) getIntent().getSerializableExtra("UserItem");
            u_uid=userItem.u_uid;
            userName=userItem.firstName+" "+userItem.lastName;
            profilePic=userItem.profile_pic;

        }


        setContentView(R.layout.activity_chat_activty);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        etMessageBox=(EditText) findViewById(R.id.etMessageBox);
        send=(FloatingActionButton) findViewById(R.id.send);
        imgPerson=(ImageView) findViewById(R.id.imgPerson);



        Picasso
                .with(this)
                .load(profilePic)
                .fit()
                .into((ImageView) imgPerson);


        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        list=dataInOut.getChatMessages(u_uid);
        dataInOut.close();
        if(list!=null)
        {
            mLayoutManager.scrollToPosition(list.size()-1);
            chatAdapter=new ChatAdapter(list);
            interfaceToChatAdapter=(ChatAdapter)chatAdapter;
            recyclerView.setAdapter(chatAdapter);
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isNetworkAvailable(chatActivity))
                {
                    String message=""+etMessageBox.getText().toString().trim();
                    if(message.length()>0&&message.length()<220)
                    {
                        etMessageBox.setText("");
                        sendMessage(message,1,1);
                    }
                    else
                    {
                        Utility.showToast(chatActivity,"Error","empty or limit exceed",true);
                        etMessageBox.setText("");
                    }
                }
                else
                {
                    Utility.showToast(chatActivity,"Error", Constants.CONNECTION_ERROR,true);
                }
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                if(list!=null)
                recyclerView.scrollToPosition(list.size()-1);

            }
        });


    }



    public void sendMessage(String message,int type,int readType)
    {
        dataInOut.open(chatActivity);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        dataInOut.insertChatMessage(u_uid,userName,profilePic,message,type,readType);


        if(list==null)
        {
            list=dataInOut.getChatMessages(u_uid);
            mLayoutManager.scrollToPosition(list.size()-1);
            chatAdapter=new ChatAdapter(list);
            interfaceToChatAdapter=(ChatAdapter)chatAdapter;
            recyclerView.setAdapter(chatAdapter);
        }
        else
        {
            interfaceToChatAdapter=(ChatAdapter)chatAdapter;
            ChatItem chatItem=new ChatItem(u_uid,message,type,timestamp);
            interfaceToChatAdapter.addItem(chatItem);
            mLayoutManager.scrollToPosition(list.size()-1);

        }
        chatActivity.send(message);
        ChatBoxFragment.chatBoxFragment.changeView();
    }

    public void receiveMessage(String message,int type,int readType)
    {
        dataInOut.open(chatActivity);
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        dataInOut.insertChatMessage(u_uid,userName,profilePic,message,type,readType);


        if(list==null)
        {
            list=dataInOut.getChatMessages(u_uid);
            mLayoutManager.scrollToPosition(list.size()-1);
            chatAdapter=new ChatAdapter(list);
            interfaceToChatAdapter=(ChatAdapter)chatAdapter;
            recyclerView.setAdapter(chatAdapter);
        }
        else
        {
            interfaceToChatAdapter=(ChatAdapter)chatAdapter;
            ChatItem chatItem=new ChatItem(u_uid,message,type,timestamp);
            interfaceToChatAdapter.addItem(chatItem);
            mLayoutManager.scrollToPosition(list.size()-1);

        }
        ChatBoxFragment.chatBoxFragment.changeView();
    }



    public void send(String message)
    {
        String URL = Constants.URL_SEND_MESSAGE;
        JSONObject input = new JSONObject();
        try
        {
            input.put(Constants.USER_UUID,u_uid);
            input.put("m_u_uid",PREFS.getString(Constants.USER_UUID,""));//PREFS.getString(Constants.USER_UUID,"")
            input.put(Constants.TYPE,"222");
            input.put(Constants.TAG_URL,PREFS.getString(Constants.USER_PHOTO_URL,""));
            input.put(Constants.BIG_ALLOW_ICON,"1");
            input.put(Constants.NOTIFICATION_ID,"0514");
            input.put(Constants.BIG_TEXT,PREFS.getString(Constants.USER_NAME,""));
            input.put(Constants.SUMMARY,"");
            input.put(Constants.COLOR,"#FF5656");
            input.put(Constants.MESSAGE,message);
            input.put(Constants.TITLE,PREFS.getString(Constants.USER_NAME,""));

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

    @Override
    public void onBackPressed() {
        if(ChatBoxFragment.chatBoxFragment!=null)
        {
            ChatBoxFragment.chatBoxFragment.changeView();
        }
        MemoriesApplication.ChatActivityFlag=false;
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        MemoriesApplication.ChatActivityFlag=true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        MemoriesApplication.ChatActivityFlag=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MemoriesApplication.ChatActivityFlag=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MemoriesApplication.ChatActivityFlag=true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MemoriesApplication.ChatActivityFlag=true;
    }
}

package com.maya.memories.databases;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maya.memories.constants.Constants;
import com.maya.memories.models.ChatBoxItem;
import com.maya.memories.models.ChatItem;
import com.maya.memories.utils.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 7/30/2017.
 */

public class DataInOut
{
    private static SQLiteDatabase MEMORIES_CHAT_DB;
    public Context context;
    public DataInOut(Context context)
    {
        this.context=context;

        try {
            MEMORIES_CHAT_DB = context.openOrCreateDatabase(Constants.DATABASE,Context.MODE_PRIVATE, null);
            MEMORIES_CHAT_DB.execSQL("create table if not exists chat_list_messages " +
                    "(" +
                    "sender_id varchar[80]," +
                    "message varchar[300]," +
                    "message_type INTEGER," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")" +
                    "");
            MEMORIES_CHAT_DB.execSQL("create table if not exists chat_box" +
                    "(" +
                    "sender_id varchar[80] PRIMARY KEY," +
                    "sender_name varchar[80],"+
                    "sender_pic varchar[300],"+
                    "message varchar[300]," +
                    "read_type INTEGER," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");
        }
        catch(Exception e)
        {
            Logger.d("DataInOut Constructor","Maya main"+e.toString());
        }
    }

    public void open(Context context)
    {
        this.context=context;

        try
        {
            MEMORIES_CHAT_DB = context.openOrCreateDatabase(Constants.DATABASE, Context.MODE_PRIVATE, null);
        }
        catch(Exception e)
        {
            Logger.d("DataInOut Open","Maya main"+e.toString());
        }
    }


    public void insertChatMessage(String senderId,String senderName,String senderPic,String message,int messageType,int readType)
    {
        try
        {
            MEMORIES_CHAT_DB.execSQL("INSERT INTO chat_list_messages(sender_id, message, message_type,created_at) VALUES " +
                    "("

                    +"'"+senderId+"','"+message+"',"+messageType+",'"+new Timestamp(System.currentTimeMillis())+"'"+

                    ")");


            MEMORIES_CHAT_DB.execSQL("insert or replace into chat_box(sender_id, sender_name, sender_pic, message, read_type,created_at) VALUES " +
                    "("

                    +"'"+senderId+"','"+senderName+"','"+senderPic+"','"+message+"',"+readType+",'"+new Timestamp(System.currentTimeMillis())+"'"+

                    ")");

        }
        catch (Exception e)
        {
            Logger.d("Insert Chat Message",""+e.getMessage());
        }
    }

    public void close()
    {
        try {
            MEMORIES_CHAT_DB.close();
        }
        catch (Exception e)
        {
            Logger.d("CLOSE",e.getMessage());
        }
    }


    public List<ChatBoxItem> getChatBox()
    {
        try {
            Cursor cursorObject = MEMORIES_CHAT_DB.rawQuery("select * from chat_box ORDER BY datetime(created_at) DESC", null);
            if (cursorObject.getCount() == 0) {
                return null;

            }
            else
            {
                List<ChatBoxItem> list=new ArrayList<ChatBoxItem>();
                cursorObject.moveToFirst();
                if (cursorObject != null)
                {

                    do
                    {
                        String id=new String(cursorObject.getString(cursorObject.getColumnIndex("sender_id")));
                        String name=new String(cursorObject.getString(cursorObject.getColumnIndex("sender_name")));
                        String profilePic=new String(cursorObject.getString(cursorObject.getColumnIndex("sender_pic")));
                        String message=new String(cursorObject.getString(cursorObject.getColumnIndex("message")));
                        int readyType=Integer.parseInt(cursorObject.getString(cursorObject.getColumnIndex("read_type")));
                        Timestamp timestamp=Timestamp.valueOf(cursorObject.getString(cursorObject.getColumnIndex("created_at")));


                        Logger.d("Content "," "+id+" "+name+" "+profilePic+" "+readyType+" "+message+" "+timestamp.toString());

                        list.add(new ChatBoxItem(
                                id,
                                name,
                                profilePic,
                                message,
                                readyType,
                                timestamp)
                        );
                    }
                    while (cursorObject.moveToNext());

                    return list;


                }

            }


        }
        catch (Exception e)
        {
            Logger.d("Get Chat Box",e.getMessage());
        }

        return null;
    }



    public void updateReadMessage(String u_uid)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put("read_type", 1);
            MEMORIES_CHAT_DB.update("chat_box", cv, "sender_id='" + u_uid + "'", null);
        }
        catch (Exception e)
        {
            Logger.d("update Read Message", e.getMessage());
        }

    }

    // get chat messages
    public List<ChatItem> getChatMessages(String u_uid)
    {
        try {
            Cursor cursorObject = MEMORIES_CHAT_DB.rawQuery("select * from chat_list_messages where sender_id='"+u_uid+"'", null);
            if (cursorObject.getCount() == 0) {
                return null;

            }
            else
            {
                List<ChatItem> list=new ArrayList<ChatItem>();
                cursorObject.moveToFirst();
                if (cursorObject != null)
                {

                    do
                    {
                        String id=new String(cursorObject.getString(cursorObject.getColumnIndex("sender_id")));
                        String message=new String(cursorObject.getString(cursorObject.getColumnIndex("message")));
                        int messageType=Integer.parseInt(cursorObject.getString(cursorObject.getColumnIndex("message_type")));
                        Timestamp timestamp=Timestamp.valueOf(cursorObject.getString(cursorObject.getColumnIndex("created_at")));


                        Logger.d("Content "," "+id+" "+messageType+" "+message+" "+timestamp.toString());

                        list.add(new ChatItem(
                                id,
                                message,
                                messageType,
                                timestamp)
                        );
                    }
                    while (cursorObject.moveToNext());

                    return list;


                }

            }


        }
        catch (Exception e)
        {
            Logger.d("Get Chat Messages",e.getMessage());
        }

        return null;
    }


}

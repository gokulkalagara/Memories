package com.maya.memories.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.maya.memories.R;
import com.maya.memories.activities.ChatActivity;
import com.maya.memories.activities.MainActivity;
import com.maya.memories.activities.SplashActivity;
import com.maya.memories.applications.MemoriesApplication;
import com.maya.memories.databases.DataInOut;
import com.maya.memories.fragments.ChatBoxFragment;
import com.maya.memories.utils.Logger;
import com.maya.memories.utils.Utility;


/**
 * Created by Maya on 11/30/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "MyFirebaseMsgService";
    public String U_UID=null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {
            Logger.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        try
        {
            String content = remoteMessage.getData().get("message");
            String bitmap_url=remoteMessage.getData().get("url");
            String title=remoteMessage.getData().get("title");
            String bigText=remoteMessage.getData().get("big_text");
            String summary=remoteMessage.getData().get("summary");
            String color=remoteMessage.getData().get("color");
            String allowBig=remoteMessage.getData().get("big_allow_icon");
            String notification_id=remoteMessage.getData().get("nofitication_id");
            String optionType=remoteMessage.getData().get("type");
            try
            {
                U_UID=remoteMessage.getData().get("m_u_uid");
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
            sendNotification(content,bitmap_url,title,bigText,summary,color,allowBig,notification_id,optionType);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private void sendNotification(final String messageBody,final String bitmap_url,final String title,final String bigText,String summary,String color,String allowBig,String notication_id,String optionType)
    {


    if(messageBody!=null)
    {
    if (optionType != null)
    {
                    if (optionType.equals("222")) // messages purpose
                    {


                        if(MemoriesApplication.ChatActivityFlag==true||MemoriesApplication.MainActivityFlag==true)
                        {

                                if(MemoriesApplication.ChatActivityFlag==true)
                                {

                                            if(ChatActivity.chatActivity.u_uid.equals(U_UID))
                                            {
                                            ChatActivity.chatActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    ChatActivity.chatActivity.receiveMessage(messageBody,0,1);
                                                }
                                            });

                                            }
                                            else
                                            {
                                                MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run()
                                                    {
                                                        DataInOut dataInOut=new DataInOut(MainActivity.mainActivity);
                                                        dataInOut.insertChatMessage(U_UID,bigText,bitmap_url,messageBody,0,0);
                                                        dataInOut.close();

                                                    }
                                                });
                                                if(ChatBoxFragment.chatBoxFragment!=null)
                                                {
                                                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run()
                                                        {
                                                            ChatBoxFragment.chatBoxFragment.changeView();

                                                        }
                                                    });
                                                }
                                                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                NotificationManager notificationManager =
                                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.message_notification);
                                                remoteViews.setTextViewText(R.id.tvPersonName, bigText);
                                                remoteViews.setTextViewText(R.id.tvMessage,messageBody);
                                                if (bitmap_url != null) {
                                                    Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                                                    if (bitmap1 != null) {
                                                        remoteViews.setImageViewBitmap(R.id.imgPerson, bitmap1);

                                                    }
                                                } else {

                                                    return;

                                                }
                                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                                                if (title != null) {
                                                    notificationBuilder.setContentTitle(title);
                                                } else {
                                                    notificationBuilder.setContentTitle("Memories");
                                                }
                                                notificationBuilder.setSmallIcon(R.drawable.ic_white);
                                                notificationBuilder.setAutoCancel(true);
                                                notificationBuilder.setSound(defaultSoundUri);
                                                notificationBuilder.setCustomBigContentView(remoteViews);
                                                notificationBuilder.setContentText(messageBody);
                                                notificationBuilder.setColor(Color.parseColor("#FF5656"));
                                                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories));

                                                if (notication_id != null)
                                                {
                                                    try {
                                                        int notification_number = Integer.parseInt(notication_id);
                                                        if (notification_number > 0) {
                                                            notificationManager.notify(notification_number, notificationBuilder.build());
                                                        }
                                                    } catch (Exception e) {
                                                        notificationManager.notify(0, notificationBuilder.build());
                                                    }
                                                } else {
                                                    notificationManager.notify(0, notificationBuilder.build());
                                                }

                                            }

                                }
                                else
                                {

                                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            DataInOut dataInOut=new DataInOut(MainActivity.mainActivity);
                                            dataInOut.insertChatMessage(U_UID,bigText,bitmap_url,messageBody,0,0);
                                            dataInOut.close();

                                        }
                                    });
                                    if(ChatBoxFragment.chatBoxFragment!=null)
                                    {
                                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run()
                                            {
                                                ChatBoxFragment.chatBoxFragment.changeView();

                                            }
                                        });
                                    }
                                    Intent intent = new Intent(this, MainActivity.class);
                                    intent.putExtra("message", "1");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    NotificationManager notificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.message_notification);
                                    remoteViews.setTextViewText(R.id.tvMessage,messageBody);
                                    remoteViews.setTextViewText(R.id.tvPersonName, bigText);

                                    if (bitmap_url != null) {
                                        Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                                        if (bitmap1 != null) {
                                            remoteViews.setImageViewBitmap(R.id.imgPerson, bitmap1);

                                        }
                                    } else {

                                        return;

                                    }
                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                                    if (title != null) {
                                        notificationBuilder.setContentTitle(title);
                                    } else {
                                        notificationBuilder.setContentTitle("Memories");
                                    }
                                    notificationBuilder.setSmallIcon(R.drawable.ic_white);
                                    notificationBuilder.setAutoCancel(true);
                                    notificationBuilder.setSound(defaultSoundUri);
                                    notificationBuilder.setCustomBigContentView(remoteViews);
                                    notificationBuilder.setContentText(messageBody);
                                    notificationBuilder.setColor(Color.parseColor("#FF5656"));
                                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories)).setContentIntent(pendingIntent);

                                    if (notication_id != null)
                                    {
                                        try {
                                            int notification_number = Integer.parseInt(notication_id);
                                            if (notification_number > 0) {
                                                notificationManager.notify(notification_number, notificationBuilder.build());
                                            }
                                        } catch (Exception e) {
                                            notificationManager.notify(0, notificationBuilder.build());
                                        }
                                    } else {
                                        notificationManager.notify(0, notificationBuilder.build());
                                    }
                                }

                        }
                        else
                        {

                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("message", "1");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.message_notification);
                            remoteViews.setTextViewText(R.id.tvPersonName, bigText);
                            remoteViews.setTextViewText(R.id.tvMessage,messageBody);
                            if (bitmap_url != null) {
                                Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                                if (bitmap1 != null) {
                                    remoteViews.setImageViewBitmap(R.id.imgPerson, bitmap1);

                                }
                            } else {

                                return;

                            }
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                            if (title != null) {
                                notificationBuilder.setContentTitle(title);
                            } else {
                                notificationBuilder.setContentTitle("Memories");
                            }
                            notificationBuilder.setSmallIcon(R.drawable.ic_white);
                            notificationBuilder.setAutoCancel(true);
                            notificationBuilder.setSound(defaultSoundUri);
                            notificationBuilder.setCustomBigContentView(remoteViews);
                            notificationBuilder.setContentText(messageBody);
                            notificationBuilder.setColor(Color.parseColor("#FF5656"));
                            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories))
                                    .setContentIntent(pendingIntent);

                            if (notication_id != null)
                            {
                                try {
                                    int notification_number = Integer.parseInt(notication_id);
                                    if (notification_number > 0) {
                                        notificationManager.notify(notification_number, notificationBuilder.build());
                                    }
                                } catch (Exception e) {
                                    notificationManager.notify(0, notificationBuilder.build());
                                }
                            } else {
                                notificationManager.notify(0, notificationBuilder.build());
                            }

                            Logger.d("222","work here");
                            DataInOut dataInOut=new DataInOut(getApplicationContext());
                            dataInOut.insertChatMessage(U_UID,bigText,bitmap_url,messageBody,0,0);
                            dataInOut.close();

                        }



                    }
                    else if(optionType.equals("111"))  // notify the new user
                    {

                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.putExtra("message", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationManager notificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.login_notify);
                            remoteViews.setTextViewText(R.id.tvPersonName, bigText);
                            if (bitmap_url != null) {
                                Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                                if (bitmap1 != null) {
                                    remoteViews.setImageViewBitmap(R.id.imgPerson, bitmap1);

                                }
                            } else {

                                return;

                            }
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                            if (title != null) {
                                notificationBuilder.setContentTitle(title);
                            } else {
                                notificationBuilder.setContentTitle("Memories");
                            }
                            notificationBuilder.setSmallIcon(R.drawable.ic_white);
                            notificationBuilder.setAutoCancel(true);
                            notificationBuilder.setSound(defaultSoundUri);
                            notificationBuilder.setCustomBigContentView(remoteViews);
                            notificationBuilder.setContentText(messageBody);
                            notificationBuilder.setColor(Color.parseColor("#FF5656"));
                            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories)).setContentIntent(pendingIntent);

                            if (notication_id != null)
                            {
                                try {
                                    int notification_number = Integer.parseInt(notication_id);
                                    if (notification_number > 0) {
                                        notificationManager.notify(notification_number, notificationBuilder.build());
                                    }
                                } catch (Exception e) {
                                    notificationManager.notify(0, notificationBuilder.build());
                                }
                            } else {
                                notificationManager.notify(0, notificationBuilder.build());
                            }
                    }
                    else if(optionType.equals("333"))
                    {
                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.putExtra("message", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                .setTicker(title).setWhen(0)
                                .setSmallIcon(R.drawable.ic_white)
                                .setContentText(messageBody)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                                .setContentIntent(pendingIntent);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (title != null) {
                            notificationBuilder.setContentTitle(title);
                        } else {
                            notificationBuilder.setContentTitle("Memories");
                        }
                        if (color != null) {
                            notificationBuilder.setColor(Color.parseColor(color));
                        } else {
                            notificationBuilder.setColor(Color.parseColor("#FF5656"));
                        }
                        if (summary != null)
                            bigPictureStyle.setSummaryText(messageBody);
                        if (summary != null)
                            bigPictureStyle.setSummaryText(messageBody);
                        if (bigText != null) {
                            bigPictureStyle.setBigContentTitle(bigText);
                        } else {

                        }
                        if (bitmap_url != null) {
                            Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                            if (bitmap1 != null) {
                                bigPictureStyle.bigPicture(bitmap1);
                                notificationBuilder.setStyle(bigPictureStyle);

                            }
                        } else {
                            //notificationBuilder.setStyle(bigPictureStyle);
                        }
                        if (allowBig != null) {
                            if (allowBig.equals("1")) {
                                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories));
                            } else {

                            }
                        } else {

                        }
                        if (notication_id != null) {
                            try {
                                int notification_number = Integer.parseInt(notication_id);
                                if (notification_number > 0) {
                                    notificationManager.notify(notification_number, notificationBuilder.build());
                                }
                            } catch (Exception e) {
                                notificationManager.notify(0, notificationBuilder.build());
                            }


                        } else {
                            notificationManager.notify(0, notificationBuilder.build());
                        }
                    }
                    else
                    {
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

                        Intent intent = new Intent(this, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                                .setTicker(title).setWhen(0)
                                .setSmallIcon(R.drawable.ic_white)
                                .setContentText(messageBody)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                                .setContentIntent(pendingIntent);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (title != null) {
                            notificationBuilder.setContentTitle(title);
                        } else
                            {
                            notificationBuilder.setContentTitle("Memories");
                        }
                        if (color != null) {
                            notificationBuilder.setColor(Color.parseColor(color));
                        } else {
                            notificationBuilder.setColor(Color.parseColor("#FF5656"));
                        }
                        if (summary != null)
                            bigPictureStyle.setSummaryText(messageBody);
                        if (summary != null)
                            bigPictureStyle.setSummaryText(messageBody);
                        if (bigText != null) {
                            bigPictureStyle.setBigContentTitle(bigText);
                        } else {

                        }
                        if (bitmap_url != null) {
                            Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                            if (bitmap1 != null) {
                                bigPictureStyle.bigPicture(bitmap1);
                                notificationBuilder.setStyle(bigPictureStyle);

                            }
                        } else {
                            //notificationBuilder.setStyle(bigPictureStyle);
                        }
                        if (allowBig != null) {
                            if (allowBig.equals("1")) {
                                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.memories));
                            } else {

                            }
                        } else {

                        }
                        if (notication_id != null) {
                            try {
                                int notification_number = Integer.parseInt(notication_id);
                                if (notification_number > 0) {
                                    notificationManager.notify(notification_number, notificationBuilder.build());
                                }
                            } catch (Exception e) {
                                notificationManager.notify(0, notificationBuilder.build());
                            }


                        } else {
                            notificationManager.notify(0, notificationBuilder.build());
                        }
                    }

    }
    }

    }


}

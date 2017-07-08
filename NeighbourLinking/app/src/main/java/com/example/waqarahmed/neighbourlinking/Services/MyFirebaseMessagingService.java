package com.example.waqarahmed.neighbourlinking.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.MainActivity;
import com.example.waqarahmed.neighbourlinking.Classes.AppUtils;
import com.example.waqarahmed.neighbourlinking.Classes.BadgeUtils;
import com.example.waqarahmed.neighbourlinking.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by waqas on 3/3/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {
        int requestID = (int) System.currentTimeMillis();
//        PendingIntent intent = PendingIntent.getActivity(context, requestID,
//                notificationIntent,0 );

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText(message);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        requestID,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
                                                       // mId allows you to update the notification later on.

        int unOpenCount= AppUtils.getPreferenceInt("NOTICOUNT",this);
        unOpenCount=unOpenCount+1;
        AppUtils.savePreferenceLong("NOTICOUNT",unOpenCount,this);
        mNotificationManager.notify(String.valueOf(unOpenCount),requestID, mBuilder.build());
        BadgeUtils.setBadge( MyFirebaseMessagingService.this,(int)unOpenCount);


    }

//    public void buidNotification(){
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)  getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//    }

}

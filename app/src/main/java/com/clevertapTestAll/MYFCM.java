package com.clevertapTestAll;

import static com.xiaomi.push.db.t;

import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Constants;
import com.clevertap.android.sdk.Logger;
import com.clevertap.android.sdk.pushnotification.NotificationInfo;
import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
//import com.xiaomi.mipush.sdk.MiPushMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;




public class MYFCM extends FirebaseMessagingService {

        public static final String NOTIFICATION_CHANNEL_ID = "CT1104";


    @Override
        public void onNewToken(@NonNull @NotNull String refreshedToken) {
            super.onNewToken(refreshedToken);
            //storeToken(refreshedToken);
        }

        @Override
        public void onMessageReceived(RemoteMessage message) {
//            try {

                new CTFcmMessageHandler().createNotification(getApplicationContext(), message);

//                Bundle extras = new Bundle();
//                Log.d("Am here  " , "notif");
//                if (message.getData().size() > 0) {
//                    for (Map.Entry<String, String> entry : message.getData().entrySet()) {
//                        extras.putString(entry.getKey(), entry.getValue());
//                    }
//
//                    CleverTapAPI.processPushNotification(getApplicationContext(), extras);
//
//                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    // custom rendering
//                    int notificationId = new Random().nextInt(60000);
//                    Intent intentnew = new Intent();
//                    intentnew.setAction(Intent.ACTION_VIEW);
//                    intentnew.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intentnew.putExtras(extras);
//                    PendingIntent pendingIntentnew = PendingIntent.getActivity(getApplicationContext(), 0, intentnew, PendingIntent.FLAG_MUTABLE);
//                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "wzrk_cid")
//                            .setSmallIcon(com.clevertap.android.sdk.R.drawable.ct_volume_on) //a resource for your custom small icon
//                            .setColor(Color.YELLOW)
//                            .setContentTitle(extras.getString("nm"))
//                            .setContentText(extras.getString("nt"))
//                            .setContentIntent(pendingIntentnew)
//                            .setAutoCancel(true);
//                    notificationManager.notify(notificationId, notificationBuilder.build());

               // }
//            } catch (Throwable t) {
//                Log.d("MYFCMLIST", "Error parsing FCM message", t);
//            }
        }

}
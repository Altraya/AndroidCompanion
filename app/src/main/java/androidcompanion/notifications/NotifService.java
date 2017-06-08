package androidcompanion.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidcompanion.main.SystemManager;

public class NotifService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        String pack = sbn.getPackageName();
        String ticker = "";
        if(sbn.getNotification().tickerText != null){
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        Intent msgrcv = new Intent("androidcompanion.notifications.NOTIFICATION_EVENT");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        sendBroadcast(msgrcv);

    }

    //WHEN A NOTIF IS REMOVED
    //    @Override
    //    public void onNotificationRemoved(StatusBarNotification sbn) {
    //        Log.i(TAG,"********** onNOtificationRemoved");
    //        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
    //        Intent i = new  Intent("androidcompanion.notifications.NOTIFICATION_LISTENER_EXAMPLE");
    //        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
    //
    //        sendBroadcast(i);
    //    }

}

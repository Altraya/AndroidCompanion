package androidcompanion.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidcompanion.main.SystemManager;

/**
 * Notification listener service
 * Broadcast message to PC
 * @author Phillippine
 * */
public class NotifService extends NotificationListenerService {

    @Override
    public void onCreate(){
        System.out.println("NOTIFY SERVICE ACTIVE");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("NOTIFY SERVICE BOUND");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        System.out.println("NOTIFY SERVICE CATCH");

        String pack = sbn.getPackageName();
        String ticker = "";
        if(sbn.getNotification().tickerText != null){
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        Intent msgrcv = new Intent("androidcompanion.notifications.NOTIFICATION_EVENT");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        System.out.println("Notification caught by service");
        System.out.println("Title : " + title + " Text : " + text);

        sendBroadcast(msgrcv);

    }

}

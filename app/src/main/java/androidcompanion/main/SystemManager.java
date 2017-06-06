package androidcompanion.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import androidcompanion.data.SaveManager;
import androidcompanion.netcode.Client;
import androidcompanion.netcode.ClientManager;
import androidcompanion.notifications.NotificationInterpretor;
import androidcompanion.notifications.NotifyFactory;

/**
 * Created by Jo on 24/04/2017.
 */
// TODO disconnect device if server shut down
public class SystemManager {

    //Singleton

    private static SystemManager instance = null;
    protected SystemManager() {
        // Exists only to defeat instantiation.
    }
    public static SystemManager getInstance() {
        if(instance == null) {
            instance = new SystemManager();
            instance.instanciate();
        }
        return instance;
    }

    //Instance

    private NotifyFactory notifyFactory;
    private NotificationInterpretor notificationInterpretor;
    private ClientManager clientManager;
    private SaveManager saveManager;
    private PermissionManager permissionManager;

    //Set up function
    public void instanciate(){

        notifyFactory = new NotifyFactory();
        notificationInterpretor = new NotificationInterpretor();
        clientManager = new ClientManager();
        saveManager = new SaveManager();
        permissionManager = new PermissionManager();

        LocalBroadcastManager.getInstance(MyApp.getContext()).registerReceiver(onNotice, new IntentFilter("Msg"));

    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String ticker = intent.getStringExtra("ticker");
            String text = intent.getStringExtra("text");

            if(ticker != null) //if the ticker is not null that means it's a message
            {
                //here we will send the right message and not only "you have 2 messages"
                SystemManager.getInstance().getClientManager().notifyAll(pack, ticker, text);
            }
            else {
                SystemManager.getInstance().getClientManager().notifyAll(pack, title, text);
            }
        }
    };

    public NotifyFactory getNotifyFactory() {
        return notifyFactory;
    }

    public void setNotifyFactory(NotifyFactory notifyFactory) {
        this.notifyFactory = notifyFactory;
    }

    public NotificationInterpretor getNotificationInterpretor() {
        return notificationInterpretor;
    }

    public void setNotificationInterpretor(NotificationInterpretor notificationInterpretor) {
        this.notificationInterpretor = notificationInterpretor;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public void setSaveManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

}

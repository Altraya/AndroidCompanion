package androidcompanion.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import androidcompanion.contact.ContactManager;
import androidcompanion.data.SaveManager;
import androidcompanion.netcode.ClientManager;
import androidcompanion.notifications.NotifService;
import androidcompanion.notifications.NotificationInterpretor;
import androidcompanion.notifications.NotifyFactory;

/**
 * System manager who manage all other Factory, Manager and Receiver
 * @author Josselin
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
    private NotificationReceiver nReceiver;
    private ContactManager contactManager;
    private ToastManager toastManager;

    //Settings
    private String deviceFileName = "device_list.json";

    //Set up function
    public void instanciate(){

        notifyFactory = new NotifyFactory();
        notificationInterpretor = new NotificationInterpretor();
        clientManager = new ClientManager();
        saveManager = new SaveManager();
        permissionManager = new PermissionManager();
        contactManager = new ContactManager();
        toastManager = new ToastManager();

        //Set notif receiver
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("androidcompanion.notifications.NOTIFICATION_EVENT");
        MyApp.getContext().registerReceiver(nReceiver,filter);

        saveManager.loadDevices();

    }


    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("NEW NOTIF !!!");

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
    }

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

    public ContactManager getContactManager() {
        return contactManager;
    }

    public void setContactManager(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    public String getDeviceFileName() {
        return deviceFileName;
    }

    public void setDeviceFileName(String deviceFileName) {
        this.deviceFileName = deviceFileName;
    }

    public ToastManager getToastManager() {
        return toastManager;
    }

    public void setToastManager(ToastManager toastManager) {
        this.toastManager = toastManager;
    }
}

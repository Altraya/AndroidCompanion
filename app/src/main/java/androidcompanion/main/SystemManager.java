package androidcompanion.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidcompanion.data.SaveManager;
import androidcompanion.netcode.Client;
import androidcompanion.netcode.ClientManager;
import androidcompanion.netcode.LocalClient;
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
            instance.checkBroadCastReceiver();
        }

        return instance;
    }

    //Instance

    private NotifyFactory notifyFactory;
    private NotificationInterpretor notificationInterpretor;
    private ClientManager clientManager;
    private SaveManager saveManager;
    private PermissionManager permissionManager;

    private Context context;

    private boolean isNotificationListenerRegistered = false;

    //Set up function
    public void instanciate(){

        notifyFactory = new NotifyFactory();
        notificationInterpretor = new NotificationInterpretor();
        clientManager = new ClientManager();
        saveManager = new SaveManager();
        permissionManager = new PermissionManager();

    }

    public void checkBroadCastReceiver(){

        if(context != null && !isNotificationListenerRegistered){
            LocalBroadcastManager.getInstance(instance.getContext()).registerReceiver(onNotice, new IntentFilter("Msg"));
            init();
        }

    }

    private void init(){
        // We copy the assets to the external storage
        SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
        boolean isFirstLaunch = settings.getBoolean("FIRST_RUN", false);
        if (!isFirstLaunch) {
            // do the thing for the first time
            // here we copy the assets to the external storage
            SystemManager.getInstance().getSaveManager().copyAssets();
            settings = context.getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        } else {
            // other time the app loads
        }

        try
        {
            // reloading of the LocalClients list && connection
            JSONObject jsonObj = new JSONObject(SystemManager.getInstance().getSaveManager().loadJSONFromAsset("device_list.json"));

            if(!jsonObj.isNull("devices"))
            {
                JSONArray devices = jsonObj.getJSONArray("devices");
                for(int i = 0; i < devices.length(); i++)
                {
                    JSONObject device = devices.getJSONObject(i);
                    String deviceIPAdress = device.getString("ip_adress");
                    String devicePort = device.getString("port");
                    String devicePairingKey = device.getString("pairing_key");
                    // Connection to the device using the infos previously provided
                    LocalClient newClient = SystemManager.getInstance().getClientManager().addClient(deviceIPAdress,Integer.parseInt(devicePort),Integer.parseInt(devicePairingKey));
                    // effective connection to the client (socket)
                    if(newClient != null){
                        newClient.connect();
                    }
                    //Toast.makeText(getApplicationContext(),"Device successfully connected!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isNotificationListenerRegistered() {
        return isNotificationListenerRegistered;
    }

    public void setNotificationListenerRegistered(boolean notificationListenerRegistered) {
        isNotificationListenerRegistered = notificationListenerRegistered;
    }
}

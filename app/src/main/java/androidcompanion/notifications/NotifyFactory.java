package androidcompanion.notifications;

import android.util.Log;

import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;

import java.util.Date;

import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;

import androidcompanion.notifications.json.BatteryState;
import androidcompanion.notifications.json.ContactList;
import androidcompanion.notifications.json.JsonObject;
import androidcompanion.notifications.json.Message;
import androidcompanion.notifications.json.Notify;

/**
 * Notification factory to react to send json message
 * @author Josselin
 * Created by Jo on 24/04/2017.
 */

public class NotifyFactory {

    public NotifyFactory(){
    }

    public void connect(LocalClient localClient){

        localClient.getClient().sendMessage(getJson(localClient,"connect",null));

    }

    public void disconnect(LocalClient localClient){

        localClient.getClient().sendMessage(getJson(localClient,"disconnect",null));

    }
    public void notifyBattery(LocalClient localClient,float percent, boolean isCharging){

        if(!localClient.getConnectionState().equals(LocalClient.ConnectionState.ACCEPTED)){
            return;
        }

        try {

            BatteryState batteryObject = new BatteryState(percent, isCharging);
            localClient.getClient().sendMessage(getJson(localClient, "BatteryState", batteryObject));

        }catch(Exception e){
            System.out.println("Could not send battery state");
        }

    }

    public void notify(LocalClient localClient,String app,String title,String text){

        System.out.println("App : " + app + " Title : " + title + " Text : " + text);

        if(!localClient.getClientSettings().isNotificationAllowed()){
            return;
        }

        if(!localClient.getClientSettings().isAuthorized(app)){
            return;
        }

        if(!localClient.getConnectionState().equals(LocalClient.ConnectionState.ACCEPTED)){
            return;
        }

        try {

            final Date d = new Date();
            Notify notifyObject = new Notify(app, title, text, d.toString());
            String jsonData = getJson(localClient, "Notification", notifyObject);
            System.out.println(jsonData);
            localClient.getClient().sendMessage(jsonData);

        }catch(Exception e){
            System.out.println("Could not send notification");
        }

    }

    public void sendContact(LocalClient localClient){

        if(!localClient.getConnectionState().equals(LocalClient.ConnectionState.ACCEPTED)){
            return;
        }

        try {

            final Date d = new Date();
            ContactList contactList = new ContactList(SystemManager.getInstance().getContactManager().getListeContacts());
            localClient.getClient().sendMessage(getJson(localClient, "contacts", contactList));

        }catch(Exception e){
            System.out.println("Could not send contacts");
        }

    }

    private String getJson(LocalClient localClient,String type,JsonObject object){

        String conn = localClient.getClient().getAddress() + ":" + localClient.getClient().getPort() + "@" + localClient.getPairingKey();
        String author = DeviceName.getDeviceName();
        Message message = new Message(type,conn,author,object);
        Gson gson = new Gson();
        return gson.toJson(message);

    }

}

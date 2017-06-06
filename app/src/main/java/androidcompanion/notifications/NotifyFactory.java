package androidcompanion.notifications;

import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;

import java.util.Date;

import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;
import androidcompanion.notifications.json.JsonObject;
import androidcompanion.notifications.json.Message;
import androidcompanion.notifications.json.Notify;

/**
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

    public void notify(LocalClient localClient,String app,String title,String text){

        final Date d = new Date();

        Notify notifyObject = new Notify(app,title,text,d.toString());

        localClient.getClient().sendMessage(getJson(localClient,"Notification",notifyObject));

    }

    private String getJson(LocalClient localClient,String type,JsonObject object){

        String conn = localClient.getClient().getAddress() + ":" + localClient.getClient().getPort() + "@" + localClient.getPairingKey();

        String author = DeviceName.getDeviceName();

        Message message = new Message(type,conn,author,object);

        Gson gson = new Gson();

        return gson.toJson(message);

    }

}

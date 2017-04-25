package androidcompanion.notifications;

import com.google.gson.Gson;

import java.util.Date;

import androidcompanion.main.MainActivity;
import androidcompanion.main.SystemManager;
import androidcompanion.notifications.json.JsonObject;
import androidcompanion.notifications.json.Message;
import androidcompanion.notifications.json.Notify;

/**
 * Created by Jo on 24/04/2017.
 */

public class NotifyFactory {

    private MainActivity main;

    public NotifyFactory(){
    }

    public void connect(){

        SystemManager.getInstance().getClient().sendMessage(getJson("connect",null));

    }

    public void notify(String app,String title,String text){

        final Date d = new Date();

        Notify notifyObject = new Notify(app,title,text,d.toString());

        SystemManager.getInstance().getClient().sendMessage(getJson("Notification",notifyObject));

    }

    private String getJson(String type,JsonObject object){

        String conn = SystemManager.getInstance().getClient().getAddress() + "@" + SystemManager.getInstance().getClient().getPort();
        String author = "MOMO-LG";

        Message message = new Message(type,conn,author,object);

        Gson gson = new Gson();

        return gson.toJson(message);

    }

}

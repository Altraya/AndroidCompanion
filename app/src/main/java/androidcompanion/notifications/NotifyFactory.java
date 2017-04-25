package androidcompanion.notifications;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Objects;

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

    public void notify(final String j_app,final String j_title,final String j_text){

        final Date d = new Date();

        Notify notifyObject = new Notify(j_app,j_title,j_text,d.toString());

        SystemManager.getInstance().getClient().sendMessage(getJson("Notification",notifyObject));

    }

    private String getJson(String j_type,JsonObject j_object){

        String conn = SystemManager.getInstance().getClient().getAddress() + "@" + SystemManager.getInstance().getClient().getPort();
        String author = "MOMO-LG";

        Message message = new Message(j_type,conn,author,j_object);

        Gson gson = new Gson();

        return gson.toJson(message);

    }

}

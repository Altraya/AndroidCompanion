package androidcompanion.notifications;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Objects;

import androidcompanion.main.MainActivity;
import androidcompanion.main.SystemManager;

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

        Object notifyObject = new Object(){

            String application = j_app;
            String title = j_title;
            String message = j_text;
            String heureDate = d.toString();

        };

        SystemManager.getInstance().getClient().sendMessage(getJson("Notification",notifyObject));

    }

    private String getJson(final String j_type,final Object j_object){

        Object jsonObj = new Object(){

            String type = j_type;
            String conn = SystemManager.getInstance().getClient().getAddress() + "@" + SystemManager.getInstance().getClient().getPort();
            String author = "MOMO-LG";
            Object object = j_object;

        };

        Gson gson = new Gson();

        return gson.toJson(jsonObj);

    }

}

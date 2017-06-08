package androidcompanion.notifications.json;

/**
 * Represent a notification (can be all notification type)
 * @author Phillippine & Josselin
 * Created by Jo on 25/04/2017.
 */
public class Notify extends JsonObject{

    String application;
    String title;
    String message;
    String heureDate;

    public Notify(String application,String title,String message,String heureDate){

        super();

        //application name from intent package like : com.google.com.app.messaging
        this.application = application;
        //title of the notification popup
        this.title = title;
        //message receive
        this.message = message;
        //the datetime when we received it
        this.heureDate = heureDate;

    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeureDate() {
        return heureDate;
    }

    public void setHeureDate(String heureDate) {
        this.heureDate = heureDate;
    }

}

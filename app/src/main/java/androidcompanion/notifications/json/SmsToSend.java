package androidcompanion.notifications.json;

/**
 * Created by Jo on 25/04/2017.
 */

public class SmsToSend extends JsonObject{

    String number;
    String message;

    public SmsToSend(String number, String message){

        super();

        this.number = number;
        this.message = message;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

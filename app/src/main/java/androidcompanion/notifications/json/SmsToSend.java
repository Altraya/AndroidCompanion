package androidcompanion.notifications.json;

import java.util.List;

/**
 * One subobject of message
 * Represent a message (sms / mms or something from a messaging app)
 * @author Phillippine & Josselin
 * Created by Jo on 25/04/2017.
 */

public class SmsToSend extends JsonObject{

    //application name from intent package like : com.google.com.app.messaging -> not used here now @todo
    String application;
    List<String> numbers;
    String message;

    public SmsToSend(String application,List<String> numbers, String message){

        super();

        this.application = application;
        this.numbers = numbers;
        this.message = message;

    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumber(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }


    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }
}

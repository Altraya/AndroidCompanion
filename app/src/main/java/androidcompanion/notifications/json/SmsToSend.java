package androidcompanion.notifications.json;

/**
 * Created by Jo on 25/04/2017.
 */

public class SmsToSend extends JsonObject{

    String [] numbers;
    String message;

    public SmsToSend(String [] numbers, String message){

        super();

        this.numbers = numbers;
        this.message = message;

    }

    public String [] getNumbers() {
        return numbers;
    }

    public void setNumber(String [] numbers) {
        this.numbers = numbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package androidcompanion.notifications.json;

/**
 * One subobject of message
 * Represent a subobject containing number information for the person we need to call
 * @author Phillippine & Josselin
 * Created by Sakina on 27/05/2017.
 */

public class NumberToCall extends JsonObject {

    //represent a number to send to the phone to call it
    String number ;

    public NumberToCall(String number){

        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {

        this.number = number;
    }


}

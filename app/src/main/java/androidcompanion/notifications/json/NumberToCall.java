package androidcompanion.notifications.json;

/**
 * Created by Sakina on 27/05/2017.
 */

public class NumberToCall extends JsonObject {
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number ;

    NumberToCall(String number){
        this.number = number;
    }


}

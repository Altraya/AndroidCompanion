package androidcompanion.notifications;

import android.telephony.SmsManager;

import com.google.gson.Gson;

import androidcompanion.netcode.LocalClient;
import androidcompanion.notifications.json.Message;
import androidcompanion.notifications.json.SmsToSend;

/**
 * Created by Jo on 28/04/2017.
 */

public class NotificationInterpretor {

    public NotificationInterpretor(){

    }

    public void interpretNotify(LocalClient source,String jsonString){

        Gson gson = new Gson();

        Message message = gson.fromJson(jsonString, Message.class);

        switch (message.getType()){
            case "smsToSend" : interpretSmsToSend(message); break;
            case "disconnectionAcknowledged" : interpretDisconnectionConfirmation(source);
        }

    }

    private void interpretSmsToSend(Message message){

        if(message.getObject() == null) return;

        SmsToSend smsToSend = (SmsToSend) message.getObject();

        SmsManager smsManager = SmsManager.getDefault();

        String[] numbers = smsToSend.getNumbers();

        for(int i = 0; i < numbers.length; i++){

            smsManager.sendTextMessage(numbers[i], null, smsToSend.getMessage(), null, null);

        }

    }

    private void interpretDisconnectionConfirmation(LocalClient source){

        source.disconnect();

    }

}

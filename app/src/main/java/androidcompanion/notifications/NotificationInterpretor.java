package androidcompanion.notifications;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.gson.Gson;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.netcode.LocalClient;
import androidcompanion.notifications.json.Message;
import androidcompanion.notifications.json.NumberToCall;
import androidcompanion.notifications.json.SmsToSend;

/**
 * Created by Jo on 28/04/2017.
 */

public class NotificationInterpretor {

    public NotificationInterpretor() {

    }

    public void interpretNotify(LocalClient source,String jsonString){

        Gson gson = new Gson();

        Message message = gson.fromJson(jsonString, Message.class);

        switch (message.getType()){
            case "smsToSend" :
                System.out.println("Will send a message "+message);
                interpretSmsToSend(message);
                break;
            case "askCall" :
                System.out.println("Will send a call order "+message);
                interpretNumberToCall(message);
                break;
            case "disconnectionAcknowledged" :
                System.out.println("The client "+source.toString()+" will be diconnected");
                interpretDisconnectionConfirmation(source);
                break;
            default:
                break;
        }

    }

    private void interpretSmsToSend(Message message){

        System.out.println(message);
        if(message.getObject() == null) return;

        SmsToSend smsToSend = (SmsToSend) message.getObject();
        System.out.println(smsToSend);
        SmsManager smsManager = SmsManager.getDefault();

        String[] numbers = smsToSend.getNumbers();

        for(int i = 0; i < numbers.length; i++){

            smsManager.sendTextMessage(numbers[i], null, smsToSend.getMessage(), null, null);

        }

    }

    private void interpretNumberToCall(Message message){

        if(message.getObject() == null) return;
        NumberToCall numberToCall = (NumberToCall) message.getObject();
        String number = numberToCall.getNumber();
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));
            Context currentContext = MyApp.getInstance().getContext();
            if (ActivityCompat.checkSelfPermission(currentContext,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            currentContext.startActivity(callIntent);

        } catch (ActivityNotFoundException e) {
            Log.e("error call", "Call failed", e);
        }



    }


    private void interpretDisconnectionConfirmation(LocalClient source){

        source.disconnect();

    }

}

package androidcompanion.notifications;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.main.ToastManager;
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

        System.out.println("Miaou");
        System.out.println(jsonString);
        Gson gson = new Gson();
        System.out.println("waffle blue");
        SmsToSend message = gson.fromJson(jsonString, SmsToSend.class);

        System.out.println(message.getType());

        switch (message.getType()){
            case "smsToSend" :
                System.out.println("Will send a message "+message);
                interpretSmsToSend(message);
                break;
            case "askCall" :
                System.out.println("Will send a call order "+message);
                //interpretNumberToCall(message);
                break;
            case "disconnectionAcknowledged" :
                System.out.println("The client "+source.toString()+" will be diconnected");
                interpretDisconnectionConfirmation(source);
                break;
            default:
                break;
        }

    }

    private void interpretSmsToSend(SmsToSend message){

        System.out.println(message);

        SmsManager smsManager = SmsManager.getDefault();

        List<String> numbers = message.getNumbers();

        for(int i = 0; i < numbers.size(); i++){

            System.out.println("Sending to : " + message.getNumbers().get(i));

            sendSMS(message.getNumbers().get(i), message.getMessage());

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

    private void sendSMS(String  phoneNumber, String  message)
    {
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            ToastManager.makeToast("You send a message to "+phoneNumber);
        }catch (Exception e)
        {
            Log.e("Error",e.toString());
        }
    }

}

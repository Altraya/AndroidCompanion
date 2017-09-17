package androidcompanion.netcode;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;

import androidcompanion.device.DeviceInformationActivity;
import androidcompanion.device.DeviceListingActivity;
import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.main.ToastManager;
import project.androidcompanion.R;

/**
 * Created by Jo on 28/04/2017.
 */

public class LocalClient {

    private Client client;
    private LocalClient thisObj;
    private int pairingKey;

    public LocalClient(String address,int port, int pairingKey){

        thisObj = this;

        client = new Client(address,port);

        this.pairingKey = pairingKey;

        client.addClientEventListener(new ClientEvent.ClientEventListener() {
            @Override
            public void connectedEvent(ClientEvent event) {
                //Sends connection message to the server
                SystemManager.getInstance().getNotifyFactory().connect(thisObj);
                thisObj.getClient().setActive(true);
                ((DeviceListingActivity)(DeviceListingActivity.context)).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Reset the view cache by resetting the ListView adapter
                        DeviceListingActivity.listView.setAdapter(DeviceListingActivity.listView.getAdapter());
                    }
                });
                ToastManager.makeToast("Connexion établie");
            }

            @Override
            public void messageReceivedEvent(ClientEvent event, String message) {
                System.out.println("Message reçu du serveur " + message);
                //Interprets incoming json string
                ToastManager.makeToast("Message reçu du serveur" + message);
                SystemManager.getInstance().getNotificationInterpretor().interpretNotify(thisObj,message);
            }

            @Override
            public void disconnectedEvent(ClientEvent event) {

                try {
                    SystemManager.getInstance().getClientManager().getClients().remove(thisObj);
                    getClient().setActive(false);
                    ((DeviceListingActivity)(DeviceListingActivity.context)).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Reset the view cache by resetting the ListView adapter
                            DeviceListingActivity.listView.setAdapter(DeviceListingActivity.listView.getAdapter());
                        }
                    });
                    ToastManager.makeToast("Appareil deconnecté");
                }catch (Exception e)
                {
                    Log.e("Error", e.toString());
                }

                // The following instruction causes the app to crash. Due to thread issue?
                /*DeviceListingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SystemManager.getInstance().getSaveManager().loadConnectedDevices(DeviceListingActivity.deviceAdapter);
                    }
                });*/
                /*new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                            @Override
                            public void run() {
                                SystemManager.getInstance().getSaveManager().loadConnectedDevices(DeviceListingActivity.deviceAdapter);
                            }
                        });
                    }
                }).start();*/
            }
        });

    }

    public void connect(){

        client.connect();

    }

    public void disconnect(){

        client.disconnect();

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getPairingKey() {
        return pairingKey;
    }

    public void setPairingKey(int pairingKey) {
        this.pairingKey = pairingKey;
    }
}

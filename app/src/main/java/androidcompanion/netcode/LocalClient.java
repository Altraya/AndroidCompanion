package androidcompanion.netcode;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import java.util.UUID;

import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;
import androidcompanion.main.ToastManager;

/**
 * Created by Jo on 28/04/2017.
 */

public class LocalClient {

    private Client client;
    private LocalClient thisObj;

    private ClientSettings clientSettings;

    private int pairingKey;
    private UUID uid;

    public LocalClient(String address,int port, int pairingKey){

        uid = UUID.randomUUID();

        thisObj = this;

        client = new Client(address,port);

        clientSettings = new ClientSettings();

        this.pairingKey = pairingKey;

        client.addClientEventListener(new ClientEvent.ClientEventListener() {
            @Override
            public void connectedEvent(ClientEvent event) {
                //Sends connection message to the server
                System.out.println("Connected to " + client.getAddress());
                ToastManager.makeToast("Connexion établie");
                SystemManager.getInstance().getNotifyFactory().connect(thisObj);

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = MyApp.getContext().registerReceiver(null, ifilter);
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                //int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                //float batteryPct = level / (float)scale;
                float batteryPct = level;
                boolean isCharging = true;
                SystemManager.getInstance().getNotifyFactory().notifyBattery(thisObj,batteryPct, isCharging);
            }

            @Override
            public void messageReceivedEvent(ClientEvent event, String message) {
                System.out.println("Message recu du serveur "+message);
                //Interprets incomming json string
                ToastManager.makeToast("Message reçu du serveur");
                SystemManager.getInstance().getNotificationInterpretor().interpretNotify(thisObj,message);
            }

            @Override
            public void disconnectedEvent(ClientEvent event) {

                System.out.println("Disconnected : " + client.getAddress() );

                try {
                    SystemManager.getInstance().getClientManager().getClients().remove(thisObj);
                    // removing device settings
                    SystemManager.getInstance().getDeviceSettingsManager().removeDeviceSetting(Integer.toString(thisObj.getPairingKey()));
                    // TODO Remove from file and adapter when click on disconnect button only. Other than that just disable disconnect button and allow re-connection somehow
                    SystemManager.getInstance().getSaveManager().removeDeviceFromJsonFile("device_list.json",
                            thisObj.getClient().getAddress(),
                            Integer.toString(thisObj.getClient().getPort()),
                            Integer.toString(thisObj.getPairingKey()));
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

    public ClientSettings getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(ClientSettings clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getUid() {
        return uid.toString();
    }

    public void setUid(String uid) {
        this.uid = UUID.fromString(uid);
    }
}

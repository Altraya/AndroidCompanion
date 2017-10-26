package androidcompanion.netcode;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Looper;
import android.util.Log;

import java.util.UUID;

import androidcompanion.device.DeviceListingActivity;
import androidcompanion.main.MyApp;
import androidcompanion.main.SystemManager;


/**
 * Created by Jo on 28/04/2017.
 */

public class LocalClient {

    private Client client;
    private LocalClient thisObj;
    private ConnectionState connectionState;
    private ClientSettings clientSettings;

    private int pairingKey;
    private UUID uid;

    public LocalClient(String address,int port, int pairingKey){

        uid = UUID.randomUUID();

        thisObj = this;

        client = new Client(address,port);

        setConnectionState(ConnectionState.PENDING);

        clientSettings = new ClientSettings();

        this.pairingKey = pairingKey;

        client.addClientEventListener(new ClientEvent.ClientEventListener() {
            @Override
            public void connectedEvent(ClientEvent event) {
                //Sends connection message to the server
                System.out.println("Connected to " + client.getAddress());
                SystemManager.getInstance().getToastManager().makeToast("Connexion établie");
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
                SystemManager.getInstance().getToastManager().makeToast("Message reçu du serveur");
                SystemManager.getInstance().getNotificationInterpretor().interpretNotify(thisObj,message);
            }

            @Override
            public void disconnectedEvent(ClientEvent event) {

                System.out.println("Disconnected : " + client.getAddress() );

                try {
                    SystemManager.getInstance().getToastManager().makeToast("Appareil deconnecté");
                }catch (Exception e)
                {
                    Log.e("Error", e.toString());
                }

            }
        });

    }

    public void connect(){

        client.connect();

    }

    public void disconnect(){

        client.disconnect();

    }

    public void remove(){

        client.disconnect();
        SystemManager.getInstance().getClientManager().getClients().remove(this);
        SystemManager.getInstance().getSaveManager().saveDevices();
        DeviceListingActivity.refreshListview();

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

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(final ConnectionState connectionState) {
        this.connectionState = connectionState;

        switch (connectionState) {
            case ACCEPTED:
                break;
            case PENDING:
                // TODO le Timer de 10 secondes avant de passer en refused
                android.os.Handler UIHandler = new android.os.Handler(Looper.getMainLooper());
                UIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (getConnectionState() == ConnectionState.PENDING) {
                            SystemManager.getInstance().getToastManager().makeToast("Délai d'attente dépassé");
                            setConnectionState(ConnectionState.REFUSED);
                        }
                    }
                }, 10000);
               /* new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (getConnectionState() == ConnectionState.PENDING) {
                            SystemManager.getInstance().getToastManager().makeToast("Délai d'attente dépassé");
                            setConnectionState(ConnectionState.REFUSED);
                        }
                    }
                }, 10000);
               */

                break;
            case REFUSED:
                this.disconnect();
                break;
        }
        DeviceListingActivity.refreshListview();
    }

    public enum ConnectionState {
        ACCEPTED,
        PENDING,
        REFUSED
    }
}

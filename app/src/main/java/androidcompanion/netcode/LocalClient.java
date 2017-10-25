package androidcompanion.netcode;

import android.util.Log;

import java.util.UUID;

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
                SystemManager.getInstance().getToastManager().makeToast("Connexion établie");
                SystemManager.getInstance().getNotifyFactory().connect(thisObj);
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

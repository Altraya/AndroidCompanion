package androidcompanion.netcode;

import androidcompanion.main.SystemManager;

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
            }

            @Override
            public void messageReceivedEvent(ClientEvent event, String message) {
                //Interprets incomming json string
                SystemManager.getInstance().getNotificationInterpretor().interpretNotify(message);
            }

            @Override
            public void disconnectedEvent(ClientEvent event) {

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
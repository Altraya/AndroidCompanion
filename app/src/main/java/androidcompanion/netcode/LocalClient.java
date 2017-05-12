package androidcompanion.netcode;

import androidcompanion.main.SystemManager;

/**
 * Created by Jo on 28/04/2017.
 */

public class LocalClient {

    private Client client;

    public LocalClient(String address,int port){

        client = new Client(address,port);

        client.addClientEventListener(new ClientEvent.ClientEventListener() {
            @Override
            public void connectedEvent(ClientEvent event) {
                //Sends connection message to the server
                SystemManager.getInstance().getNotifyFactory().connect();
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
}

package androidcompanion.netcode;

import android.location.Address;

import java.util.ArrayList;

import androidcompanion.main.SystemManager;

/**
 * Created by Jo on 28/04/2017.
 */

public class ClientManager {

    private ArrayList<LocalClient> clients = new ArrayList<LocalClient>();

    public  ClientManager(){

    }

    public void notifyAll(String pack,String title,String text){

        for(int i = 0; i < clients.size(); i++){

            SystemManager.getInstance().getNotifyFactory().notify(clients.get(i),pack, title, text);

        }

    }

    public LocalClient addClient(String address, int port, int pairingKey){

        LocalClient localClient = new LocalClient(address,port, pairingKey);

        clients.add(localClient);

        return localClient;

    }

    public ArrayList<LocalClient> getClients() {
        return clients;
    }

    public void setClients(ArrayList<LocalClient> clients) {
        this.clients = clients;
    }

}

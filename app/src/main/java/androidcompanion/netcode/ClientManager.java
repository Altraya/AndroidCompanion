package androidcompanion.netcode;

import android.location.Address;

import java.util.ArrayList;

/**
 * Created by Jo on 28/04/2017.
 */

public class ClientManager {

    private ArrayList<LocalClient> clients = new ArrayList<LocalClient>();

    public  ClientManager(){



    }

    public void addClient(String address, int port){

        clients.add(new LocalClient(address,port));

    }

    public ArrayList<LocalClient> getClients() {
        return clients;
    }

    public void setClients(ArrayList<LocalClient> clients) {
        this.clients = clients;
    }

}

package androidcompanion.netcode;

import android.location.Address;

import java.util.ArrayList;

import androidcompanion.main.SystemManager;
import androidcompanion.main.ToastManager;

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

    //send battery notification
    public void notifyBattery(float pourcent, boolean isCharging){

        for(int i = 0; i < clients.size(); i++){

            SystemManager.getInstance().getNotifyFactory().notifyBattery(clients.get(i),pourcent, isCharging);

        }

    }

    public LocalClient addClient(String address, int port, int pairingKey){

        cleanup();

        for(int i = 0; i < clients.size(); i++){

            if(clients.get(i).getClient().getAddress().equals(address) && clients.get(i).getClient().getPort() == port){

                ToastManager.makeToast("Already connected");

                return null;

            }

        }

        LocalClient localClient = new LocalClient(address,port, pairingKey);

        clients.add(localClient);

        return localClient;

    }

    public void cleanup(){

        for(int i = 0; i < clients.size(); i++){

            if(!clients.get(i).getClient().isActive()){

                clients.remove(i);

                i--;

            }

        }

    }

    public ArrayList<LocalClient> getClients() {
        return clients;
    }

    public void setClients(ArrayList<LocalClient> clients) {
        this.clients = clients;
    }

}

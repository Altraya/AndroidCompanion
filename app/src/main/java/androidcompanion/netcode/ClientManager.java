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

        System.out.println("Content : ");

        for(int i = 0; i < clients.size(); i++){

            System.out.println(i + " : " + clients.get(i).getClient().getAddress() + " " + clients.get(i).getClient().getPort());

        }

        for(int i = 0; i < clients.size(); i++){

            System.out.println("NOTIFYING " + clients.get(i).getClient().getAddress());

            SystemManager.getInstance().getNotifyFactory().notify(clients.get(i),pack, title, text);

        }

    }

    public LocalClient addClient(String address, int port, int pairingKey){

        System.out.println("Content : ");

        for(int i = 0; i < clients.size(); i++){

            System.out.println(i + " : " + clients.get(i).getClient().getAddress() + " " + clients.get(i).getClient().getPort());

        }

        cleanup();

        for(int i = 0; i < clients.size(); i++){

            if(clients.get(i).getClient().getAddress().equals(address) && clients.get(i).getClient().getPort() == port){

                ToastManager.makeToast("Déjà connecté");

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

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

        System.out.println("Notify all : " + text);

        System.out.println("Clients : " + clients.size());

        for(int i = 0; i < clients.size(); i++){

            System.out.println("Notify : " + clients.get(i).getClient().getAddress());

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

        for(int i = 0; i < clients.size(); i++){

            if(clients.get(i).getClient().getAddress().equals(address) && clients.get(i).getClient().getPort() == port){

                if(!clients.get(i).getClient().isActive()){
                    clients.get(i).connect();
                    SystemManager.getInstance().getToastManager().makeToast("Reconnected");
                }else{
                    SystemManager.getInstance().getToastManager().makeToast("Already connected");
                }

                return null;

            }

        }

        LocalClient localClient = new LocalClient(address,port, pairingKey);

        clients.add(localClient);
        SystemManager.getInstance().getSaveManager().saveDevices();

        return localClient;

    }

    public LocalClient getClientByUid(String uid){
        for(int i = 0; i < clients.size(); i ++){
            if(clients.get(i).getUid().toString().equals(uid)){
                return clients.get(i);
            }
        }
        return null;
    }

    public ArrayList<LocalClient> getClients() {
        return clients;
    }

    public void setClients(ArrayList<LocalClient> clients) {
        this.clients = clients;
    }

}

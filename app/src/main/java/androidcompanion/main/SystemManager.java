package androidcompanion.main;

import androidcompanion.netcode.Client;
import androidcompanion.notifications.NotifyFactory;

/**
 * Created by Jo on 24/04/2017.
 */

public class SystemManager {

    //Singleton

    private static SystemManager instance = null;
    protected SystemManager() {
        // Exists only to defeat instantiation.
    }
    public static SystemManager getInstance() {
        if(instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    //Instance

    private NotifyFactory notifyFactory;
    private Client client;

    public NotifyFactory getNotifyFactory() {
        return notifyFactory;
    }

    public void setNotifyFactory(NotifyFactory notifyFactory) {
        this.notifyFactory = notifyFactory;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}

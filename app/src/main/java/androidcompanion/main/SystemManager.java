package androidcompanion.main;

import androidcompanion.netcode.Client;
import androidcompanion.netcode.ClientManager;
import androidcompanion.notifications.NotificationInterpretor;
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
    //A virer !!!
    private Client client;
    private NotificationInterpretor notificationInterpretor;
    private ClientManager clientManager;

    //Set up function
    public void instanciate(){

        notifyFactory = new NotifyFactory();
        notificationInterpretor = new NotificationInterpretor();
        clientManager = new ClientManager();

    }

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

    public NotificationInterpretor getNotificationInterpretor() {
        return notificationInterpretor;
    }

    public void setNotificationInterpretor(NotificationInterpretor notificationInterpretor) {
        this.notificationInterpretor = notificationInterpretor;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }
}

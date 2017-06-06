package androidcompanion.main;

import androidcompanion.data.SaveManager;
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
            instance.instanciate();
        }
        return instance;
    }

    //Instance

    private NotifyFactory notifyFactory;
    private NotificationInterpretor notificationInterpretor;
    private ClientManager clientManager;
    private SaveManager saveManager;
    private PermissionManager permissionManager;

    //Set up function
    public void instanciate(){

        notifyFactory = new NotifyFactory();
        notificationInterpretor = new NotificationInterpretor();
        clientManager = new ClientManager();
        saveManager = new SaveManager();
        permissionManager = new PermissionManager();

    }

    public NotifyFactory getNotifyFactory() {
        return notifyFactory;
    }

    public void setNotifyFactory(NotifyFactory notifyFactory) {
        this.notifyFactory = notifyFactory;
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

    public SaveManager getSaveManager() {
        return saveManager;
    }

    public void setSaveManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}

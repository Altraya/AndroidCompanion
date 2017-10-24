package androidcompanion.netcode;

import java.util.ArrayList;

/**
 * Created by Jo on 24/10/2017.
 */

public class ClientSettings {

    private ArrayList<String> bannedPackages = new ArrayList<>();

    private boolean notificationAllowed = true;

    public ClientSettings(){

    }

    public void ban(String app){
        if(isAuthorized(app)){
            bannedPackages.add(app);
        }
    }

    public void unban(String app){
        if(!isAuthorized(app)){
            bannedPackages.remove(app);
        }
    }

    public void reset(){

        bannedPackages.clear();

    }

    //Check that the app is authorized to send notifications to the server
    public boolean isAuthorized(String app){

        for(int i = 0; i < bannedPackages.size(); i++){
            if(bannedPackages.get(i).equals(app)){
                return false;
            }
        }

        return true;

    }

    public ArrayList<String> getBannedPackages() {
        return bannedPackages;
    }

    public void setBannedPackages(ArrayList<String> bannedPackages) {
        this.bannedPackages = bannedPackages;
    }

    public boolean isNotificationAllowed() {
        return notificationAllowed;
    }

    public void setNotificationAllowed(boolean notificationAllowed) {
        this.notificationAllowed = notificationAllowed;
    }
}

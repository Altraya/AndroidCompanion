package androidcompanion.notifications.json;

/**
 * Created by Sakina on 15/09/2017.
 */

public class BatteryState extends JsonObject{
    private float pourcent;
    private boolean isCharging;

    public BatteryState(float pourcent, boolean isCharging) {
        this.pourcent = pourcent;
        this.isCharging = isCharging;
    }
    public float getPourcent() {
        return pourcent;
    }

    public void setPourcent(float pourcent) {
        this.pourcent = pourcent;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }


}

package androidcompanion.notifications.json;

/**
 * Created by Sakina on 15/09/2017.
 */

public class BatteryState extends JsonObject{
    private float percent;
    private boolean isCharging;

    public BatteryState(float pourcent, boolean isCharging) {
        this.percent = pourcent;
        this.isCharging = isCharging;
    }
    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }


}

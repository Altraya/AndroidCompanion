package androidcompanion.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by dmarck on 12/05/2017.
 * Class to get context of the application exerywhere
 */

// singleton
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}

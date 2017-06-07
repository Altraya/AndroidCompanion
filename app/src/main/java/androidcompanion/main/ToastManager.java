package androidcompanion.main;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by Maxime on 01/06/2017.
 */

public class ToastManager {
    public static Handler UIHandler = new Handler(Looper.getMainLooper());

    public static void makeToast(final String toastMessage) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SystemManager.getInstance().getContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

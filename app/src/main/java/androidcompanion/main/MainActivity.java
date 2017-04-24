package androidcompanion.main;


import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidcompanion.netcode.Client;
import androidcompanion.netcode.ClientEvent;
import androidcompanion.notifications.NotifyFactory;
import project.androidcompanion.R;


public class MainActivity extends Activity {

    TableLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchnotification);
        tab = (TableLayout)findViewById(R.id.tab);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        //Create the notifyFactory
        SystemManager.getInstance().setNotifyFactory(new NotifyFactory());

        //Connects the client
        SystemManager.getInstance().setClient(new Client("192.168.43.223", 4444));
        SystemManager.getInstance().getClient().addClientEventListener(new ClientEvent.ClientEventListener() {
            @Override
            public void connectedEvent(ClientEvent event) {
                System.out.println("Connexion établie");
                SystemManager.getInstance().getNotifyFactory().connect();
            }

            @Override
            public void messageReceivedEvent(ClientEvent event, String message) {
                System.out.println("Message reçu : " + message);
            }

            @Override
            public void disconnectedEvent(ClientEvent event) {
                System.out.println("Déconnexion");
            }
        });

        SystemManager.getInstance().getClient().connect();

    }


    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            TableRow tr = new TableRow(getApplicationContext());
            tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#0B0719"));
            textview.setText(Html.fromHtml(pack +"<br><b>" + title + " : </b>" + text));
            tr.addView(textview);
            tab.addView(tr);

        }
    };
}
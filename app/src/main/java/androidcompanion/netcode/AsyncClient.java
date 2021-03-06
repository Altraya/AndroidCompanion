package androidcompanion.netcode;

import android.os.AsyncTask;

/**
 * Created by Maxime & Josselin on 24/04/2017.
 */
public class AsyncClient extends AsyncTask<String, String, TCPClient> {

    private TCPClient tcpClient = null;

    public AsyncClient (TCPClient tcpClient){

        this.tcpClient = tcpClient;

    }

    @Override
    protected TCPClient doInBackground(String... message) {

        //we create a TCPClient object and

        if(tcpClient == null)return null;

        tcpClient.run();

        return tcpClient;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        //return values[];

    }
}

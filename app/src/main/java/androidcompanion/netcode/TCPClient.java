package androidcompanion.netcode;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

    private String serverMessage;
    private boolean mRun = false;

    private Client client;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(Client client) {
        this.client = client;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message + "<Client Quit>");
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {

            //-------------------------La création du socket
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(client.getAddress());

            //Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, client.getPort());

            try {

                //---------------------- On peut réadapter à cet endroit pour l'envoi/la réception de données, le code ci-dessous a servit pour le chat-------------------------

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                client.getClientEventManager().fireConnectedEvent();

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    serverMessage = in.readLine();

                    if (serverMessage != null) {

                        client.getClientEventManager().fireMessageReceivedEvent(serverMessage);

                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                // Log.e("TCP", "S: Error", e);

            } finally {

                terminateConnection();

            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    public void terminateConnection(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getClientEventManager().fireDisconnectedEvent();
    }

}

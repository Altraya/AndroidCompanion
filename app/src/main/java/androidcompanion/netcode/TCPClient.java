package androidcompanion.netcode;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
    public void sendMessage(final String message){
        System.out.println("TO SEND : " + message);
        if (out != null && !out.checkError()) {

            //TODO in async
            System.out.println("DIB execution for : " + message);

            try {

                out.println(message);
                out.flush();

            }catch (Exception e) {

                e.printStackTrace();

            }
            //TODO end of async part

            Thread t = new Thread(new Runnable() {
                public void run(){
                    System.out.println("TEST TEST TEST TEST TEST TEST TEST TEST TEST");
                }
            });

            t.start();

            System.out.println("confirm order given");
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

            System.out.println("Init connection");

            //create a socket to make the connection with the server
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddr, client.getPort()),5000);

            System.out.println("Socket created");

            try {

                //---------------------- On peut réadapter à cet endroit pour l'envoi/la réception de données, le code ci-dessous a servit pour le chat-------------------------

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                System.out.println("PrintWriter ready");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("StreamReader ready");

                client.getClientEventManager().fireConnectedEvent();

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    System.out.println("Listening...");

                    serverMessage = in.readLine();

                    Log.e("ASYNC READ", serverMessage);

                    if (serverMessage != null) {
                        //call the method messageReceived from MyActivity class
                        client.getClientEventManager().fireMessageReceivedEvent(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                terminateConnection();

            }

        } catch (Exception e) {

            e.printStackTrace();
            terminateConnection();

        }

    }

    public void terminateConnection(){

        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        client.getClientEventManager().fireDisconnectedEvent();
    }

}

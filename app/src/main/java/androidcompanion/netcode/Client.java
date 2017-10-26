package androidcompanion.netcode;

/**
 * Created by Maxime & Josselin on 24/04/2017.
 */
public class Client {

    private ClientEventManager clientEventManager;

    private AsyncClient asyncClient;
    private TCPClient tcpClient;

    private String address;
    private int port;

    private boolean isActive = false;

    public Client(String address, int port){

        this.address = address;
        this.port = port;

        clientEventManager = new ClientEventManager();

    }

    public void connect(){

        if(isActive()){
            return;
        }

        isActive = true;

        tcpClient = new TCPClient(this);

        asyncClient = new AsyncClient(tcpClient);

        asyncClient.execute();

    }

    public void disconnect(){

        if(!isActive()){
            return;
        }

        tcpClient.terminateConnection();

        asyncClient.cancel(true);

    }

    public void sendMessage(String data){

        try{

            tcpClient.sendMessage(data);

            System.out.println("SENDING DATA TO REMOTE : " + data);

        }catch(Exception e){

            System.out.println("Failed to send data to server");

        }

    }

    public synchronized  void addClientEventListener(ClientEvent.ClientEventListener l){

        clientEventManager.getListeners().add(l);

    }

    public synchronized  void removeClientEventListener(ClientEvent.ClientEventListener l){

        clientEventManager.getListeners().remove(l);

    }

    public AsyncClient getAsyncClient() {
        return asyncClient;
    }

    public void setAsyncClient(AsyncClient asyncClient) {
        this.asyncClient = asyncClient;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TCPClient getTcpClient() {
        return tcpClient;
    }

    public void setTcpClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public ClientEventManager getClientEventManager() {
        return clientEventManager;
    }

    public void setClientEventManager(ClientEventManager clientEventManager) {
        this.clientEventManager = clientEventManager;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDeviceId() {
        return address + ':' + port;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

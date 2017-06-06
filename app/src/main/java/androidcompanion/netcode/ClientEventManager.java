package androidcompanion.netcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maxime & Josselin on 24/04/2017.
 */
public class ClientEventManager {

    private List listeners = new ArrayList();

    public ClientEventManager(){



    }

    public synchronized void fireConnectedEvent(){

        ClientEvent event = new ClientEvent(this);

        Iterator listenerIt = listeners.iterator();

        while(listenerIt.hasNext()){

            ((ClientEvent.ClientEventListener) listenerIt.next()).connectedEvent(event);

        }

    }

    public synchronized void fireMessageReceivedEvent(String message){

        ClientEvent event = new ClientEvent(this);

        Iterator listenerIt = listeners.iterator();

        while(listenerIt.hasNext()){

            ((ClientEvent.ClientEventListener) listenerIt.next()).messageReceivedEvent(event, message);

        }

    }

    public synchronized void fireDisconnectedEvent(){

        ClientEvent event = new ClientEvent(this);

        Iterator listenerIt = listeners.iterator();

        while(listenerIt.hasNext()){

            ((ClientEvent.ClientEventListener) listenerIt.next()).disconnectedEvent(event);

        }

    }

    public List getListeners() {
        return listeners;
    }

    public void setListeners(List listeners) {
        this.listeners = listeners;
    }
}

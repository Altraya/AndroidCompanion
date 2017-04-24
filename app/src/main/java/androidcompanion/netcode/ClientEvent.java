package androidcompanion.netcode;

import java.util.EventObject;

/**
 * Created by Maxime & Josselin on 24/04/2017.
 */
public class ClientEvent extends EventObject{

    public ClientEvent(Object source){
        super(source);
    }

    public interface ClientEventListener{

        void connectedEvent(ClientEvent event);
        void messageReceivedEvent(ClientEvent event, String message);
        void disconnectedEvent(ClientEvent event);

    }

}

package androidcompanion.notifications.json;

/**
 * Created by Jo on 25/04/2017.
 */

public class Message extends JsonObject{

    String type;
    String conn;
    String author;
    Object object;

    public Message(String type,String conn,String author,Object object){

        super();

        this.type = type;
        this.conn = conn;
        this.author = author;
        this.object = object;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}

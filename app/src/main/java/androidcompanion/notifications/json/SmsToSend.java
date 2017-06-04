package androidcompanion.notifications.json;

import java.util.List;

/**
 * Created by Jo on 25/04/2017.
 */

public class SmsToSend extends JsonObject{

    String type;
    String conn;
    String author;
    String application;
    List<String> numbers;
    String message;

    public SmsToSend(String type,String conn,String author,String application,List<String> numbers, String message){

        super();

        this.type = type;
        this.conn = conn;
        this.author = author;
        this.application = application;
        this.numbers = numbers;
        this.message = message;

    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumber(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
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

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }
}

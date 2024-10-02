package feeltheagi.springai.model;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Message {

    private String author;
    private String receiverName;
    private String text;
    private Timestamp time = Timestamp.valueOf(LocalDateTime.now());

    public Message(String author, String receiverName, String text) {
        this.author = author.toLowerCase();
        this.receiverName = receiverName.toLowerCase();
        this.text = text;
    }
}

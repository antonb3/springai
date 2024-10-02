package feeltheagi.springai.repo;

import feeltheagi.springai.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class MessageRepoImpl implements MessageRepo {

    private List<Message> all = new ArrayList<>();

    public List<Message> getAll() {
        return all;
    }

    public List<Message> getAllByChat(String chat) {
        return all.stream()
                .filter(message -> chat.equals(message.getReceiverName().toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Message> getAllByAuthor(String author) {
        return all.stream()
                .filter(message -> author.equals(message.getAuthor().toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Message> getAllByReceiverName(String address) {
        return all.stream()
                .filter(
                        message ->
                                address.toLowerCase().equals(message.getReceiverName().toLowerCase())
                                || address.toLowerCase().equals("general_chat")
                )
                .collect(Collectors.toList());
    }

    public void add(Message message) {
        this.all.add(message);
        log.info(message.getAuthor() + " send to " + message.getReceiverName() + ": " + message.getText());
    }
}

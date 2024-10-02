package feeltheagi.springai.repo;

import feeltheagi.springai.model.Message;

import java.util.List;

public interface MessageRepo {

    public List<Message> getAll();

    public List<Message> getAllByChat(String chat);

    public List<Message> getAllByAuthor(String author);

    public List<Message> getAllByReceiverName(String receiverName);

    public void add(Message message);
}

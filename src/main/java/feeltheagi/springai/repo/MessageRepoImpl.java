package feeltheagi.springai.repo;

import feeltheagi.springai.model.Agent;
import feeltheagi.springai.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class MessageRepoImpl implements MessageRepo {

    @Autowired
    private AgentRepo agentRepo;

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

    public List<Message> getAllByReceiverName(String receiverName) {
        return all.stream()
                .filter(
                        message ->
                                receiverName.toLowerCase().equals(message.getReceiverName().toLowerCase())
                                || receiverName.toLowerCase().equals("general_chat")
                )
                .collect(Collectors.toList());
    }

    public String getFullprompt(String receiverName) {
        List<Message> messagesToPrompt = getAllByReceiverName(receiverName);
        Agent receiver = agentRepo.getOneByName(receiverName);
        String prompt = receiver.getSystemPrompt() + "\n";
        for (Message m : messagesToPrompt) {
            String chat = "direct_chat";
            if ("general_chat".equalsIgnoreCase(m.getReceiverName())) {
                chat = "general_chat";
            }
            prompt += "["+chat+"] " + m.getAuthor() + ": '" + m.getText() + "'\n";
        }
        return prompt;
    }

    public void add(Message message) {
        this.all.add(message);
        log.info(message.getAuthor() + " send to " + message.getReceiverName() + ": " + message.getText());
    }
}

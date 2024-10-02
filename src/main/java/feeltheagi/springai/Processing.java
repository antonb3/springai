package feeltheagi.springai;

import feeltheagi.springai.model.Agent;
import feeltheagi.springai.model.Message;
import feeltheagi.springai.repo.AgentRepo;
import feeltheagi.springai.repo.MessageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class Processing {

    @Autowired
    MessageRepo messageRepo;
    @Autowired
    AgentRepo agentRepo;

    private Pattern pattern = Pattern.compile("@(\\S+)\\s+([^@]+)");

    public void run() {
        try {
            Agent user = agentRepo.getOneByName("user");
            String userResponse = user.sendToAgent(user.getSystemPrompt());
            process(user.getName(), userResponse);
        } catch (Exception e) {
            Utils.logException(e);
        }
    }

    public String process(String name, String data) {
        try {
            if ("user".equalsIgnoreCase(name) && "exit".equalsIgnoreCase(data)) {
                exit();
            }
            Matcher matcher = pattern.matcher(data);

            while (matcher.find()) {
                String address = matcher.group(1);
                String text = matcher.group(2).trim();
                messageRepo.add(new Message(name, address, text));
                String chat = "direct_chat";
                List<Agent> receivers = new ArrayList<>();

                if ("general_chat".equalsIgnoreCase(address)) {
                    chat = "general_chat";
                    receivers.addAll(agentRepo.getAll());
                } else {
                    receivers.add(agentRepo.getOneByName(address));
                }

                for (Agent receiver : receivers) {
                    if (!name.equalsIgnoreCase(receiver.getName())) {
                        List<Message> messagesToPrompt = messageRepo.getAllByReceiverName(receiver.getName());
                        String prompt = receiver.getSystemPrompt() + "\n";
                        for (Message m : messagesToPrompt) {
                            prompt += "["+chat+"] " + m.getAuthor() + ": '" + m.getText() + "'\n";
                        }
                        String response = receiver.sendToAgent(prompt);
                        process(receiver.getName(), response);
                    }
                }
            }
        } catch (Exception e) {
            Utils.logException(e);
        }
        return "";
    }

    public void exit() {
        System.out.println("End");
    }
}

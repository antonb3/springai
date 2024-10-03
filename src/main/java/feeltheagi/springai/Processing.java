package feeltheagi.springai;

import feeltheagi.springai.model.Agent;
import feeltheagi.springai.model.Message;
import feeltheagi.springai.repo.AgentRepo;
import feeltheagi.springai.repo.MessageRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private Boolean isWork = true;

    private Pattern pattern = Pattern.compile("@(\\S+)\\s+([^@]+)");

    public void run(String username) {
        try {
            Agent user = agentRepo.getOneByName(username);
            String userResponse = user.sendToAgent(user.getSystemPrompt());
            process(user.getName(), userResponse);
        } catch (Exception e) {
            Utils.logException(e);
        }
    }

    public String process(String name, String data) {
        if (isWork) {
            try {
                if ("exit".equalsIgnoreCase(data)) {
                    exit();
                    return "";
                }
                Matcher matcher = pattern.matcher(data);

                int count = 0;
                while (matcher.find()) {
                    count++;
                    String address = matcher.group(1).trim().replace(",", "").replace(":", "");
                    String text = matcher.group(2).trim();
                    messageRepo.add(new Message(name, address, text));
                    List<Agent> receivers = new ArrayList<>();

                    if ("general_chat".equalsIgnoreCase(address)) {
                        receivers.addAll(agentRepo.getAll());
                    } else {
                        Agent receiver = agentRepo.getOneByName(address);
                        if (receiver != null) {
                            receivers.add(receiver);
                        }
                    }
                    Collections.shuffle(receivers);

                    for (Agent receiver : receivers) {
                        if (!name.equalsIgnoreCase(receiver.getName())) {
                            String prompt = messageRepo.getFullprompt(receiver.getName());
                            String response = receiver.sendToAgent(prompt);
                            process(receiver.getName(), response);
                        }
                    }
                }

                if (count < 1) {
                    Agent receiver = agentRepo.getOneByName(name);
                    String prompt = messageRepo.getFullprompt(receiver.getName());
                    prompt += "[System] System: 'You must start your message by mentioning one of the other agents (users) or chats with the @ symbol'\n";
                    String response = receiver.sendToAgent(prompt);
                    process(receiver.getName(), response);
                }
            } catch (Exception e) {
                Utils.logException(e);
            }
        }
        return "";
    }

    public void exit() {
        System.out.println("End");
        isWork = false;
    }
}

package feeltheagi.springai;

import feeltheagi.springai.model.AgentOllama;
import feeltheagi.springai.model.AgentUser;
import feeltheagi.springai.repo.AgentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Autowired
    AgentRepo agentRepo;
    @Autowired
    Processing processing;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,String> roleMap = new HashMap<>();
        String username = "incognito";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your name:");
        username = scanner.nextLine();

        System.out.println("Enter your role:");
        roleMap.put(username,scanner.nextLine());

        List<String> otherAgentNameList = new ArrayList<>();
        System.out.println("Add other agent. Enter agent's name (enter 'enough' to stop adding agents):");
        String agentName = scanner.nextLine();
        while (!"enough".equalsIgnoreCase(agentName)) {
            otherAgentNameList.add(agentName);
            System.out.println(agentName+" added. Enter other agent's name (enter 'enough' to stop adding agents):");
            agentName = scanner.nextLine();
        }
        
        for (String s : otherAgentNameList) {
            System.out.println("Enter role of agent "+s+":");
            roleMap.put(s,scanner.nextLine());
        }
        
        for (String a : otherAgentNameList) {
            String thisAgentRole = roleMap.get(a);
            String systemPrompt = "[System] System: 'Your name is "+a+".\n" +
                    "You are "+thisAgentRole+".\n" +
                    "If you want to tell everyone something, start the message with @general_chat.\n" +
                    "To send a message to "+roleMap.get(username) + " " +username+", start the message with @"+username+".\n";
            for (String otherA : otherAgentNameList) {
                if (!a.equalsIgnoreCase(otherA)) {
                    systemPrompt += "To send a message to "+roleMap.get(otherA) + " " +otherA+", start the message with @"+otherA+".\n";
                }
            }
            systemPrompt += "You can send messages to several chats at once, for example: " +
                    "@general_chat message to everyone @"+username+" message to "+username+"'\n";
            agentRepo.add(new AgentOllama(
                    "http://localhost:11434",
                    "llama3.2:1b",
                    systemPrompt,
                    a));
        }
        agentRepo.add(new AgentUser("To send a message to the general chat, start the message with @general_chat.\n" +
                "To send an agent, start the message with @agent_name.\n" +
                "To exit enter 'exit'",
                username));
        processing.run(username);
    }
}
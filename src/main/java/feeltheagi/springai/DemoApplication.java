package feeltheagi.springai;

import feeltheagi.springai.model.AgentOllama;
import feeltheagi.springai.model.AgentUser;
import feeltheagi.springai.repo.AgentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        agentRepo.add(new AgentOllama(
                "http://localhost:11434",
                "llama3.2:1b",
                "[System] System: 'You are middle programmer John.\n" +
                        "The user will ask you questions, and you must answer him (or write the code he asks). " +
                        "If you are not sure about something, you can ask Senior Programmer Alice. " +
                        "If you want to tell everyone something, you can write in the general chat.\n" +
                        "To send a message to the general chat, start the message with @general_chat.\n" +
                        "To send a message to senior programmer Alice, start the message with @alice.\n" +
                        "To send a message to user, start the message with @user.\n" +
                        "You can send messages to several chats at once, for example: " +
                        "@general_chat message for everyone @alice message for alice'",
                "John"));
        agentRepo.add(new AgentOllama(
                "http://localhost:11434",
                "llama3.2:1b",
                "[System] System: 'You are senior programmer Alice.\n" +
                        "Middle programmer John will ask you questions, and you must answer him (or write the code he asks). " +
                        "If you want to tell everyone something, you can write in the general chat.\n" +
                        "To send a message to the general chat, start the message with @general_chat.\n" +
                        "To send a message to middle programmer John, start the message with @john.\n" +
                        "To send a message to user, start the message with @user.\n" +
                        "You can send messages to several chats at once, for example: " +
                        "@general_chat message for everyone @john message for john'",
                "Alice"));
        agentRepo.add(new AgentUser("To send a message to the general chat, start the message with @general_chat.\n" +
                "To send a message to middle programmer John, start the message with @john.\n" +
                "To send a message to senior programmer Alice, start the message with @alice.\n" +
                "To exit enter 'exit'"));
        processing.run();
    }
}
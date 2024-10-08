package feeltheagi.springai.model;

import lombok.Data;

import java.util.Scanner;

@Data
public class AgentUser implements Agent {

    private final String name;
    private final String systemPrompt;
    private final Scanner scanner = new Scanner(System.in);

    public AgentUser(String systemPrompt, String name) {
        this.systemPrompt = systemPrompt;
        this.name = name;
    }

    public String sendToAgent(String prompt) {
        //System.out.println(prompt);
        System.out.println("Enter your prompt (for example @general_chat message for everyone @john message for john):");
        return scanner.nextLine();
    }
}
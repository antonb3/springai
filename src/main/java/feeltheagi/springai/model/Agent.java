package feeltheagi.springai.model;

public interface Agent {
    public String getName();
    public String getSystemPrompt();
    public String sendToAgent(String prompt);
}
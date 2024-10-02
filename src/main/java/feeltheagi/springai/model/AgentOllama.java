package feeltheagi.springai.model;

import lombok.Data;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;

@Data
public class AgentOllama implements Agent {

    private final OllamaChatModel agent;
    private final String name;
    private final String systemPrompt;

    public AgentOllama(String url, String model, String systemPrompt, String name) {
        this.agent = new OllamaChatModel(new OllamaApi(url), OllamaOptions.create()
                .withModel(model)
                .withTemperature(0.4d));
        this.name = name;
        this.systemPrompt = systemPrompt;
    }

    public String sendToAgent(String prompt) {
        ChatResponse response = agent.call(new Prompt(prompt,
                OllamaOptions.create().withTemperature(0.7d)));

        String responseText = response.getResult().getOutput().getContent();
        return responseText;
    }
}
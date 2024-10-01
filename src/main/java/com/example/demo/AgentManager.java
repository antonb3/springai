package com.example.demo;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgentManager {

    private final OllamaChatModel agent1;
    private final OllamaChatModel agent2;
    private final List<String> agent1History = new ArrayList<>();
    private final List<String> agent2History = new ArrayList<>();

    public AgentManager() {
        // Инициализация агентов с разными моделями
        this.agent1 = new OllamaChatModel(new OllamaApi("http://localhost:11434"), OllamaOptions.create()
                .withModel("llama3.2:1b")
                .withTemperature(0.4d));
        this.agent2 = new OllamaChatModel(new OllamaApi("http://localhost:11434"), OllamaOptions.create()
                .withModel("llama3.2:3b")
                .withTemperature(0.4d));
    }

    public String sendToAgent1(String message) {
        agent1History.add("User: " + message);
        String fullPrompt = String.join("\n", agent1History);

        ChatResponse response = agent1.call(new Prompt(fullPrompt,
                OllamaOptions.create().withTemperature(0.7d)));

        String agentResponse = response.getResult().getOutput().getContent();
        agent1History.add("Agent 1: " + agentResponse);
        return agentResponse;
    }

    public String sendToAgent2(String message) {
        agent2History.add("User: " + message);
        String fullPrompt = String.join("\n", agent2History);

        ChatResponse response = agent2.call(new Prompt(fullPrompt,
                OllamaOptions.create().withTemperature(0.5d)));

        String agentResponse = response.getResult().getOutput().getContent();
        agent2History.add("Agent 2: " + agentResponse);
        return agentResponse;
    }
}
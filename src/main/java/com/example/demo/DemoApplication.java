package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private AgentManager agentManager;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        System.out.println("Начните диалог (для выхода введите 'exit'):");

        while (true) {
            System.out.print("Вы: ");
            userInput = scanner.nextLine();

            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }

            // Отправляем ввод пользователя обоим агентам
            String agent1Response = agentManager.sendToAgent1(userInput);
            String agent2Response = agentManager.sendToAgent2(userInput);

            System.out.println("Агент 1: " + agent1Response);
            System.out.println("Агент 2: " + agent2Response);
        }

        System.out.println("Диалог завершен.");
    }
}
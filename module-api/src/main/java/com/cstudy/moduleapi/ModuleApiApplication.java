package com.cstudy.moduleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static com.cstudy.moduleapi.discord.DiscordBot.setupDiscordBot;


@SpringBootApplication(scanBasePackages = {
        "com.cstudy.moduleapi",
        "com.cstudy.modulecommon"
})
public class ModuleApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ModuleApiApplication.class, args);
        setupDiscordBot(context);
    }

}

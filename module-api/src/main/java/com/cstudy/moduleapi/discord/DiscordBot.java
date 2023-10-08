package com.cstudy.moduleapi.discord;

import com.cstudy.moduleapi.application.request.RequestService;
import lombok.Getter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot {
    public static void setupDiscordBot(ConfigurableApplicationContext context) {
        DiscordBotToken discordBotTokenEntity = context.getBean(DiscordBotToken.class);
        RequestService post = context.getBean(RequestService.class);
        String discordBotToken = discordBotTokenEntity.getDiscordBotToken();

        JDABuilder.createDefault(discordBotToken)
                .setActivity(Activity.playing("메시지 기다리는 중!"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordListener(post))
                .build();
    }
}

@Getter
@Component
class DiscordBotToken {
    @Value("${discord.bot.token}")
    private String discordBotToken;

}
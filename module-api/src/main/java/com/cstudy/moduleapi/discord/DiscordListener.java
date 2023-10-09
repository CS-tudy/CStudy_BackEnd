package com.cstudy.moduleapi.discord;

import com.cstudy.moduleapi.application.request.RequestService;
import com.cstudy.modulecommon.domain.request.Request;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.cstudy.moduleapi.discord.DiscordViewComponent.viewDiscord;

@Slf4j
@Component
public class DiscordListener extends ListenerAdapter {

    private final RequestService requestService;

    public DiscordListener(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        TextChannel textChannel = event.getChannel().asTextChannel();
        Message message = event.getMessage();

        log.info("get message : " + message.getContentDisplay());

        if (user.isBot()) {
            return;
        } else if (message.getContentDisplay().equals("")) {
            log.info("디스코드 Message 문자열 값 공백");
        }
        String[] messageArray = message.getContentDisplay().split(" ");

        if (messageArray[0].equalsIgnoreCase("무건아")) {
            String[] messageArgs = Arrays.copyOfRange(messageArray, 1, messageArray.length);

            for (String msg : messageArgs) {
                String returnMessage = sendMessage(event, msg);
                textChannel.sendMessage(returnMessage).queue();
            }
        }

    }

    public String sendMessage(MessageReceivedEvent event, String message) {
        User user = event.getAuthor();
        log.info(">>>>"+user);

        String returnMessage = "";
        switch (message) {
            case "날짜":
                LocalDateTime now = LocalDateTime.now();
                returnMessage = String.valueOf(now);
                break;

            case "명령어":
                returnMessage = "1. 날짜 \n  2. 명령어 \n 3. 대회 \n 4. 문제 ";
                break;

            case "문제":
                List<Request> listForDiscord = requestService.getRequestListForDiscord();
                StringBuilder answer = viewDiscord(listForDiscord);
                returnMessage = String.valueOf(answer);
                break;
            case "대회":
                returnMessage = "대회";
                break;

            default:
                break;
        }
        return returnMessage;
    }

}
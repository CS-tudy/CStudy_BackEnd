package com.cstudy.moduleapi.discord;

import com.cstudy.modulecommon.domain.request.Request;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscordViewComponent {
    static StringBuilder viewDiscord(List<Request> search) {
        StringBuilder answer = new StringBuilder();
        answer.append("## 요청 문제 리스트" + "\n").append("\n");
        search.forEach(request -> {

            answer.append("```json")
                    .append("\n")
                    .append("\uD83D\uDE00 요청 문제 제목: "+" "+ + request.getId())
                    .append("\n")
                    .append(request.getTitle())
                    .append("\n")
                    .append("\uD83D\uDC3C 요청 문제 설명")
                    .append("\n")
                    .append(request.getDescription())
                    .append("\n")
                    .append("\uD83D\uDC14 요청 문제 날짜")
                    .append("\n")
                    .append(request.getCreatedAt())
                    .append("```")
                    .append("\n");
            answer.append("\n");
        });
        return answer;
    }
}

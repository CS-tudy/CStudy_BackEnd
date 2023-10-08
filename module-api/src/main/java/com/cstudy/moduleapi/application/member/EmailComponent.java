package com.cstudy.moduleapi.application.member;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Random;

@Component
public class EmailComponent {
    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @NotNull
    public  String emailHtml(String key) {
        String msgg = "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요 CS;tudy입니다!!!</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += key + "</strong><div><br/> ";
        msgg += "</div>";
        return msgg;
    }
}

package com.example.faceCommit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${BOT_TOKEN}")
    private String BOT_TOKEN;

    @Value("${CHAT_ID}")
    private String CHAT_ID;
    private final RestTemplate restTemplate = new RestTemplate();


    public void sendMessage(String message) {
        String url = String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
            BOT_TOKEN, CHAT_ID, message
        );
        restTemplate.getForObject(url, String.class);
    }

}

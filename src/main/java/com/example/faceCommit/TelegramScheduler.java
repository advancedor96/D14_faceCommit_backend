package com.example.faceCommit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TelegramScheduler {
    
    @Autowired
    private TelegramService telegramService;
    
    @Scheduled(cron = "0 0 * * * ?")
    public void sendScheduledMessage() {
        System.out.println("排程");
       ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(2));
        // 只要小時和 00 分鐘
        String hourTime = now.withMinute(0).withSecond(0).format(DateTimeFormatter.ofPattern("HH:mm"));


        telegramService.sendMessage("你的Render java還活著，時間" + hourTime);
    }
}
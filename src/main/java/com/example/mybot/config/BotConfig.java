package com.example.mybot.config;

import com.example.mybot.entity.MyTelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    private final MyTelegramBot myTelegramBot;

    public BotConfig(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myTelegramBot);
            System.out.println("✅ Бот успешно зарегистрирован");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Ошибка регистрации бота");
        }
    }
}


package com.example.mybot.entity;

import com.example.mybot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public MyTelegramBot(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            System.out.println("📥 Получено сообщение: " + text + " от chatId: " + chatId);

            userRepository.findByTelegramToken(text).ifPresentOrElse(user -> {
                // Сохраняем chatId
                user.setTelegramChatId(chatId);
                userRepository.save(user);

                // Отправляем подтверждение
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("✅ Токен подтверждён. Теперь ты привязан к боту!");

                try {
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, () -> {
                // Если токен не найден
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("❌ Токен не найден. Попробуй ещё раз.");

                try {
                    execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}


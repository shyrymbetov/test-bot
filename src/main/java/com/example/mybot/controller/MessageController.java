package com.example.mybot.controller;

import com.example.mybot.entity.Message;
import com.example.mybot.entity.MyTelegramBot;
import com.example.mybot.entity.User;
import com.example.mybot.repository.MessageRepository;
import com.example.mybot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MyTelegramBot myTelegramBot;

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> body, Principal principal) {
        String text = body.get("text");
        var user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Сохраняем сообщение в БД
        Message message = new Message();
        message.setUser(user);
        message.setText(text);
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);

        // Формируем сообщение для Telegram
        String messageForTelegram = user.getFullName() + ", я получил от тебя сообщение:\n" + text;

        if (user.getTelegramChatId() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(user.getTelegramChatId()));
            sendMessage.setText(messageForTelegram);
            try {
                myTelegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message to Telegram");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Telegram chatId not linked");
        }

        return ResponseEntity.ok("Message sent");
    }
}

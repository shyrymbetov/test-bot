# Telegram Bot Integration with Spring Boot

Этот проект представляет собой Telegram-бота, интегрированного с REST API на Spring Boot. Бот принимает сообщения от пользователей и позволяет приложению отправлять сообщения пользователям через Telegram.

## 🔧 Стек технологий

- Java 17+
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- TelegramBots Java Library (`org.telegram:telegrambots-spring-boot-starter`)
- Maven

## 📦 Установка и запуск

### 1. Клонируйте репозиторий

```bash
git clone https://github.com/yourusername/telegram-bot.git
cd telegram-bot


2. Настройте .env или application.yml
Добавьте в application.yml или .properties файл:

properties
Копировать
Редактировать
telegram.bot.username=YourBotUsername
telegram.bot.token=YourBotToken

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password
Получите botToken у BotFather в Telegram.

3. Сборка и запуск
bash
Копировать
Редактировать
mvn clean install
java -jar target/telegram-bot.jar

📲 Как пользоваться
1. Связка Telegram с пользователем
Пользователь получает уникальный telegramToken в приложении.

В Telegram отправляет этот токен боту:

bash
Копировать
Редактировать
/start
<уникальный telegramToken>
Бот сохраняет chatId пользователя в БД.

2. Отправка сообщений пользователю
Используйте POST-запрос:

bash
Копировать
Редактировать
POST /sendMessage
Пример запроса:

json
Копировать
Редактировать
{
  "text": "Привет, это тестовое сообщение!"
}
Заголовок:

makefile
Копировать
Редактировать
Authorization: Bearer <ваш_JWT_токен>
Бот отправит сообщение в Telegram, если пользователь ранее связал аккаунт.

🧩 Структура проекта
css
Копировать
Редактировать
src/
 └── main/
     ├── java/
     │   └── com.example.mybot/
     │       ├── entity/
     │       │   └── MyTelegramBot.java
     │       ├── controller/
     │       │   └── MessageController.java
     │       ├── repository/
     │       ├── service/
     │       └── DemoApplication.java
     └── resources/
         └── application.properties
🛠 Возможные улучшения
Интерфейс для генерации telegramToken

Поддержка команд (/help, /unsubscribe)

Поддержка изображений и файлов

Docker

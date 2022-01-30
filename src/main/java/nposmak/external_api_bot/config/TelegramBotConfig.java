package nposmak.external_api_bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class TelegramBotConfig {

    @Value("${telegrambot.webHookPath}")
    private String botPath;
    @Value("${telegrambot.botUserName}")
    private String botUserName;
    @Value("${telegrambot.botToken}")
    private String botToken;



}

package nposmak.external_api_bot;


import nposmak.external_api_bot.config.TelegramBot;
import nposmak.external_api_bot.config.TelegramBotConfig;
import nposmak.external_api_bot.config.TelegramFacade;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class ApplicationConfig {

    private final TelegramBotConfig botConfig;

    public ApplicationConfig(TelegramBotConfig botConfig) {
        this.botConfig = botConfig;
    }


    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getBotPath()).build();
    }



    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot telegramBot = new TelegramBot(telegramFacade, setWebhook);
        telegramBot.setBotToken(botConfig.getBotToken());
        telegramBot.setBotUserName(botConfig.getBotUserName());
        telegramBot.setBotPath(botConfig.getBotPath());

        return telegramBot;
    }

    /** message source for REPLY*/
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}

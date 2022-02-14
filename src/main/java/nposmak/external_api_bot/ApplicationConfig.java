package nposmak.external_api_bot;


import nposmak.external_api_bot.config.TelegramBot;
import nposmak.external_api_bot.config.TelegramBotConfig;
import nposmak.external_api_bot.config.TelegramFacade;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@EnableCaching
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

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("192.168.99.100");//docker access url
        redisStandaloneConfiguration.setPort(6379);//docker access port
        //redisStandaloneConfiguration.setPassword("password");

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return  jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}

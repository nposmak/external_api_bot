package nposmak.external_api_bot.redisCache;


import nposmak.external_api_bot.botState_control.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.concurrent.TimeUnit;


@Repository
public class UsersDataService {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "user";
    private static final String KEY1 ="state";

    public void setCurrentBotState(long userId, BotState botState){
        try {
            redisTemplate.opsForHash().put(KEY1, userId, botState);
            redisTemplate.expire(KEY1, 5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BotState getCurrentBotState(Long userId) {
        BotState botState;
        botState = (BotState) redisTemplate.opsForHash().get(KEY1, userId);
        if(botState==null){
            botState = BotState.COMPLETE;
        }
        return botState;
    }

    public void saveUserData(long userId, UsersData usersData) {
        try {
            redisTemplate.opsForHash().put(KEY, userId, usersData);
            redisTemplate.expire(KEY, 5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UsersData getTrainSearchData(long userId) {
        UsersData usersData = (UsersData) redisTemplate.opsForHash().get(KEY, userId);
        if(usersData==null){
            usersData = new UsersData();
        }
        return usersData;
    }

}

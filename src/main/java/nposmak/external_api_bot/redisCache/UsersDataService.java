package nposmak.external_api_bot.redisCache;


import nposmak.external_api_bot.botState_control.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Repository
public class UsersDataService {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final String KEY = "user";
    private static final String KEY1 ="state";


    public void setCurrBotState(long userId, BotState botState){
        try {
            redisTemplate.opsForHash().put(KEY1, userId, botState);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public BotState getUsersCurrentBotState(Long userId) {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public UsersData getUsersTrainSearchData(long userId) {
        UsersData usersData = (UsersData) redisTemplate.opsForHash().get(KEY, userId);
        if(usersData==null){
            usersData = new UsersData();
        }
        return usersData;
    }

}

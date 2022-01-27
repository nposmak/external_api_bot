package nposmak.external_api_bot.chatCache;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.dto.TrainInfo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestDataCache implements RequestDataCacheInterface {

    private Map<Long, BotState> usersBotState = new HashMap<>();
    private Map<Long, RequestData> usersRequestData = new HashMap<>();
    //private Map<Long, List<TrainInfo>> foundedTrains = new HashMap<>();



    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
        usersBotState.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotState.get(userId);
        if(botState==null){
            botState = BotState.COMPLETE;
        }
        return botState;
    }

    @Override
    public void saveTrainSearchData(long userId, RequestData requestData) {
        usersRequestData.put(userId, requestData);
    }

    @Override
    public RequestData getUsersTrainSearchData(long userId) {
        RequestData userRequestData = usersRequestData.get(userId);
        if(userRequestData==null){
            userRequestData = new RequestData();
        }
        return userRequestData;
    }




    //@Override
//    public List<TrainInfo> getUsersFoundedTrains(long chatId) {
//        List<TrainInfo> foundedTrain = foundedTrains.get(chatId);
//
//        return Objects.isNull(foundedTrain) ? Collections.emptyList() : foundedTrain;
//    }
}
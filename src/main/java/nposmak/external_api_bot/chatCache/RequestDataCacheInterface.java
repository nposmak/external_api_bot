package nposmak.external_api_bot.chatCache;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.dto.TrainInfo;

import java.util.List;

public interface RequestDataCacheInterface {

    void setUsersCurrentBotState(long userId, BotState botState);

    BotState getUsersCurrentBotState(long userId);

    RequestData getUsersTrainSearchData(long userId);

    void saveTrainSearchData(long userId, RequestData requestData);

    //List<TrainInfo> getUsersFoundedTrains(long chatId);



//
//
//
//    void saveSearchFoundedTrains(long chatId, List<Train> foundTrains);
//
//
}

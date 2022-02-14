package nposmak.external_api_bot.chatCache;

import nposmak.external_api_bot.botState_control.BotState;

public interface RequestDataCacheInterface {

    void setUsersCurrentBotState(long userId, BotState botState);

    BotState getUsersCurrentBotState(long userId);

    RequestData getUsersTrainSearchData(long userId);

    void saveTrainSearchData(long userId, RequestData requestData);

}

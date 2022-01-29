package nposmak.external_api_bot.handlers;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.InputMessageHandler;
import nposmak.external_api_bot.chatCache.RequestData;
import nposmak.external_api_bot.chatCache.RequestDataCache;
import nposmak.external_api_bot.chatCache.StationCache;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.dto.StationCode;
import nposmak.external_api_bot.service.StationBookCommunication;
import nposmak.external_api_bot.service.StationCodeCommunication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationBookHandler implements InputMessageHandler {
    private RequestDataCache requestDataCache;
    private StationBookCommunication stationBookCommunication;
    private StationCache stationCache;

    public StationBookHandler(RequestDataCache requestDataCache,
                              StationBookCommunication stationBookCommunication,
                              StationCache stationCache) {
        this.requestDataCache = requestDataCache;
        this.stationBookCommunication = stationBookCommunication;
        this.stationCache = stationCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if(requestDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.STATION_BOOK)){
            requestDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_STATION_NAME);
        }
        return processUsersMessage(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.STATION_BOOK;
    }

    private SendMessage processUsersMessage(Message usersMessage){
        String usersAnswer = usersMessage.getText();
        long userId = usersMessage.getFrom().getId();
        long chatId = usersMessage.getChatId();

        SendMessage replyToUser = new SendMessage();
        replyToUser.setChatId(String.valueOf(chatId));

        RequestData requestData = requestDataCache.getUsersTrainSearchData(userId);
        BotState botState = requestDataCache.getUsersCurrentBotState(userId);

        if(botState.equals(BotState.ASK_STATION_NAME)){
            replyToUser.setText("Введите название станции:");
            requestDataCache.setUsersCurrentBotState(userId, BotState.STATION_NAME_RECIVED);
        }

        if(botState.equals(BotState.STATION_NAME_RECIVED)){
            List<StationCode> trainStations = stationBookCommunication.processStationBookRequest(usersAnswer);
            if(trainStations.isEmpty()){
                replyToUser.setText(Icons.X+" "+"По вашему запросу ничего не найдено"+" "+Icons.X);
                return replyToUser;
            }

            StringBuilder stationBookText = new StringBuilder();
            for(StationCode stationCode: trainStations){
                if(stationCode.getStationName().contains(usersAnswer.toUpperCase())){
                    stationBookText.append("имя в справочнике: "+stationCode.getStationName()+"\n");
                }


            replyToUser.setText(String.valueOf(stationBookText));
            }
            //requestDataCache.setUsersCurrentBotState(userId, BotState.MENU);
            return replyToUser;
        }

        return replyToUser;
    }
}

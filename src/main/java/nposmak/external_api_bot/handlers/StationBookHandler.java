package nposmak.external_api_bot.handlers;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.InputMessageHandler;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.dto.StationCode;
import nposmak.external_api_bot.redisCache.UsersData;
import nposmak.external_api_bot.redisCache.UsersDataService;
import nposmak.external_api_bot.service.StationBookCommunication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class StationBookHandler implements InputMessageHandler {
    private UsersDataService usersDataService;
    private StationBookCommunication stationBookCommunication;

    public StationBookHandler(StationBookCommunication stationBookCommunication,
                              UsersDataService usersDataService
                              ) {
        this.usersDataService = usersDataService;
        this.stationBookCommunication = stationBookCommunication;
    }

    @Override
    public SendMessage handle(Message message) {
        if(usersDataService.getCurrentBotState(message.getFrom().getId()).equals(BotState.STATION_BOOK)){
            usersDataService.setCurrentBotState(message.getFrom().getId(), BotState.ASK_STATION_NAME);
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

        UsersData usersData = usersDataService.getTrainSearchData(userId);
        BotState botState = usersDataService.getCurrentBotState(userId);

        if(botState.equals(BotState.ASK_STATION_NAME)){
            replyToUser.setText("Введите название станции:");
            usersDataService.setCurrentBotState(userId, BotState.STATION_NAME_RECIVED);
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
            return replyToUser;
        }

        return replyToUser;
    }
}

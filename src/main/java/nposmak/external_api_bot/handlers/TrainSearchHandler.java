package nposmak.external_api_bot.handlers;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.InputMessageHandler;
import nposmak.external_api_bot.chatCache.RequestDataCache;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.dto.TrainInfo;
import nposmak.external_api_bot.redisCache.UsersData;
import nposmak.external_api_bot.redisCache.UsersDataService;
import nposmak.external_api_bot.service.SendMessInSequence;
import nposmak.external_api_bot.service.StationCodeCommunication;
import nposmak.external_api_bot.service.TrainInfoCommunication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class TrainSearchHandler implements InputMessageHandler {

    //private RequestDataCache requestDataCache;
    private StationCodeCommunication stationCodeCommunication;
    private TrainInfoCommunication trainInfoCommunication;
    private SendMessInSequence sendMessInSequence;

    private UsersDataService usersDataService;


    public TrainSearchHandler(RequestDataCache requestDataCache,
                              StationCodeCommunication stationCodeCommunication,
                              TrainInfoCommunication trainInfoCommunication,
                              SendMessInSequence sendMessInSequence,
                              UsersDataService usersDataService) {
        //this.requestDataCache = requestDataCache;
        this.stationCodeCommunication = stationCodeCommunication;
        this.trainInfoCommunication = trainInfoCommunication;
        this.sendMessInSequence = sendMessInSequence;

        this.usersDataService = usersDataService;
    }

    @Override
    public SendMessage handle(Message message){

//        if(requestDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARCH_FOR_TRAIN)){
//            requestDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_DEPARTURE_STATION);
//        }

        if(usersDataService.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARCH_FOR_TRAIN)){
            usersDataService.setCurrBotState(message.getFrom().getId(), BotState.ASK_DEPARTURE_STATION);
        }
        return processUsersMessage(message);
    }

    @Override
    public BotState getHandlerName(){

        return BotState.SEARCH_FOR_TRAIN;
    }


    private SendMessage processUsersMessage(Message usersMessage){

        String usersAnswer = usersMessage.getText();
        long userId = usersMessage.getFrom().getId();
        long chatId = usersMessage.getChatId();

        SendMessage replyToUser = new SendMessage();
        replyToUser.setChatId(String.valueOf(chatId));

//        RequestData requestData = requestDataCache.getUsersTrainSearchData(userId);
//        BotState botState = requestDataCache.getUsersCurrentBotState(userId);
        UsersData usersData = usersDataService.getUsersTrainSearchData(userId);
        BotState botState = usersDataService.getUsersCurrentBotState(userId);

        if(botState.equals(BotState.ASK_DEPARTURE_STATION)){
            replyToUser.setText("Введите станцию отправления:");
            //requestDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARRIVAL_STATION);
            usersDataService.setCurrBotState(userId, BotState.ASK_ARRIVAL_STATION);
        }

        if(botState.equals(BotState.ASK_ARRIVAL_STATION)){
            long departureStationCode = stationCodeCommunication.getStationCode(usersAnswer);
            if( departureStationCode == -1){
                replyToUser.setText("Станция не найдена, введите заного или проверьте в СПРАВОЧНИКЕ СТАНЦИЙ.");
              return replyToUser;
            }else
            //requestData.setDepartureStationCode(departureStationCode);
            usersData.setDepartureStationCode(departureStationCode);
            //replyToUser.setText("Код станции отправления:"+requestData.getDepartureStationCode() + "\n"+"Введите станицю назначения:");
            replyToUser.setText("Код станции отправления:"+usersData.getDepartureStationCode() + "\n"+"Введите станицю назначения:");
            usersDataService.setCurrBotState(userId, BotState.ASK_DEPARTURE_DATE);
        }

        if(botState.equals(BotState.ASK_DEPARTURE_DATE)){
            long arrivalStationCode = stationCodeCommunication.getStationCode(usersAnswer);

            if(arrivalStationCode == -1){
                replyToUser.setText("Станция не найдена, введите заного или проверьте в СПРАВОЧНИКЕ СТАНЦИЙ.");
                return replyToUser;
            }
            //requestData.setArrivalStationCode(arrivalStationCode);
            usersData.setArrivalStationCode(arrivalStationCode);

            //replyToUser.setText(requestData.getArrivalStationCode() + "\n" +"Введите дату отправления в формате дд.мм.гггг");
            replyToUser.setText(usersData.getArrivalStationCode() +
                    "\n" +"Введите дату отправления в формате дд.мм.гггг");
            usersDataService.setCurrBotState(userId, BotState.CHECK_DEPARTURE_DATE);
        }

        if(botState.equals(BotState.CHECK_DEPARTURE_DATE)){
            Date departureDate;
            try {
                departureDate = new SimpleDateFormat("dd.MM.yyyy").parse(usersAnswer);
            }catch (ParseException e){

                replyToUser.setText("Неверный формат даты!");
                return  replyToUser;
            }
            //requestData.setDateDepart(departureDate);
            usersData.setDateDepart(departureDate);
            List<TrainInfo> trainInfoList = trainInfoCommunication.getTrainInfo(
                    //requestData.getDepartureStationCode(),
                    //requestData.getArrivalStationCode(),
                    usersData.getDepartureStationCode(),
                    usersData.getArrivalStationCode(),
                    departureDate);

            if(trainInfoList.isEmpty()){

                replyToUser.setText(Icons.X+" "+"По вашему запросу ничего не найдено"+" "+"попробуйте ввести" +
                        " ДРУГУЮ ДАТУ или проверьте название в СПРАВОЧНИКЕ");
                return  replyToUser;
            }
            sendMessInSequence.TicketsMessageSequence(chatId, trainInfoList);
            replyToUser.setText("Поиск завершен!");
            //requestDataCache.setUsersCurrentBotState(userId, BotState.COMPLETE);
            return replyToUser;
        }

        //requestDataCache.saveTrainSearchData(userId, requestData);
        usersDataService.saveUserData(userId, usersData);
        return replyToUser;
    }

}

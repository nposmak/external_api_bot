package nposmak.external_api_bot.handlers;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.InputMessageHandler;
import nposmak.external_api_bot.chatCache.RequestData;
import nposmak.external_api_bot.chatCache.RequestDataCache;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.dto.TrainCarInfo;
import nposmak.external_api_bot.dto.TrainInfo;
import nposmak.external_api_bot.service.StationCodeCommunication;
import nposmak.external_api_bot.service.TrainInfoCommunication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class TrainSearchHandler implements InputMessageHandler {

    private RequestDataCache requestDataCache;
    private StationCodeCommunication stationCodeCommunication;
    private TrainInfoCommunication trainInfoCommunication;


    public TrainSearchHandler(RequestDataCache requestDataCache,
                              StationCodeCommunication stationCodeCommunication,
                               TrainInfoCommunication trainInfoCommunication) {
        this.requestDataCache = requestDataCache;
        this.stationCodeCommunication = stationCodeCommunication;
        this.trainInfoCommunication = trainInfoCommunication;
    }

    @Override
    public SendMessage handle(Message message){

        if(requestDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.SEARCH_FOR_TRAIN)){
            requestDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_DEPARTURE_STATION);
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

        RequestData requestData = requestDataCache.getUsersTrainSearchData(userId);
        BotState botState = requestDataCache.getUsersCurrentBotState(userId);

        if(botState.equals(BotState.ASK_DEPARTURE_STATION)){

            replyToUser.setText("Введите станцию отправления:");

            requestDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARRIVAL_STATION);
        }

        if(botState.equals(BotState.ASK_ARRIVAL_STATION)){
            long departureStationCode = stationCodeCommunication.getStationCode(usersAnswer);

            if( departureStationCode == -1){
                replyToUser.setText("Станция не найдена, введите заного или проверьте в СПРАВОЧНИКЕ СТАНЦИЙ.");

              return replyToUser;

            }else

            requestData.setDepartureStationCode(departureStationCode);


            replyToUser.setText("Код станции отправления:"+requestData.getDepartureStationCode() + "\n"+"Введите станицю назначения:");

            requestDataCache.setUsersCurrentBotState(userId, BotState.ASK_DEPARTURE_DATE);
        }

        if(botState.equals(BotState.ASK_DEPARTURE_DATE)){
            long arrivalStationCode = stationCodeCommunication.getStationCode(usersAnswer);

            if(arrivalStationCode == -1){
                replyToUser.setText("Станция не найдена, введите заного или проверьте в СПРАВОЧНИКЕ СТАНЦИЙ.");
                return replyToUser;
            }
            requestData.setArrivalStationCode(arrivalStationCode);


            replyToUser.setText(requestData.getArrivalStationCode() + "\n" +"Введите дату отправления в формате дд.мм.гггг");

            requestDataCache.setUsersCurrentBotState(userId, BotState.CHECK_DEPARTURE_DATE);


        }

        if(botState.equals(BotState.CHECK_DEPARTURE_DATE)){



            Date departureDate;
            try {
                departureDate = new SimpleDateFormat("dd.MM.yyyy").parse(usersAnswer);
            }catch (ParseException e){

                replyToUser.setText("Неверный формат даты!");
                return  replyToUser;
            }
            requestData.setDateDepart(departureDate);


            List<TrainInfo> trainInfoList = trainInfoCommunication.getTrainInfo(
                    requestData.getDepartureStationCode(),
                    requestData.getArrivalStationCode(),
                    departureDate);

            if(trainInfoList.isEmpty()){

                replyToUser.setText(Icons.X+" "+"По вашему запросу ничего не найдено"+" "+Icons.X);
                return  replyToUser;
            }

            StringBuilder trainInfoText = new StringBuilder();
            for(TrainInfo trains: trainInfoList){

                trainInfoText.append("\n"+Icons.TRAIN +"№" + trains.getTrainNumber()+" " +trains.getTrainBrand()+"\n" );
                trainInfoText.append("станция отправления: " + trains.getStationDeparture()+ "\n");
                trainInfoText.append("станция прибытия: " + trains.getStationArrival()+ "\n");
                trainInfoText.append("дата отправления:"+ Icons.ARROW + trains.getDateDeparture()+ "\n");
                trainInfoText.append("время отправления:"+ Icons.ARROW  + trains.getTimeDeparture()+ "\n");
                trainInfoText.append("дата прибытия: " + trains.getDateArrival()+ "\n");
                trainInfoText.append("время прибытия: " + trains.getTimeArrival()+ "\n");
                trainInfoText.append("время в пути: "+ trains.getTimeInWay()+ "\n");

                List<TrainCarInfo> cars = new ArrayList<>(trains.getCars());
                StringBuilder carText = new StringBuilder();

                for(TrainCarInfo car: cars){
                    carText.append("\n"+Icons.VAGON + "тип вагона: "+car.getCarType()+" "+ "\n");
                    carText.append("мин. цена: " + car.getTariff()+" "+ "\n");
                    carText.append("свободных мест: "+car.getFreeSeats()+"\n");

                }

            replyToUser.setText(String.valueOf(trainInfoText.append(carText)));

            }

            requestDataCache.setUsersCurrentBotState(userId, BotState.COMPLETE);
            return replyToUser;
        }

        requestDataCache.saveTrainSearchData(userId, requestData);
        return replyToUser;
    }

}

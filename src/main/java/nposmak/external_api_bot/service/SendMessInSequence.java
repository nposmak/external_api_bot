package nposmak.external_api_bot.service;

//import nposmak.external_api_bot.chatCache.RequestDataCache;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.config.TelegramBot;
import nposmak.external_api_bot.dto.TrainCarInfo;
import nposmak.external_api_bot.dto.TrainInfo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMessInSequence {
    private TelegramBot telegramBot;

    public SendMessInSequence(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void TicketsMessageSequence(long chatId, List<TrainInfo> trainInfoList){
        for(TrainInfo trainInfo : trainInfoList){
            StringBuilder trainInfoText = new StringBuilder();
            trainInfoText.append("\n"+ Icons.TRAIN +"№" + trainInfo.getTrainNumber()+" " +trainInfo.getTrainBrand()+"\n" );
            trainInfoText.append("станция отправления: " + trainInfo.getStationDeparture()+ "\n");
            trainInfoText.append("станция прибытия: " + trainInfo.getStationArrival()+ "\n");
            trainInfoText.append("дата отправления:"+ Icons.RED_DOT + trainInfo.getDateDeparture()+ "\n");
            trainInfoText.append("время отправления:"+ Icons.RED_DOT  + trainInfo.getTimeDeparture()+ "\n");
            trainInfoText.append("дата прибытия: " + Icons.BLUE_DOT  +trainInfo.getDateArrival()+ "\n");
            trainInfoText.append("время прибытия: " + Icons.BLUE_DOT  +trainInfo.getTimeArrival()+ "\n");
            trainInfoText.append("время в пути: "+ trainInfo.getTimeInWay()+ "\n");

            List<TrainCarInfo> cars = new ArrayList<>(trainInfo.getCars());
            StringBuilder carText = new StringBuilder();

            for(TrainCarInfo car: cars){
                carText.append(Icons.VAGON + "тип вагона: "+car.getCarType()+" "+ "\n");
                carText.append("мин. цена: " + car.getTariff()+" "+ "\n");
                carText.append("свободных мест: "+car.getFreeSeats()+"\n");
            }

            String trainInfoMessage = String.valueOf( trainInfoText.append(carText));
            telegramBot.sendMessage(chatId, trainInfoMessage);
        }

    }
}

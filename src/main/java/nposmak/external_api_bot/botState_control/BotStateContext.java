package nposmak.external_api_bot.botState_control;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();



    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }



    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (whatIsBotState(currentState)) {
            return messageHandlers.get(BotState.SEARCH_FOR_TRAIN);
        }

        return messageHandlers.get(currentState);
    }


    private boolean whatIsBotState(BotState currentState){
        switch (currentState){
            case SEARCH_FOR_TRAIN:
            case ASK_DEPARTURE_STATION:
            case ASK_ARRIVAL_STATION:
            case ASK_DEPARTURE_DATE:
            case CHECK_DEPARTURE_DATE:
            case COMPLETE:
                return true;
            default:
                return false;
        }
    }

    /*private boolean isTrainSearchState(BotState currentState) {
        switch (currentState) {
            case TRAINS_SEARCH:
            case ASK_DATE_DEPART:
            case DATE_DEPART_RECEIVED:
            case ASK_STATION_ARRIVAL:
            case ASK_STATION_DEPART:
            case TRAINS_SEARCH_STARTED:
            case TRAIN_INFO_RESPONCE_AWAITING:
            case TRAINS_SEARCH_FINISH:
                return true;
            default:
                return false;
        }
    }*/

    /*private boolean isStationSearchState(BotState currentState) {
        switch (currentState) {
            case SHOW_STATIONS_BOOK_MENU:
            case ASK_STATION_NAMEPART:
            case STATION_NAMEPART_RECEIVED:
            case STATIONS_SEARCH:
                return true;
            default:
                return false;
        }
    }*/

}






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

        if (isStationSearchState(currentState)){
            return messageHandlers.get(BotState.STATION_BOOK);
        }

        if (isTrainSearchState(currentState)){
            return messageHandlers.get(BotState.SEARCH_FOR_TRAIN);
        }

        if (isMenu(currentState)){
            return messageHandlers.get(BotState.MENU);
        }

        return messageHandlers.get(currentState);
    }


    private boolean isMenu(BotState currentState){
        switch (currentState) {

            case MENU:

                return true;
            default:
                return false;
         }
    }

    private boolean isTrainSearchState(BotState currentState) {
        switch (currentState) {

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

    private boolean isStationSearchState(BotState currentState) {
        switch (currentState) {

            case STATION_BOOK:
            case ASK_STATION_NAME:
            case STATION_NAME_RECIVED:

                return true;
            default:
                return false;
        }
    }

}






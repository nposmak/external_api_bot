package nposmak.external_api_bot.config;


import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.BotStateContext;
import nposmak.external_api_bot.chatCache.RequestDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class TelegramFacade {

    private RequestDataCache requestDataCache;
    private BotStateContext botStateContext;



    public TelegramFacade(RequestDataCache requestDataCache, BotStateContext botStateContext) {
        this.requestDataCache = requestDataCache;
        this.botStateContext = botStateContext;
    }

    public SendMessage handleUpdate(Update update) {

        SendMessage responseMessage= null;
        Message message = update.getMessage();

        if (message != null && message.hasText()){
            responseMessage = handleInput(message); }
        return responseMessage;
    }

    private SendMessage handleInput(Message message) {

        String inputText = message.getText();
        long userId = message.getFrom().getId();

        BotState botState;
        SendMessage responseMessage;

        switch (inputText){
            case "НАЙТИ ПОЕЗДА":
                botState = BotState.SEARCH_FOR_TRAIN;
                break;
            case "СПРАВОЧНИК СТАНЦИЙ":
                botState = BotState.STATION_BOOK;
                break;
            case "/start":
                botState = BotState.MENU;
                break;
            default:
                botState = requestDataCache.getUsersCurrentBotState(userId);
                break;
        }

        requestDataCache.setUsersCurrentBotState(userId, botState);

        responseMessage = botStateContext.processInputMessage(botState, message);

        return responseMessage;

    }

}

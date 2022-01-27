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
            case "Найти поезда":
                botState = BotState.SEARCH_FOR_TRAIN;
                break;
            default:
                botState = requestDataCache.getUsersCurrentBotState(userId);
                break;
        }

        requestDataCache.setUsersCurrentBotState(userId, botState);
        responseMessage = botStateContext.processInputMessage(botState, message);

        return responseMessage;

    }


/*    public BotApiMethod<?> handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
             CallbackQuery callbackQuery = update.getCallbackQuery();
            return null;

        } else {

            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            if (message.hasText()) {
                sendMessage.setText("Привет " + message.getFrom().getFirstName() + " "
                + message.getFrom().getLastName() + "!"
                +"\n" + "ID вашего чата " + message.getChatId());
                return sendMessage;
            }
        }
        return null;
    }*/

}

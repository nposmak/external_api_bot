package nposmak.external_api_bot.botState_control;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface InputMessageHandler {

    SendMessage handle(Message message);

    BotState getHandlerName();
}

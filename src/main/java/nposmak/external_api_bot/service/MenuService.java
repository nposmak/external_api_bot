package nposmak.external_api_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    public SendMessage getMenuMessage(final long chatId, final String textMessage){
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMenuKeyboard();
        final SendMessage sendMenuMessage = createMenuKeyBoardMessage(chatId, textMessage, replyKeyboardMarkup);
        return sendMenuMessage;
    }

    private ReplyKeyboardMarkup getMenuKeyboard(){
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList =new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("НАЙТИ ПОЕЗДА"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("СПРАВОЧНИК СТАНЦИЙ"));

        keyboardRowList.add(row1);
        keyboardRowList.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    private SendMessage createMenuKeyBoardMessage(final long chatId, String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup){
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;

    }
}

package nposmak.external_api_bot.handlers;

import nposmak.external_api_bot.botState_control.BotState;
import nposmak.external_api_bot.botState_control.InputMessageHandler;
import nposmak.external_api_bot.config.Icons;
import nposmak.external_api_bot.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;



@Component
public class MenuHandler implements InputMessageHandler {

    private MenuService menuService;

    public MenuHandler(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public SendMessage handle(Message message) {

        return menuService.getMenuMessage(message.getChatId(),"ПРИВЕТ! "+ Icons.HI +" \n " +
                "бот ищет только прямые поезда (без пересадок), для поика нажмите <НАЙТИ ПОЕЗДА>\n" +
                "ps (некоторые станции могут иметь в базе ржд не самое очевидное название," +
                " для уточнения воспользуйтесь справочником) ");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MENU;
    }
}

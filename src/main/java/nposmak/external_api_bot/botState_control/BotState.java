package nposmak.external_api_bot.botState_control;

import java.io.Serializable;

public enum BotState implements Serializable {

    SEARCH_FOR_TRAIN,
    ASK_DEPARTURE_STATION,
    ASK_ARRIVAL_STATION,
    ASK_DEPARTURE_DATE,
    CHECK_DEPARTURE_DATE,
    COMPLETE,
    STATION_BOOK,
    ASK_STATION_NAME,
    STATION_NAME_RECIVED,
    MENU

}

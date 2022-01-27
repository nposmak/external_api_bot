package nposmak.external_api_bot.chatCache;

import java.util.Optional;

public interface StationCacheInterface {

    Optional<String> getStationName(String stationNameParam);

    Optional<Long> getStationCode(String stationNameParam);

    void addStationToCache(String stationName, long stationCode);
}

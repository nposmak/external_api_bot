package nposmak.external_api_bot.chatCache;


import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Getter
public class StationCache implements StationCacheInterface{

    private Map<String, Long> stationCodeCache = new HashMap<>();

    @Override
    public Optional<String> getStationName(String stationName) {
        return stationCodeCache.keySet().stream().filter(station_Name-> stationName.equals(stationName)).findFirst();
    }

    @Override
    public Optional<Long> getStationCode(String stationName) {
        return Optional.ofNullable(stationCodeCache.get(stationName));
    }

    @Override
    public void addStationToCache(String stationName, long stationCode) {
        stationCodeCache.put(stationName,stationCode);
    }


}

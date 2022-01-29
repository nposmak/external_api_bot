package nposmak.external_api_bot.service;

import lombok.Getter;
import lombok.Setter;
import nposmak.external_api_bot.chatCache.StationCache;
import nposmak.external_api_bot.dto.StationCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
public class StationBookCommunication {

    private RestTemplate restTemplate;
    private StationCache stationCache;

    public StationBookCommunication(RestTemplate restTemplate, StationCache stationCache){
        this.restTemplate = restTemplate;
        this.stationCache = stationCache;
    }

    public List<StationCode> processStationBookRequest(String stationName){

        String stationNameParam = stationName.toUpperCase();
        ResponseEntity<StationCode[]> responseEntity =
                restTemplate.getForEntity(
                        "https://pass.rzd.ru/suggester?stationNamePart="+
                                stationNameParam +
                                "&lang=ru&compactMode=y",
                        StationCode[].class);

        StationCode[] stations = responseEntity.getBody();
        System.out.println(Arrays.toString(stations));

        if(stations == null){
            return Collections.emptyList();
        }

        for(StationCode stationCode : stations){

            stationCache.addStationToCache(stationCode.getStationName(), stationCode.getStationCode());

        }

        return List.of(stations);
    }
}

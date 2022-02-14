package nposmak.external_api_bot.service;


import lombok.Getter;
import lombok.Setter;
import nposmak.external_api_bot.chatCache.StationCache;
import nposmak.external_api_bot.dto.StationCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Getter
@Setter
public class StationCodeCommunication {

    private RestTemplate restTemplate;
    private StationCache stationCache;

    public StationCodeCommunication(RestTemplate restTemplate, StationCache stationCache){
        this.restTemplate = restTemplate;
        this.stationCache = stationCache;
    }

    public long getStationCode(String stationName){

        String stationNameParam = stationName.toUpperCase();
        Optional<Long> stationCodeOptional = stationCache.getStationCode(stationNameParam);
        if (stationCodeOptional.isPresent())
            return stationCodeOptional.get();


        if (processCodeRequest(stationNameParam).isEmpty()){
            return -1;
        }

        return stationCache.getStationCode(stationNameParam).orElse((long) -1);
    }

    private Optional <StationCode[]> processCodeRequest(String stationName){

        String stationNameParam = stationName.toUpperCase();

        ResponseEntity<StationCode[]> responseEntity =
                    restTemplate.getForEntity(
                            "https://pass.rzd.ru/suggester?stationNamePart="+
                               stationNameParam +
                                    "&lang=ru&compactMode=y",
                            StationCode[].class);

        StationCode[] allCodes = responseEntity.getBody();
        System.out.println(Arrays.toString(allCodes));

        if(allCodes == null){
            return Optional.empty();
        }

        for(StationCode stationCode : allCodes){
//            if(stationCode.getStationName().equals(stationNameParam)){
                stationCache.addStationToCache(stationCode.getStationName(), stationCode.getStationCode());
            //}
        }

        return Optional.of(allCodes);
    }
}




















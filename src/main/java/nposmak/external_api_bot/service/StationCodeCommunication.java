package nposmak.external_api_bot.service;


import lombok.Getter;
import lombok.Setter;
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

    public StationCodeCommunication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

     public long getStationCode(String stationName){
        String stationNameParam = stationName.toUpperCase();
        ResponseEntity<StationCode[]> responseEntity =
                restTemplate.getForEntity(
                        "https://pass.rzd.ru/suggester?stationNamePart="+
                                stationNameParam +
                                "&lang=ru&compactMode=y",
                        StationCode[].class);
        StationCode[] allStations = responseEntity.getBody();
        System.out.println(Arrays.toString(allStations));
        if(allStations == null){
            return -1;
        }
        long code=0;
        for(StationCode codes : allStations){
            if(codes.getStationName().equals(stationNameParam)){
                code = codes.getStationCode();
            }
        }
        return code;
    }

}




















package nposmak.external_api_bot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import nposmak.external_api_bot.dto.TrainInfo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
@Service
public class TrainInfoCommunication{

    private final RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public TrainInfoCommunication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<TrainInfo> getTrainInfo(long departureStationCode, long arrivalStationCode, Date departureDate){

        String departureDateString = simpleDateFormat.format(departureDate);

        System.out.println(departureStationCode);
        System.out.println(arrivalStationCode);
        System.out.println(departureDateString);

        String ridURL = "https://pass.rzd.ru/timetable/public/?layer_id=5827" +
                "&dir=0"+
                "&code0="+departureStationCode +
                "&code1="+arrivalStationCode +
                "&tfl=3&checkSeats=1" +
                "&dt0="+departureDateString+"&md=0";

        String trainInfoURL = "https://pass.rzd.ru/timetable/public/ru?layer_id=5827";

        ResponseEntity<String> responseForRIDRequest =
                restTemplate.exchange(
                        ridURL,
                        HttpMethod.GET,
                        null,
                        String.class
                );
        String ridResponseBody = responseForRIDRequest.getBody();
        System.out.println(ridResponseBody);

//        Pattern pattern = Pattern.compile("[0-9]{11}");
//        Matcher matcher = pattern.matcher(ridResponseBody);
//
//        long ridForSession = Long.parseLong(matcher.group());
//        /**Проверить тут */


        String rid = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(ridResponseBody.trim());
            JsonNode ridNode = jsonNode.get("RID");
            if (ridNode != null) {
                rid = ridNode.asText();
            }else {
                rid = "00000000000";
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        long ridForSession = Long.parseLong(rid);

        List<String> sessionRIDCookie = responseForRIDRequest.getHeaders().get("Set-Cookie");

        String jSessionId = sessionRIDCookie.get(sessionRIDCookie.size() - 1);
        jSessionId = jSessionId.substring(jSessionId.indexOf("=") + 1, jSessionId.indexOf(";"));


        HttpHeaders trainInfoHeaders = new HttpHeaders();
        trainInfoHeaders.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        trainInfoHeaders.add("Cookie", "lang=ru");
        trainInfoHeaders.add("Cookie", "JSESSIONID="+jSessionId);
        trainInfoHeaders.add("Cookie", "AuthFlag=false");

        MultiValueMap<String, Long> requestBody = new LinkedMultiValueMap<String, Long>();
        requestBody.add("rid", ridForSession);
        HttpEntity formEntity = new HttpEntity<MultiValueMap<String, Long>>(requestBody, trainInfoHeaders);

        ResponseEntity<String> responseForTrainRequest =
                restTemplate.exchange(
                        trainInfoURL,
                        HttpMethod.POST,
                        formEntity,
                        String.class
                );
        while (checkIfResponseIsRIDAgain(responseForTrainRequest)) {
            responseForTrainRequest =
                    restTemplate.exchange(
                            trainInfoURL,
                            HttpMethod.POST,
                            formEntity,
                            String.class
                    );
        }

        String resultInfo = responseForTrainRequest.getBody();

        List<TrainInfo> trainInfoList =null;
        try {
            JsonNode trainsNode = objectMapper.readTree(resultInfo).path("tp").findPath("list");
            trainInfoList = Arrays.asList(objectMapper.readValue(trainsNode.toString(), TrainInfo[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(trainInfoList);

        return Objects.isNull(trainInfoList) ? Collections.emptyList() : trainInfoList;
    }

    private boolean checkIfResponseIsRIDAgain(ResponseEntity<String> responseForTrainRequest) {

        if (responseForTrainRequest.getBody() == null) {
            return true;
        }

        return responseForTrainRequest.getBody().contains("\"result\":\"RID");
    }

    private Optional<String> parseRID(String jsonRespBody) {
        String rid = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonRespBody.trim());
            JsonNode ridNode = jsonNode.get("RID");
            if (ridNode != null) {
                rid = ridNode.asText();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(rid);
    }
}
package nposmak.external_api_bot.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainCarInfo {

    @JsonProperty(value = "type")
    private String carType;

    @JsonProperty(value = "freeSeats")
    private int freeSeats;

    @JsonProperty(value = "tariff")
    private int tariff;


}

package nposmak.external_api_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrainInfo {
    @JsonProperty(value = "number")
    private String trainNumber;

    @JsonProperty(value = "brand")
    private String trainBrand;

    @JsonProperty(value = "station0")
    private String stationDeparture;

    @JsonProperty(value = "station1")
    private String stationArrival;

    @JsonProperty(value = "date0")
    private String dateDeparture;

    @JsonProperty(value = "date1")
    private String dateArrival;

    @JsonProperty(value = "time0")
    private String timeDeparture;

    @JsonProperty(value = "time1")
    private String timeArrival;

    @JsonProperty(value = "timeInWay")
    private String timeInWay;

    @JsonProperty(value = "cars")
    private List<TrainCarInfo> cars;

}

package nposmak.external_api_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationCode {

    @JsonProperty(value = "n")
    private String stationName;
    @JsonProperty(value = "c")
    private long stationCode;


}

package nposmak.external_api_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationCode {

    @JsonProperty(value = "n")
    private String stationName;
    @JsonProperty(value = "c")
    private long stationCode;

//    public StationCode(String stationName, long stationCode) {
//        this.stationName = stationName;
//        this.stationCode = stationCode;
//    }
//
//    public StationCode() {
//    }
//
//    public String getStationName() {
//        return stationName;
//    }
//
//    public void setStationName(String stationName) {
//        this.stationName = stationName;
//    }
//
//    public long getStationCode() {
//        return stationCode;
//    }
//
//    public void setStationCode(long stationCode) {
//        this.stationCode = stationCode;
//    }
//
//    @Override
//    public String toString() {
//        return "StationCode{" +
//                "stationName='" + stationName + '\'' +
//                ", stationCode=" + stationCode +
//                '}';
//    }
}

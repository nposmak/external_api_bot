package nposmak.external_api_bot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RidCode {

    @JsonProperty(value = "RID")
    long RID;

    public RidCode() {
    }

    public RidCode(long RID) {
        this.RID = RID;
    }

    public long getRID() {
        return RID;
    }

    public void setRID(long RID) {
        this.RID = RID;
    }

    @Override
    public String toString() {
        return "RidCode{" +
                "RID=" + RID +
                '}';
    }
}

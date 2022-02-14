package nposmak.external_api_bot.redisCache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
public class UsersData implements Serializable {
    private String departureStation;
    private String arrivalStation;
    private long departureStationCode;
    private long arrivalStationCode;
    private Date dateDepart;
}

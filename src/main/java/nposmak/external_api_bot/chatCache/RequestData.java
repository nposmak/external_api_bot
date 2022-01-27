package nposmak.external_api_bot.chatCache;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestData {

    private String departureStation;
    private String arrivalStation;
    private long departureStationCode;
    private long arrivalStationCode;
    private Date dateDepart;
}

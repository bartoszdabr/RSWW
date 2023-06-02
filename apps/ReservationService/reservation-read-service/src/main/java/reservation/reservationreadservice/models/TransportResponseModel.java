package reservation.reservationreadservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TransportResponseModel {

    private List<TransportModel> matchingTransports;
}

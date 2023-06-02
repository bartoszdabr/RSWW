package transport.models;

import lombok.Builder;
import lombok.Getter;
import transport.read.entity.Transport;

import java.util.List;

@Builder
@Getter
public class TransportResponseModel {

    private final List<Transport> matchingTransports;

}

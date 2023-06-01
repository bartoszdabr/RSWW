package transport.models;

import lombok.Builder;
import transport.read.entity.Transport;

import java.util.List;

@Builder
public class TransportResponseModel {

    private final List<Transport> matchingTransports;

}

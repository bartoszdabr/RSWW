package transport.write.commands;

import lombok.Data;

@Data
public class AddReservationCommand {
    private String id;
    private String transportId;
    private String userName;
    private int numberOfPeople;
}

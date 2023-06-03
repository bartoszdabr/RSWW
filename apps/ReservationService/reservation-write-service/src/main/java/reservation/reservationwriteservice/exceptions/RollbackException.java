package reservation.reservationwriteservice.exceptions;

public class RollbackException extends Exception{

    public RollbackException(String msg) {
        super(msg);
    }
}

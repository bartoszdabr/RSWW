package generator.randomeventgenerator;

public class EventPayload {
    public String makeTransportReservation(String id, String transportId, String username, String numberOfPeople) {
        return "{\n" +
                "  \"id\":\"abc\",\n" +
                "  \"transportId\":\"transportId\",\n" +
                "  \"userName\":\"abc\",\n" +
                "  \"numberOfPeople\":2,\n" +
                "  \"type\":\"ADD\"\n" +
                "}";
    }

    public String cancelTransportReservation(String id, String transportId, String username, String numberOfPeople) {
        return "{\n" +
                "  \"id\":\"" + id + "\",\n" +
                "  \"transportId\":\"" + transportId + "\",\n" +
                "  \"userName\":\"" + username + "\",\n" +
                "  \"numberOfPeople\":" + numberOfPeople + ",\n" +
                "  \"type\":\"CANCEL\"\n" +
                "}";
    }

    public String addHotelReservation(String guid, String username, String roomReservationId, String numOfPeople) {
        return "{\"guid\":\"" + guid + "\",\n" +
                "\"username\":\"" + username + "\",\n" +
                "\"roomReservationId\":\"" + roomReservationId + "\",\n" +
                "\"numOfPeople\":" + numOfPeople + ",\"type\":\"ADD\"}";
    }

    public String deleteHotelReservation(String guid, String username, String roomReservationId, String numOfPeople) {
        return "{\"guid\":\"" + guid + "\",\n" +
                "\"username\":\"" + username + "\",\n" +
                "\"roomReservationId\":\"" + roomReservationId + "\",\n" +
                "\"numOfPeople\":" + numOfPeople + ",\"type\":\"CANCEL\"}";
    }
}
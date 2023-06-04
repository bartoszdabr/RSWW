## Reservation Service

To add new reservation make `POST` call on `/api/reservations/v1/write/reservations`
with the following payload
```json
{
  "username": "yourUsername",
  "transportId": "transportId",
  "roomReservationId": 0, // id of hotel reservation
  "ageGroupsSize": { // num of people in each age group
    "lessThan3YearsOld": 1,
    "lessThan10YearsOld": 0,
    "lessThan18YearsOld": 0,
    "adult": 2
  }
}
```

To find offers make a `GET` call on `/api/reservations/v1/read` with following specification parameters
- startLocation - string pointing to from where users want to start their trip, eg. `Warszawa`
- destinationLocation - string specifying destination location, eg. `Grecja`
- startDate - start of date search range for offers, eg. `2023-05-31`
- endDate - end of date search range for offers, eg. `2023-06-15`
- adults - integer with number of participating adults, eg. `2`
- under3YearsOld - integer with number of participating kids under 3 years old, eg. `1`
- under10YearsOld - integer with number of participating kids under 10 years old, eg. `0`
- under18YearsOld - integer with number of participating kids under 18 years old, eg. `3`

Example call: `/api/reservations/v1/read/offers?startLocation=Katowice&destinationLocation=Grecja&startDate=2023-05-31&endDate=2023-06-11&adults=2&under10YearsOld=1`

To check if any reservation for current offer was made make a `GET` request on `/api/reservations/v1/read/notifications` with the following parameters:
- hotelId - string with hotel id to check
- transportId - string with transport id to check 

Example call: `/api/reservations/v1/read/notifications?hotelId=4a94f53c-38c6-423d-9ea9-96873cf335d4&transportId=4a94f53c-38c6-423d-9ea9-96873cf335d3`

Example response
```json
{
  "timestamp": "2023-06-09T11:01:09.991Z"
}
```

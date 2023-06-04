## Transport Service

Send message in the following format to make reservation. Default queue = `transportQueue` 
```json
{
"id":"reservationId",
"transportId":"transportId",
"username":"abc",
"numOfPeople":2,
"type":"add or ADD"
}

```

Send message in the following format cancel reservation.  Default queue = `transportQueue`
```json
{
  "id":"abc",
  "transportId":"transportId",
  "username":"abc",
  "numOfPeople":2,
  "type":"cancel or CANCEL"
}
```

Operation status response. Default queue=`reservationQueue`

```json
{
  "id": "reservationId",
  "status": "FAILURE"
},
{
  "id": "reservationId",
  "status": "SUCCEDED" 
}
```
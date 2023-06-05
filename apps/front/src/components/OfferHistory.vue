<template>
    <div class="container">
    <div class="row">
      <div class="col-md-6">
        <h2>Transport changes</h2>
        <table class="table">
          <thead>
            <tr>
              <th>Message</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in transportEvents">
              <td>{{ event.message }}</td>
              <td>{{ event.timestamp }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col-md-6">
        <h2>Hotel changes</h2>
        <table class="table">
          <thead>
            <tr>
              <th>Message</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in hotelEvents">
              <td>{{ event.message }}</td>
              <td>{{ event.timestamp }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import {getBackendUrl} from './utils.js';
import axios from 'axios';

export default {
  data() {
    return {
        hotelId: '',
        transportId: '',
        hotelEvents: [],
        transportEvents: []
    }
  },
  mounted() {
    this.hotelId = this.$route.query.hotelId;
    this.transportId = this.$route.query.transportId;
    this.fetchHotelEvents();
    this.fetchTransportEvents();
  
    this.hotelCallInterval = setInterval(() => {
      this.fetchHotelEvents();
    }, 1000);
    
    this.transportCallInterval = setInterval(() => {
      this.fetchTransportEvents();
    }, 1000);
  },
  beforeDestroy() {
    clearInterval(this.hotelCallInterval);
    clearInterval(this.transportCallInterval);
  },

  methods: {
    fetchHotelEvents() {
      this.formatHotelEvents(JSON.parse(mock));
      const url = `${getBackendUrl()}/api/hotels/v1/read/events/${this.hotelId}`;
        axios.get(url)
        .then(response => {
            this.formatHotelEvents(response.data);
        })
        .catch(error => {
          console.error(error);
        });
    },
    fetchTransportEvents() {
        const url = `${getBackendUrl()}/api/transport/v1/write/events/${this.transportId}`;
        axios.get(url)
        .then(response => {
            this.formatTransportEvents(response.data.transportEvents)
        })
        .catch(error => {
          console.error(error);
        });
    },

    formatTransportEvents(transportEvents) {
        let events = []
        transportEvents.forEach(event => {
            const eventJson = JSON.parse(event.eventJson);
            const timestamp = eventJson.timeStamp;
            if (event.type == 'CREATE') {
                const availableSeats = eventJson.availableSeats;
                events.push({
                    message: 'The new transport was created with ' + availableSeats + ' available seats.',
                    timestamp: timestamp
                })
            } else if(event.type == 'ADD') {
                const numberOfPeople = eventJson.numberOfPeople;
                events.push({
                    message: 'The number of seats has decreased -' + numberOfPeople + ' new seats.',
                    timestamp: timestamp
                })
            } else {
                const numberOfPeople = eventJson.numberOfPeople;
                events.push({
                    message: 'The number of seats has increased +' + numberOfPeople + ' seats.',
                    timestamp: timestamp
                })
            }
        });
        this.transportEvents = events;
    },
    formatHotelEvents(hotelEvents) {
      let events = []
      console.log(hotelvents);
      hotelEvents.forEach(event => {
        const timestamp = event.dateTime;
        const data = JSON.parse(event.data);
        if (event.type == 1) {
          events.push({
              message: 'The hotel offer has been created: ' + data.HotelName + 'f rom date ' + data.FromDate + ' to date ' + data.ToDate + ', number of places: ' + data.NumberOfGuestsInAllRooms,
              timestamp: timestamp
          })
        } else if (event.type == 0) {
          events.push({
              message: 'The  room for' + data.NumberOfGuestsInAllRooms + ' was reserved.',
              timestamp: timestamp
          })

        } else if (event.type == 2) {
          events.push({
              message: 'The reservation for room for' + data.NumberOfGuestsInAllRooms + ' was canceled.',
              timestamp: timestamp
          })
        }
      });
      this.hotelEvents = events;
    }
  }
};

</script>
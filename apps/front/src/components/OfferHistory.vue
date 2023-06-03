<template>
    <h1>Offer history</h1>
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
        transportEvents: [],
    }
  },
  mounted() {
    this.hotelId = this.$route.query.hotelId;
    this.transportId = this.$route.query.transportId;
    this.fetchHotelEvents();
    this.fetchTransportEvents();
  
    this.hotelCallInterval = setInterval(() => {
      this.fetchHotelEvents();
    }, 1000000);
    
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
      const url = `${getBackendUrl()}/api/hotel/v1/write/events/${this.hotelId}`;
        axios.get(url)
        .then(response => {
        
        })
        .catch(error => {
          console.error(error);
        });
    },
    fetchTransportEvents() {
        //const url = `${getBackendUrl()}/api/transport/v1/write/events/${this.transportId}`;
        const url = 'http://localhost:9011/api/transport/v1/write/events/02050f21-7561-4e12-ac59-e3c9b80cbed5';
        axios.get(url)
        .then(response => {
            //console.log(response.data);
            this.formatTransportEvents(response.data.transportEvents)
        })
        .catch(error => {
          //console.error(error);
        });
    },

    formatTransportEvents(transportEvents) {
        //console.log(transportEvents);
        const events = 

        transportEvents.forEach(event => {
            const eventJson = JSON.parse(event.eventJson.replace(/\\/g, ''));
            const timestamp = event.timeStamp;
            const availableSeats = eventJson.availableSeats;
            //console.log(event.type)
            if (event.type == 'CREATE') {
                console.log(eventJson.timeStamp);
                console.log(eventJson.availableSeats);
            }
            
            //console.log(timestamp);
        });
    }
  }
};

</script>
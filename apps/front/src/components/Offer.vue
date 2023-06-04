<template>
<div class="container">
  <div class="row">
    <div class="col d-flex justify-content-center mb-4">
      <img src="top-image.jpg" alt="Hotel image" class="top-image">
    </div>
  </div>
  <div class="row">
    <div class="col">
      <h2>Transport Data</h2>
      <div class="transport-data">
        <h4>Date: {{ transportDate }}</h4>
        <h4>Source Place: {{ transportSourcePlace }}</h4>
        <h4>Destination Place: {{ transportDestinationPlace }}</h4>
        <h4>Available Seats: {{ transportAvailableSeats }}</h4>
      </div>
      <div class="button-group mt-4">
        <button class="btn btn-primary">Show Offer History</button>
        <button class="btn btn-success">Order Offer</button>
      </div>
    </div>
    <div class="col">
      <h2>Hotel Data</h2>
      <div class="hotel-data">
        <h4>Hotel Name: {{ hotelName }}</h4>
        <h4>Address: {{ hotelAddress }}</h4>
        <h4>Rating: {{ hotelRating }}</h4>
        <h4>Price: {{ hotelPrice }}</h4>
      </div>
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
        transportAvailableSeats: 0,
        transportDate: '',
        transportSourcePlace: '',
        transportDestinationPlace: '',
    }
  },
  mounted() {
    this.hotelId = this.$route.query.hotelId;
    this.transportId = this.$route.query.transportId;
    this.fetchHotelData();
    this.fetchTransportData();

    this.hotelCallInterval = setInterval(() => {
      this.fetchHotelData();
    }, 2000);
    
    this.transportCallInterval = setInterval(() => {
      this.fetchTransportData();
    }, 2000);
  },
  beforeDestroy() {
    clearInterval(this.hotelCallInterval);
    clearInterval(this.transportCallInterval);
  },
  methods: {
    fetchHotelData() {
        
    },
    fetchTransportData() {
        const url = `${getBackendUrl()}/api/transport/v1/read/transports/${this.transportId}`;
        console.log(url);
        axios.get(url)
        .then(response => {
          console.log(response.data);
          this.transportAvailableSeats = response.data.availableSeats;
          this.transportDate = response.data.date;
          this.transportSourcePlace = response.data.sourcePlace;
          this.transportDestinationPlace = response.data.destinationPlace;
        })
        .catch(error => {
          console.error(error);
        });

    }
  }
}
</script>
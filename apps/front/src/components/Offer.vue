<template>
<div class="container">
  <div class="container">
    <div id="image-slider" class="carousel slide pt-4" data-ride="carousel">
      <div class="carousel-inner">
        <div v-for="(image, index) in imageLinks" :key="index" :class="{ 'carousel-item': true, 'active': index === currentImage }">
          <img :src="image" class="d-block w-100" alt="Slider Image">
        </div>
      </div>
      <a class="carousel-control-prev" href="#image-slider" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="carousel-control-next" href="#image-slider" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
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
        <button class="btn btn-primary" @click="showOfferHistory">Show Offer History</button>
        <button class="btn btn-success" @click="orderOffer">Order Offer</button>
      </div>
    </div>
    <div class="col">
      <h2>Hotel Data</h2>
      <div class="hotel-data">
        <h4>Hotel Name: {{ hotelName }}</h4>
        <h4>Opinion Score: {{ opinionScone }}</h4>
        <h4>Stars: {{ stars }}</h4>
        <div v-for="(value, key) in description" :key="key">
          <h3>{{ key }}</h3>
          <p v-if="value">{{ value }}</p>
          <p v-else>No information available</p>
      </div>
      <h4>Tags:</h4>
      <ul>
        <li v-for="(tag, index) in tags" :key="index">{{ tag }}</li>
      </ul>
      <h4>Free places: {{ freePlaces }}</h4>
      </div>
    </div>
  </div>
  <div>
    <h1 v-if="purchaseMessage">{{ purchaseMessage }}</h1>
  </div>
  <div>
    <h1 v-if="reservationStatus">{{ reservationStatus }}</h1>
  </div>
</div>


</template>

<script>
import {getBackendUrl} from './utils.js';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';

export default {
  data() {
    return {
        hotelId: '',
        transportId: '',
        transportAvailableSeats: 0,
        transportDate: '',
        transportSourcePlace: '',
        transportDestinationPlace: '',
        hotelName:'',
        opinionScone:0,
        stars:0,
        description: {},
        imageLinks: [],
        currentImage:0,
        tags: [],
        freePlaces: 0,
        purchaseMessage: '',
        lastPurchaseTimestamp: '2000-01-01T20:37:24.670918Z',
        reservationStatus: '',
        peoples: {},
        fromDate: '2000-01-01',
        toDate: '2000-01-01'
    }
  },
  mounted() {
    this.hotelId = this.$route.query.hotelId;
    this.transportId = this.$route.query.transportId;
    this.peoples = this.$route.query.data;
    console.log(this.peoples);
    this.fetchHotelData();
    this.fetchTransportData();

    this.hotelCallInterval = setInterval(() => {
      this.fetchHotelData();
    }, 1000);

    this.transportCallInterval = setInterval(() => {
      this.fetchTransportData();
    }, 1000);
    this.imageCallInterval = setInterval(() => {
      this.slideImage();
    }, 2000);
    this.purchaseCallInterval = setInterval(() => {
      this.lookForNewPurchase();
    }, 1000);
  },
  beforeDestroy() {
    clearInterval(this.hotelCallInterval);
    clearInterval(this.transportCallInterval);
    clearInterval(this.imageCallInterval);
    clearInterval(this.purchaseCallInterval);
  },
  methods: {
    fetchHotelData() {
      const url = `${getBackendUrl()}/api/hotel/v1/read/hotel/${this.hotelId}`;
        axios.get(url)
        .then(response => {
          this.hotelName = response.data.hotelName;
          if (this.imageLinks.length < 1) {
            this.imageLinks = response.data.images;
          }
          this.description = response.data.description;
          this.tags = response.data.tags;
          this.freePlaces = response.data.numberOfGuestsInAllRoom;
          this.fromDate = response.data.fromDate;
          this.toDate = response.data.toDate;
        })
        .catch(error => {
          console.error(error);
        });
    },
    fetchTransportData() {
        const url = `${getBackendUrl()}/api/transport/v1/read/transports/${this.transportId}`;
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
    },
    lookForNewPurchase() {

        const url = `${getBackendUrl()}/api/reservations/v1/read/notifications?hotelId=${this.hotelId}&transportId=${this.transportId}`;

        let newPurchaseTimestamp; 
        axios.get(url)
        .then(response => {
          newPurchaseTimestamp  = response.data.timestamp;
        })
        .catch(error => {
          console.error(error);
        });

        this.purchaseMessage = '';

        const lastPurchaseDate = new Date(this.lastPurchaseTimestamp);
        const newPurchaseDate = new Date(newPurchaseTimestamp);

        if (newPurchaseDate > lastPurchaseDate) {
          this.purchaseMessage = 'Someone purchased this offer';
          this.lastPurchaseTimestamp = newPurchaseTimestamp;
        }
    },
    slideImage() {
      this.currentImage += 1;
      this.currentImage = this.currentImage % this.imageLinks.length;
    },
    showOfferHistory() {
      this.$router.push({ name: 'OfferHistory', query: { hotelId: this.hotelId, transportId: this.transportId} });
    },
    orderOffer() {
      
      const startDateObj = new Date(this.fromDate);
      const endDateObj = new Date(this.toDate);

      // Calculate the difference in milliseconds
      const diffInMs = Math.abs(endDateObj - startDateObj);

      // Convert milliseconds to days
      const days = Math.ceil(diffInMs / (1000 * 60 * 60 * 24));

      console.log(days);

      this.reservationStatus = '';
      const apiUrl = `${getBackendUrl()}/api/reservations/v1/write/reservations`;
      const requestBody =  {
        username: sessionStorage.getItem('username'),
        transportId: this.transportId,
        roomReservationId: this.hotelId,  
        ageGroupsSize: {
          lessThan3YearsOld: this.peoples.numberOfChildren3,
          lessThan10YearsOld: this.peoples.numberOfChildren10,
          lessThan18YearsOld: this.peoples.numberOfChildren18,
          adult: this.peoples.numberOfAdults
        },
        numOfDays: days
      }
      axios.post(apiUrl, requestBody)
      .then(response => {
        this.reservationStatus = 'The offer has been successfully purchased';
      })
      .catch(error => {
        this.reservationStatus = 'Given offer cannot be purached. Please try again or choose different offer';
        console.error(error);
      });
    }
  }
}
</script>

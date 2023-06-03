<template>
  <div id="travels" class="container">
  <form @submit.prevent="submitForm">
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="destination">Destination:</label>
        <input type="text" class="form-control" v-model="destination" placeholder="Enter destination">
      </div>
      <div class="form-group col-md-6">
        <label for="placeOfDeparture">Place of Departure:</label>
        <input type="text" class="form-control" v-model="placeOfDeparture" placeholder="Enter place of departure">
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="departureDate">Date of Departure:</label>
        <input type="date" class="form-control" v-model="departureDate">
      </div>
      <div class="form-group col-md-6">
        <label for="endDate">End date:</label>
        <input type="date" class="form-control" v-model="endDate">
      </div>
    </div>
    <div class="form-row">
      <div class="form-group col-md-3">
        <label for="numberOfAdults">Adults:</label>
        <input type="number" class="form-control" v-model="numberOfAdults" min="0">
      </div>
      <div class="form-group col-md-3">
        <label for="numberOfChildren3">Children (1-3 years):</label>
        <input type="number" class="form-control" v-model="numberOfChildren3" min="0">
      </div>
      <div class="form-group col-md-3">
        <label for="numberOfChildren10">Children (4-10 years):</label>
        <input type="number" class="form-control" v-model="numberOfChildren10" min="0">
      </div>
      <div class="form-group col-md-3">
        <label for="numberOfChildren18">Children (11-18 years):</label>
        <input type="number" class="form-control" v-model="numberOfChildren18" min="0">
      </div>
    </div>
    <button type="submit" class="btn btn-primary" @click="submitForm">Submit</button>
  </form>


    <div id="offer-list" class="container py-4">
    <div class="row">
      <div class="col-md-12">
        <div class="card">
          <div class="card-body">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Hotel Name</th>
                  <th>Hotel location</th>
                  <th>Source place</th>
                  <th>Destination place</th>
                  <th>Transport Date</th>
                  <th>Cost</th>
                </tr>
              </thead>
              <tbody>
                <template v-for="offer in offers">
                    <tr v-for="transport in offer.transports" :key="transport.id" @click="handleOfferClick(offer.hotelId, transport.id)">
                    <td>{{ offer.name }}</td>
                    <td>{{ offer.location }}</td>
                    <td>{{ transport.sourcePlace }}</td>
                    <td>{{ transport.destinationPlace }}</td>
                    <td>{{ transport.date }}</td>
                    <td>{{ offer.cost }}</td>
                  </tr>
                </template>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

  </div>


</template>



<script>
import { toQueryString } from './utils.js';
import {getBackendUrl} from './utils.js';
import axios from 'axios';

export default {
  name: 'TravelForm',
  data() {
    return {
      destination: '',
      placeOfDeparture: '',
      departureDate: '',
      endDate:'',
      numberOfAdults: 0,
      numberOfChildren3: 0,
      numberOfChildren10: 0,
      numberOfChildren18: 0,
      offers: []
    };
  },
  methods: {
    handleOfferClick(hotelId, transportId) {
      this.$router.push({ name: 'Offer', query: { hotelId: hotelId, transportId: transportId} });
    },
    submitForm() {
      const queryParams = {};

      if (this.destination) {
        queryParams.destinationLocation = this.destination;
      }

      if (this.placeOfDeparture) {
        queryParams.startLocation = this.placeOfDeparture;
      }

      if (this.departureDate) {
        queryParams.startDate = this.departureDate;
      }
      let numOfPeople = 0;

      if (this.numberOfAdults > 0) {
        numOfPeople += parseInt(this.numberOfAdults);
      }

      if (this.numberOfChildren3 > 0) {
        numOfPeople += parseInt(this.numberOfChildren3);
      }

      if (this.numberOfChildren10 > 0) {
        numOfPeople +=  parseInt(this.numberOfChildren10);
      }

      if (this.numberOfChildren18 > 0) {
        numOfPeople +=  parseInt(this.numberOfChildren18);
      }

      if(numOfPeople>0) {
        queryParams.numOfPeople = numOfPeople;
      }

      this.makeApiRequest(queryParams);
    },
    makeApiRequest(queryParams) {
      const apiUrl = `${getBackendUrl()}${toQueryString(queryParams)}`;
      console.log(apiUrl);

      let mock = '[{"hotelId":"123","name":"hotelName","rating":0,"stars":0,"location":"Turcja","startDate":"2023-06-02","endDate":"2023-06-02","numOfPeople":0,"transports":[{"id":"02050f21-7561-4e12-ac59-e3c9b80cbed5","sourcePlace":"string","destinationPlace":"string","date":"2023-06-02","availableSeats":0},{"id":"transport2","sourcePlace":"string","destinationPlace":"string","date":"2023-06-02","availableSeats":0}],"cost":0}]';
      this.offers = JSON.parse(mock);

      console.log(this.offers);
    }
  },
};
</script>

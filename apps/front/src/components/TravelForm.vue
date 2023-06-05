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
  <div>
      <h1 >{{ errorMessage }}</h1>
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
      offers: [],
      errorMessage:'',
      peoples: {}
    };
  },
  methods: {
    handleOfferClick(hotelId, transportId) {
      this.$router.push({ name: 'Offer', query: { hotelId: hotelId, transportId: transportId, data:this.peoples} });
    },
    submitForm() {
      const queryParams = {};
      this.errorMessage = '';
      if (this.destination) {
        queryParams.destinationLocation = this.destination;
      }

      if (this.placeOfDeparture) {
        queryParams.startLocation = this.placeOfDeparture;
      }

      if (this.departureDate) {
        queryParams.startDate = this.departureDate;
      }

      if (this.endDate) {
        queryParams.endDate = this.endDate;
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

      if(numOfPeople<=0) {
        this.errorMessage = 'Please specify valid number of people';
      } else {

        this.peoples = {
          numberOfAdults: parseInt(this.numberOfAdults),
          numberOfChildren3: parseInt(this.numberOfChildren3),
          numberOfChildren10: parseInt(this.numberOfChildren10),
          numberOfChildren18: parseInt(this.numberOfChildren18)
        }
        if (isNaN(this.peoples.numberOfAdults) || isNaN(this.peoples.numberOfChildren3) || isNaN(this.peoples.numberOfChildren10) || isNaN(this.peoples.numberOfChildren18)) {
          this.errorMessage = 'Please specify valid number of people';
        } else {
        this.makeApiRequest(queryParams);
        }
      }
    },
    makeApiRequest(queryParams) {
      const apiUrl = `${getBackendUrl()}/api/reservations/v1/read/offers?${toQueryString(queryParams)}`;
      console.log(apiUrl);
      axios.get(apiUrl)
        .then(response => {
            this.offers = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    }
  },
};
</script>

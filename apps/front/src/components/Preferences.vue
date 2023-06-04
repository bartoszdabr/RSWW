<template>
  <div class="container">
    <div class="row">
      <div class="col-md-6">
        <h2>Top hotels</h2>
        <table class="table">
          <thead>
            <tr>
              <th>Hotel</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="hotel in topHotels">
              <td>{{ hotel }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col-md-6">
        <h2>Top destinations</h2>
        <table class="table">
          <thead>
            <tr>
              <th>Destination</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="destination in topDestinations">
              <td>{{ destination }}</td>
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
  name: 'Preferences',
  data() {
    return {
        topHotels: [],
        topDestinations: [],
    }
  },
  mounted() {
    this.fetchPreferences();
  
    this.preferencesCallInterval = setInterval(() => {
      this.fetchPreferences();
    }, 1000);
    
  },
  beforeDestroy() {
    clearInterval(this.preferencesCallInterval);
  },

  methods: {
    fetchPreferences() {
      const url = `${getBackendUrl()}/api/hotel/v1/write/preferences`;
        axios.get(url)
        .then(response => {
          //handle preferences
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>
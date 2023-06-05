<template>
  <div class="container">
    <div class="row">
      <div class="col-md-6">
        <h2>Hotels with the greatest interest</h2>
        <table class="table">
          <thead>
            <tr>
              <th>Hotel</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="hotel in topHotels">
              <td>{{ hotel.name }}</td>
              <td>{{ hotel.count }}</td>
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
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="destination in topDestinations">
              <td>{{ destination.name }}</td>
              <td>{{ destination.count }}</td>
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
      //fetch hotels
    
      const url = `${getBackendUrl()}/api/hotels/v1/read/hotels/top`;
        axios.get(url)
        .then(response => {
          this.topHotels = response.data;
        })
        .catch(error => {
          console.error(error);
        });

        //fetch directions
      const directionsUrl = `${getBackendUrl()}/api/hotels/v1/read/destination/top`;
      axios.get(directionsUrl, header)
        .then(response => {
          this.topDestinations = response.data;
        })
        .catch(error => {
          console.error(error);
      });

    }
  }
};
</script>
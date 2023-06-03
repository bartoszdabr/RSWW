<template>
    <div id="preferences" class="container">
        <h1>Top directions</h1>

        <h1>Top hotels</h1>

    </div>
</template>

<script>

export default {
  name: 'Preferences',
  data() {
    return {
        topHotels: [],
        topDirections: [],
    }
  },
  mounted() {
    this.fetchPreferences();
  
    this.preferencesCallInterval = setInterval(() => {
      this.fetchHotelEvents();
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
        
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
};
</script>
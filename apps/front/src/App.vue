<template>
  <div id="app">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.6.0/css/bootstrap.min.css">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="collapse navbar-collapse">
    <ul class="navbar-nav">
      <li class="nav-item">
        <router-link to="/" class="nav-link px-3">Home</router-link>
      </li>
      <li class="nav-item">
        <router-link to="/hello" class="nav-link px-3">About</router-link>
      </li>
      <li class="nav-item">
        <button class="btn btn-outline-light" @click="logout">Logout</button>
      </li>
    </ul>
  </div>
</nav>
    <router-view/>
  </div>
</template>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/4.6.0/js/bootstrap.min.js"></script>
<script>
export default {
  name: 'App',
  methods: {
    logout() {
      sessionStorage.clear();
      this.$router.push('/login');
    }
  }
}
</script>

<style>

</style>

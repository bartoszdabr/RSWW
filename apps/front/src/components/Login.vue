<template>
    <div class="container login-container" >
      <h2>Login</h2>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" class="form-control" id="username" v-model="username">
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" class="form-control" id="password" v-model="password">
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
      </form>
      <p v-if="errorMessage" class="text-danger mt-3">{{ errorMessage }}</p>
    </div>
  </template>
  
  <style scoped>
  .login-container {
    max-width: 400px;
    margin: 0 auto;
  }
  </style>

  <script>

  export default {
    data() {
      return {
        username: '',
        password: '',
        errorMessage: ''
      };
    },
    methods: {
      login() {
        // Define the list of predefined credentials
        const credentialsList = [
          { username: 'aaa', password: '123' },
          { username: 'bbb', password: '123' },
          { username: 'ccc', password: '123' },
          { username: 'ddd', password: '123' },
          { username: 'eee', password: '123' },
          { username: 'fff', password: '123' },
        ];
  
        // Check if the provided credentials match the list
        const match = credentialsList.find(
          cred => cred.username === this.username && cred.password === this.password
        );
  
        if (match) {
          sessionStorage.setItem('username', this.username);
          sessionStorage.setItem('password', this.password);
          this.$router.push('/');
        } else {
          this.errorMessage = 'Invalid credentials. Please try again.';
        }
      }
    }
  };
  </script>
  
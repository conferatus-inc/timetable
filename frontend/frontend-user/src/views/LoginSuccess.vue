<template>
  <v-container>

  </v-container>
</template>

<script>
import { ref } from 'vue';
import router from '@/router';
import { http } from '@/http-common.js';

const redirectUri = 'http://localhost:3001/';
var yaToken = ref(null);
const hash = window.location.hash;
if (hash.includes('#access_token=')) {
  yaToken = hash.split('=')[1];
  if (yaToken.includes('&')) {
    yaToken = yaToken.split('&')[0];
  } 
} else {
  console.log("Увы");
}

console.log('YaToken:', yaToken);

var accessToken = ref(null);

http.get('/api/v1/accounts/login', {
            headers: {
              Authorization: "Bearer " + yaToken,
              role: 'ROLE_USER'
            }})
  .then(response => {
      accessToken.value = response.data.accessToken;
      console.log('Access token:', accessToken);
      localStorage.setItem('accessToken', accessToken.value);
      router.push({ path: '/' })
  })
  .catch(e => {
    console.log(e);
  })

</script>

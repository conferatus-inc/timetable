<template>
  <v-container>
    <v-col>
    <v-card class="ma-2">
      <v-card-text>Ваш id: {{id}}</v-card-text>
    </v-card>
    <v-card class="ma-2">
      <v-card-text>Ваш логин: {{login}}</v-card-text>
    </v-card>
    <v-card class="ma-2">
      <v-card-text>Ваше имя: {{name}}</v-card-text>
    </v-card>
    <v-card class="ma-2">
      <v-btn 
      variant="flat" 
      class="ma-2"
      @click="$router.push({ path: '/' })"
      >
        Домой
      </v-btn>
      </v-card>
    </v-col>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import router from '@/router';
import { http } from '@/http-common.js';

const redirectUri = 'http://localhost:3002/';
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

const id = ref('')
const login = ref('')
const name = ref('')

console.log('YaToken:', yaToken);

var accessToken = ref(null);

http.get('/api/v1/accounts/login', {
            headers: {
              Authorization: "Bearer " + yaToken,
              role: 'ROLE_ADMIN'
            }})
  .then(response => {
      console.log(response.data)
      accessToken.value = response.data.accessToken;
      console.log('Access token:', accessToken);
      localStorage.setItem('accessToken', accessToken.value);
      // onMounted(() => {
          id.value = response.data.id
          login.value = response.data.login
          name.value = response.data.username
      // })
      console.log(id)
  })
  .catch(e => {
    console.log(e);
  })

</script>

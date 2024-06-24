<template>
  <v-container>
    <v-card class="ma-2">
      <v-btn 
      variant="flat" 
      class="ma-2"
      @click="loginWithYandex"
      >
        Войти через Яндекс
      </v-btn>
      </v-card>
  </v-container>
</template>

<script>
import { ref } from 'vue';

export default {
  setup() {
    const clientId = 'c4842a2eb8854095ac88ee089d1b0eca';
    const redirectUri = 'https://redirectmeto.com/http://localhost:3001/login_successful';
    const yandexAuthUrl = `https://oauth.yandex.ru/authorize?response_type=token&client_id=${clientId}&redirect_uri=${redirectUri}`;

    const loginWithYandex = () => {
      window.location.href = yandexAuthUrl;
    };

    return {
      loginWithYandex
    };
  }
}
</script>

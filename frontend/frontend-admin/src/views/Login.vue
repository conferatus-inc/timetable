<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">knock-knock</h2>
        <div>
          <v-text-field
            label="Логин"
            :rules="rules"
            hide-details="auto"
            v-model="login"
          ></v-text-field>

          <v-text-field 
          label="Пароль"
          type="password"
          :rules="rules"
          hide-details=true
          v-model="pass"
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>
        
        <v-btn 
        variant="flat" 
        class="ma-2"
        @click="$router.push({ path: '/' })"
        :disabled="login.length <3 || pass.length <3"
        >
          Войти
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { http } from '@/http-common';
import { ref } from 'vue'

const rules = [
  value => !!value || 'Required.',
  value => (value && value.length >= 3) || 'Min 3 characters',
]

const login = ref('')
const pass = ref('')


</script>

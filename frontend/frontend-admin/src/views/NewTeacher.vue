<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добавить преподавателя</h2>
        <div>
          <v-text-field
            label="ФИО"
            :rules="rules"
            hide-details="auto"
            v-model="name"
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>
        
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/'}"
        @click="showAlert('Преподаватель успешно добавлен')"
        :disabled="name.length < 1"
        >
          Создать
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { http } from '@/http-common';
import { ref } from 'vue'
import { showAlert } from '@/store/globalAlert.js'

const rules = [
  value => !!value || 'Required',
  value => (value.length >= 3) || 'Min 3 characters',
]

const name = ref('')

// showAlert("asdfsdf")

</script>

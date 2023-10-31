<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-1">Добро пожаловать!</h2>

        <v-spacer></v-spacer>

        <v-btn 
        variant="outlined" 
        class="ma-1"
        @click="$router.push({ path: '/group' })"
        >
          Расписание для группы
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn 
        variant="outlined" 
        class="ma-1"
        @click="$router.push({ path: '/auditory' })"
        >
          Расписание для аудитории
        </v-btn>

        <v-spacer></v-spacer>

        <v-btn 
        variant="outlined" 
        class="ma-1"
        @click="$router.push({ path: '/teacher' })"
        >
          Расписание для преподавателя
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
  // import HelloWorld from '@/components/HelloWorld.vue'
  import { ref } from 'vue'
 
</script>

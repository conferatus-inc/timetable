<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Админ страница!</h2>
        
        <v-spacer></v-spacer>

        <v-card class="ma-2" width=500>
        <v-btn 
        variant="flat" 
        class="ma-2"
        @click="$router.push({ path: '/group' })"
        >
          Расписание для группы
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2" width=500>
          <v-btn 
          variant="flat" 
          class="ma-2"
          @click="$router.push({ path: '/audience' })"
          >
            Расписание для аудитории
          </v-btn>
        </v-card>
        
        <v-spacer></v-spacer>

        <v-card class="ma-2" width=500>
        <v-btn 
        variant="flat" 
        class="ma-2"
        @click="$router.push({ path: '/teacher' })"
        >
          Расписание для преподавателя
        </v-btn>
      </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
//
</script>

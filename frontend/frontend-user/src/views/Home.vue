<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добро пожаловать!</h2>

        <v-card class="ma-2">
          <v-card-text>Добро пожаловать в Timetable. Сами вы его выбрали или его выбрали за вас, это лучший проект из оставшихся. Я такого высокого мнения о Timetable, что решил разместить свой список продуктов здесь, в Grocery Senpai, столь заботливо предоставленной Conferatus Team. Я горжусь тем, что называю Timetable своим домом. Итак, собираетесь ли вы остаться здесь, или же вас ждут неизвестные дали, добро пожаловать в Timetable. Здесь безопасно.</v-card-text>
        </v-card>
        
        <v-spacer></v-spacer>

        <v-card class="ma-2">
          <v-btn 
          variant="flat" 
          class="ma-2"
          @click="$router.push({ path: '/login' })"
          >
            Авторизация
          </v-btn>
          </v-card>
  
          <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        @click="$router.push({ path: '/group' })"
        >
          Расписание для группы
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2">
          <v-btn 
          variant="flat" 
          class="ma-2"
          @click="$router.push({ path: '/audience' })"
          >
            Расписание для аудитории
          </v-btn>
        </v-card>
        
        <v-spacer></v-spacer>

        <v-card class="ma-2">
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

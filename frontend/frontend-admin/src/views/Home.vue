<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Админ страница!</h2>
        
        <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/new-teacher'}"
        >
          Добавить преподавателя
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
//
</script>

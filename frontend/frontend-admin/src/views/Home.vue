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

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/new-group'}"
        >
          Добавить группу
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/new-audience'}"
        >
          Добавить аудиторию
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/new-subject'}"
        >
          Добавить предмет
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/link-teacher-subject'}"
        >
          Добавить предмет к преподавателю
        </v-btn>
        </v-card>

        <v-spacer></v-spacer>

        <v-card class="ma-2">
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/generate-table'}"
        >
          Сгенерировать расписание
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

<template>
  <v-app-bar flat>
    <v-btn
    @click="$router.back()"
    >
      &lt;-
    </v-btn>

    <v-btn
    @click="$router.push('/')">
      <v-app-bar-title>N* :: Timetable</v-app-bar-title>
    </v-btn>
  </v-app-bar>
</template>

<script setup>
  //
</script>

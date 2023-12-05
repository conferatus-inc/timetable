<template>
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-1">Ошибка</h2>

        <h3 class="ma-1">
          {{ errorMessage }}
        </h3>
        <v-spacer></v-spacer>

        <v-btn 
          variant="flat" 
          class="ma-1"
          :to="{path: '/'}"
          >
            Домой
          </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
const props = defineProps({
  errorMessage: {
    type: String,
    default: "undefined"
  }
})
</script>

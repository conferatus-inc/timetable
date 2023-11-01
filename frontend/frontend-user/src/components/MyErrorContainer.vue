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
          variant="outlined" 
          class="ma-1"
          @click="$router.push({ path : '/'})"
          >
            Домой
          </v-btn>

      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
// import { h } from 'vue';

// export default {
//     name: 'my-error-container',
//     props: {
//         errorMessage: {
//             type: Object,
//             default: "unknown"
//         },
//     },
//     components: { h }
// }
const props = defineProps({
  errorMessage: {
    type: String,
    default: "undefined"
  }
})
</script>
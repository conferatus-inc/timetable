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

<script>
import { h } from 'vue';

 // todo unused
export default {
    name: 'my-error-container',
    props: {
        errorMessage: {
            type: Object,
            default: "unknown"
        },
    },
    components: { h }
}
</script>
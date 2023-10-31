<template>
  <q-page padding class="flex flex-left">
    <q-item>{{ eee }}</q-item>

    <q-list dense bordered padding> 
      <q-item clickable v-ripple @click="gavna.push(`GAGAGAGSJGDHSKGG`)" v-for="a in gavna" :key="a">
        <q-item-section>
          {{ a }} 
        </q-item-section>
      </q-item>
    </q-list>

    <!-- <q-input
      filled
      v-model="eee"
      input-class="text-right"
    /> -->
  </q-page>
</template>

<script setup>
import { ref } from 'vue'
import { HTTP } from 'src/http-common'

const gavna = ref(['a', 'b'])
const eee = ref('ya ya ya')

const a = HTTP.get('api/examfple')
  .then(response => {
      eee.value = response.data
  })
  .catch(e => {
    eee.value = "error" + e
  })

request.on('timeout', () => {
  request.destroy();
});
</script>

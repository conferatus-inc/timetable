<template>
  <q-page padding class="flex flex-left">

    <q-list dense bordered padding> 
      <q-item clickable v-ripple @click="gavna.push(`GAGAGAGSJGDHSKGG`)" v-for="a in gavna" :key="a">
        <q-item-section>
          {{ a }}
        </q-item-section>
      </q-item>
    </q-list>
  </q-page>
</template>

<script setup>
import { ref } from 'vue'

const gavna = ref(['a', 'b'])
</script>

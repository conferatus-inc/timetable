<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-1">Расписание преподавателей</h2>

        <items-buttons-list
        :items=items
        >

        </items-buttons-list>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
  import ItemsButtonsList from '@/components/ItemsButtonsList.vue';
  import { http } from '@/http-common';
  import { ref } from 'vue'

  const items = ref([])

  http.get('/teacher')
  .then(response => {
      items.value = response.data
  })
  .catch(e => {
    items.value = "error" + e // tood dialogue
  })

  // const items = ref([{'id': 1, 'name': 'debil'}])
</script>

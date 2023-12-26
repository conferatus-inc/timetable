<template>
  <div v-if="state === 'loading'">
    <v-container>
      <v-row>
        <!-- <v-col>
          <v-progress-circular>Загрузка</v-progress-circular>
        </v-col> -->
      </v-row>
    </v-container>
  </div>
  <div v-else-if="state === 'loaded'">
    <MyTable
    :items="items"
    :title="'Расписание для преподавателя ' + name"></MyTable>
  </div>
  <div v-else>
    <MyErrorContainer :errorMessage="errorMessage">
    </MyErrorContainer>
  </div>
</template>

<script setup>
  import MyErrorContainer from '@/components/MyErrorContainer.vue'
  import MyTable from '@/components/MyTable.vue';
  import { http } from '@/http-common';
  import { ref } from 'vue'
  import router from '@/router';

  const state = ref('loading')
  const items = ref('')
  const errorMessage = ref('') // tmp

  const name = ref(router.currentRoute._value.params.id)

  // http.get('/table/by-teacher-id/' + route.params.id)
  http.get('/api/v1/admin/timetable/lessons/by_teacher?name=' + name.value)
  .then(response => {
      items.value = response.data
      state.value = 'loaded'

      console.log(items)
  })
  .catch(e => {
    errorMessage.value = e 
    state.value = 'error'
  })
</script>

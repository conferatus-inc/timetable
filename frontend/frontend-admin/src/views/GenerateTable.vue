<template>
  <div v-if="state === 'loading'">
    <v-container>
      <v-row>
        <v-col>
          <v-progress-linear
            indeterminate
          ></v-progress-linear>
          {{ serverState }}
        </v-col>
      </v-row>
    </v-container>
  </div>
  <div v-else-if="state === 'loaded'">
    <!-- <MyTable
    :items="items[0]"
    title='Предложенное расписание'></MyTable> -->
    <v-container>
      <v-row>
        <v-col>
          <h2>
            {{ items[0] }}
          </h2>
        </v-col>
      </v-row>
    </v-container>
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

  function getTable() {
    http.get('/api/v1/admin/timetable/generate/state?taskId=10050')
      .then(response => {
          serverState.value = response.data.percentage
          if (response.data.running == false) {
            http.get('/api/v1/admin/timetable/generate/result?taskId=10050')
              .then(response2 => {
                items.value = response2.data
              })

            http.get('/api/v1/admin/timetable/generate/choose?taskId=10050&taskId=0')

            state.value = 'loaded'
          } else {
            setTimeout(() => getTable(), 1000)
          }
      })
      .catch(e => {
        errorMessage.value = e 
        state.value = 'error'
      })
  }

  const state = ref('loading')
  const items = ref('')
  const errorMessage = ref('') 
  const serverState = ref(1)

  http.get('/api/v1/admin/timetable/generate?semesterId=1')
    .catch(e => {
      errorMessage.value = e 
      state.value = 'error'
    })

  getTable()

</script>

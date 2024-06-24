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
    <v-container>
      <v-row>
        <v-col>
          <h2>Generation done! Penalties: </h2>
          <div v-for="(item, index) in items[0].penalties.penalty_results" class="penalty-table">
            <h2>{{ item.name }}</h2>
            <v-table>
              <thead>
                <tr>
                  <th>Lesson Day</th>
                  <th>Lesson Cell Number</th>
                  <th>Message</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(lesson, idx) in item.result.problemLessons" :key="idx">
                  <td>{{ lesson.lesson.cell.time.day }}</td>
                  <td>{{ lesson.lesson.cell.time.cellNumber }}</td>
                  <td>{{ lesson.message }}</td>
                </tr>
              </tbody>
            </v-table>
          </div>


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
    http.get('/api/v1/admin/timetable/generate/state')
      .then(response => {
          serverState.value = response.data.percentage
          if (response.data.running == false) {
            http.get('/api/v1/admin/timetable/generate/result')
              .then(response2 => {
                items.value = response2.data
                state.value = 'loaded'
              })

            http.get('/api/v1/admin/timetable/generate/choose?chooseIndex=0')
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

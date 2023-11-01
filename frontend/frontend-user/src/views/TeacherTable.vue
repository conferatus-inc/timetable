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
    <v-container>
      <v-row>
        <v-col>
          <h2 class="ma-1">Расписание</h2>
          <!-- <div class="ma-1">
            {{ arr2D }}
          </div> -->

          <v-table class="mytable">
            <thead>
              <tr>
                <th>Понедельник</th>
                <th>Вторник</th>
                <th>Среда</th>
                <th>Четверг</th>
                <th>Пятница</th>
                <th>Суббота</th> 
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="column in arr2D"
              >
                <td class="mytable"
                  v-for="item in column"
                >

                  <v-card density="compact" width=150 variant='text'
                  >
                    <v-card-item density='compact' class='mytable'>
                      <v-card-title>{{ item.subject }}&nbsp</v-card-title>
                      <v-card-subtitle>{{ item.teacher}}&nbsp</v-card-subtitle>
                      <v-card-text>{{ item.audience }}&nbsp</v-card-text>
                    </v-card-item>
                  </v-card>


                </td>
              </tr>
            </tbody>
          </v-table>

          <!-- <div v-for="item in items" :key="item.id">
            <v-spacer></v-spacer>
            <v-btn 
            variant="outlined" 
            class="ma-1"
            @click="router.push({ 'path' : '/teacher/' + item.id})"
            >
              {{ item.name }}
            </v-btn>
          </div> -->

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
// todo все перенести в компонент
  import MyErrorContainer from '@/components/MyErrorContainer.vue'
  import { http } from '@/http-common';
  import { ref } from 'vue'
  import router from '@/router';

  const items = ref([])
  const state = ref('loading')

  const errorMessage = ref('') // tmp

  const arr2D = ref('')

  // http.get('/table/by-teacher-id/' + route.params.id)
  http.get('/table/by-teacher-id/' + router.currentRoute._value.params.id)
  .then(response => {
      items.value = response.data
      state.value = 'loaded'


      arr2D.value = Array(items.value.days).fill(null).map(() => Array(items.value.lessonsPerDay).fill(
        {subject : ' ', teacher : ' ', audience : ' '}
      ))

      items.value.cells.forEach(cell => arr2D.value[cell.day][cell.lesson] = 
        {subject : cell.subject.name, teacher : cell.teacher.name, audience : cell.audience.name}
        )
  })
  .catch(e => {
    errorMessage.value = e 
    state.value = 'error'
  })


</script>

<!-- <style>
.mytable tr td {
  white-space: pre-line;
  /* margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column; */
  /* border: 1px solid grey; */
  /* white-space: pre-line; */
}
</style> -->
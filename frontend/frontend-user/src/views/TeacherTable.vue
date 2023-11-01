<template>
  <div v-if="state === 'loading'">
    <v-container>
      <v-row>
        <v-col>
          <v-progress-linear>Загрузка</v-progress-linear>
        </v-col>
      </v-row>
    </v-container>
  </div>
  <div v-else-if="state === 'loaded'">
    <v-container>
      <v-row>
        <v-col>
          <h2 class="ma-1">Расписание</h2>
          <div class="ma-1">
            {{ items }}
          </div>

          <!-- <v-table>
            <thead>
              <tr>
                <th class="text-left">
                  Name
                </th>
                <th class="text-left">
                  Calories
                </th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="item in desserts"
                :key="item.name"
              >
                <td>{{ item.name }}</td>
                <td>{{ item.calories }}</td>
              </tr>
            </tbody>
          </v-table> -->

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
    <my-error-container :errorMessage="errorMessage">
    </my-error-container>
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

  // http.get('/table/by-teacher-id/' + route.params.id)
  http.get('/table/by-teacher-id/' + router.currentRoute._value.params.id)
  .then(response => {
      items.value = response.data
      state.value = 'loaded'
  })
  .catch(e => {
    errorMessage.value = e 
    state.value = 'error'
  })

  // const actualTable = ref([])
  // for (let days = 0; days < items.days; step++) {
    
  // }


</script>

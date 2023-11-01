<template>
  <div v-if="state === 'loading'">
    <v-container>
      <!-- <v-row>
        <v-col>
          <v-progress-linear>Загрузка</v-progress-linear>
        </v-col>
      </v-row> -->
    </v-container>
  </div>
  <div v-else-if="state === 'loaded'">
    <v-container>
      <v-row>
        <v-col>
          <h2 class="ma-1">Расписание преподавателей</h2>

          <!-- <div v-for="item in items" :key="item.id">
            <v-spacer></v-spacer>
            <v-btn 
            variant="outlined" 
            class="ma-1"
            @click="$router.push({ path: '/teacher/' + item.id })"
            >
              {{ item.name }}
            </v-btn>
          </div> -->
          <v-card class="ma-1" max-width="300" variant='flat'>
            <v-list lines="one">
              <v-list-item
                v-for="item in items"
                :key="item.id"
                :title="item.name"
                @click="$router.push({ path: '/teacher/' + item.id })"
              ></v-list-item>
            </v-list>
          </v-card>

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
  import ItemsButtonsList from '@/components/ItemsButtonsList.vue';
  import MyErrorContainer from '@/components/MyErrorContainer.vue'
  import { http } from '@/http-common';
  import { ref } from 'vue'

  const items = ref([])
  const state = ref('loading')
  const errorMessage = ref('') // tmp

  http.get('/teacher')
  .then(response => {
      items.value = response.data
      state.value = 'loaded'
  })
  .catch(e => {
    errorMessage.value = e 
    state.value = 'error'
  })

</script>

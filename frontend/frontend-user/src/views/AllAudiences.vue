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
    <ItemsList
      :items="items"
      :title="'Список аудиторий'"
      :pathPrefix="'/audience/'">
    </ItemsList>
  </div>
  <div v-else>
    <MyErrorContainer :errorMessage="errorMessage">
    </MyErrorContainer>
  </div>
</template>

<script setup>
// todo все перенести в компонент
  import MyErrorContainer from '@/components/MyErrorContainer.vue'
  import ItemsList from '@/components/ItemsList.vue';

  import { http } from '@/http-common';
  import { ref } from 'vue'

  const items = ref([])
  const state = ref('loading')
  const errorMessage = ref('')

  http.get('/audience')
  .then(response => {
      items.value = response.data
      state.value = 'loaded'
  })
  .catch(e => {
    errorMessage.value = e 
    state.value = 'error'
  })

</script>

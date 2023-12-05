<template>
  <div v-if="state === 'loading'">
    <v-container>
    </v-container>
  </div>
  <div v-else-if="state === 'loaded'">
    <ItemsList
      :items="items"
      :title="'Выбрать преподавателя для редактирования'"
      :pathPrefix="'/teachers/'">
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

  // http.get('/api/v1/audience')
  // .then(response => {
  //     items.value = response.data
  //     state.value = 'loaded'
  // })
  // .catch(e => {
  //   errorMessage.value = e 
  //   state.value = 'error'
  // })

</script>

<template>
  <div>
    <v-container>
      <v-row>
        <v-col>
          <h2 class='ma-2'>{{ title }}</h2>
          <v-card class='ma-2' max-width='300'>
            <v-list 
            lines='one'>
              <v-list-subheader>Выберите из списка</v-list-subheader>
              <v-list-item
                v-for="item in items"
                :key="item.id"
                :title="item.name"
                :active="item.id === selectedItem"
                @click="aclick(item.id)"
              ></v-list-item>
            </v-list>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { showAlert } from '@/store/globalAlert';
import { ref } from 'vue';

const selectedItem = ref(-1)
const props = defineProps({
  title: {
    type: String,
    default: "untitled"
  },
  items: {
    required: true
  },
})

const emit = defineEmits(['choose'])

function aclick(id) {
  selectedItem.value = id
  emit('choose', id)
}
</script>

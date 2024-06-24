<template>
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добавить аудиторию</h2>
        <div>
          <v-text-field
            label="Название"
            :rules="rules"
            hide-details="auto"
            v-model="name"
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>

        <v-text>
          Тип аудитории:
        </v-text>
        <v-radio-group v-model="audienceType">
          <v-radio
          v-for="(label, key) in audienceTypes"
          :key="key"
          :label="label"
          :value="key">
          </v-radio>
        </v-radio-group>
        <v-spacer></v-spacer>

        <v-text-field
          label="Вместительность (кол-во групп)"
          v-model="audienceGroupCapacity"
        ></v-text-field>
       <v-spacer></v-spacer>

        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/'}"
        @click="postNewAudience()"
        :disabled="name.length < 3"
        :rules="rules"
        >
          Создать
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { http } from '@/http-common';
import { ref } from 'vue'
import { showAlert } from '@/store/globalAlert.js'

const rules = [
  value => !!value || 'Это поле обязательно',
  value => (value.length >= 3) || 'Требуется как минимум 3 символа',
]

const audienceTypes = {
  "LECTURE": "Лекционная",
  "TERMINAL": "Терминальная",
  "PRACTICAL": "Семинарская",
  "LABORATORY": "Лаборантская"
}

const name = ref('')
const audienceGroupCapacity = ref(1)
const audienceType = ref('LECTURE')

function postNewAudience() {
  http.post("api/v1/admin/audience?name=" + name.value + "&audience_type=" + audienceType.value + "&audience_group_capacity=" + audienceGroupCapacity.value)
    .then(response => {
      if (response.status == 200) {
        showAlert("Аудитория добавлена!")
      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}
</script>

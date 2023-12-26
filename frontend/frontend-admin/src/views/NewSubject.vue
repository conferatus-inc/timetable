<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добавить предмет</h2>
        <div>
          <v-text-field
            label="Название"
            :rules="rules"
            hide-details="auto"
            v-model="subjectName"
            single-line
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>

        <div>
          <v-text-field
            label="Сколько раз в неделю"
            :rules="[(value => value > 0)]"
            hide-details="auto"
            v-model="times"
            type="number"
            single-line
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>

        <v-text>
          Тип предмета:
        </v-text>
        <v-radio-group v-model="subjectType">
          <v-radio
          v-for="(label, key) in subjectTypes"
          :key="key"
          :label="label"
          :value="key">
          </v-radio>
        </v-radio-group>
        <v-spacer></v-spacer>
        
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/'}"
        @click="postNewSubject()"
        :disabled="subjectName.length < 3"
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

const times = ref(1)
const subjectName = ref('')
const subjectType = ref('PRACTICAL')

const subjectTypes = {
  "LECTURE": "Лекция",
  "TERMINAL": "Практическое занятие (терминальная ауд.)",
  "PRACTICAL": "Практическое занятие (семинарская ауд.)",
  "LABORATORY": "Лабораторное занятие"
}

function postNewSubject() {
  http.post("api/v1/admin/semesterplans/subject?id=1&times=" + times.value + "&subject_name=" + subjectName.value + "&subject_type=" + subjectType.value)
    .then(response => {
      if (response.status == 200) {
        showAlert("Предмет добавлен!")
      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}

</script>

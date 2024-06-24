<template>
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добавить преподавателя</h2>
        <div>
          <v-text-field
            label="ФИО"
            :rules="rules"
            hide-details="auto"
            v-model="name"
          ></v-text-field>
        </div>
        
        <v-divider class="my-4"></v-divider>

        <h3 class="ma-2">Предпочтения по расписанию</h3>
        <div v-for="(wish, index) in teacherWishes" :key="index" class="my-2">
          <v-row>
            <v-col cols="4">
              <v-select
                v-model="wish.dayOfWeek"
                :items="daysOfWeek"
                label="День недели"
                hide-details="auto"
              ></v-select>
            </v-col>
            <v-col cols="4">
              <v-select
                v-model="wish.lessonNumber"
                :items="lessonNumbers"
                label="Номер пары"
                hide-details="auto"
              ></v-select>
            </v-col>
            <v-col cols="4">
              <v-row>
              <v-slider
                v-model="wish.priority"
                :min="-10"
                :max="10"
                :step="1"
                label="Приоритет"
                hide-details="auto"
              ></v-slider>
              <div class="ma-2">{{ wish.priority }}</div>
            </v-row>
            </v-col>
          </v-row>
        </div>
        <v-btn 
          variant="flat" 
          class="ma-2" @click="addWish">Добавить предпочтение</v-btn>

        <v-spacer></v-spacer>
        
        <v-btn 
          variant="flat" 
          class="ma-2"
          :to="{path: '/'}"
          @click="postNewTeacher"
          :disabled="name.length < 3"
        >
          Создать
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { http } from '@/http-common';
import { showAlert } from '@/store/globalAlert.js'

const rules = [
  value => !!value || 'Это поле обязательно',
  value => (value.length >= 3) || 'Требуется как минимум 3 символа',
]

const name = ref('')
const teacherWishes = ref([
  { dayOfWeek: '', lessonNumber: 0, priority: 0 }
])

const daysOfWeek = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY']
const lessonNumbers = [0, 1, 2, 3, 4, 5]

function addWish() {
  teacherWishes.value.push({ dayOfWeek: '', lessonNumber: 0, priority: 0 })
}

function postNewTeacher() {
  http.post("api/v1/admin/teacher?name=" + name.value)
    .then(response => {
      if (response.status === 200) {
        showAlert("Преподаватель добавлен!")
      } else {
        showAlert(response.statusText)
      }

      console.log(teacherWishes.value)
      for (const wish of teacherWishes.value) {
        console.log("wish is " + wish)
        if (wish.dayOfWeek == null) {
          continue;
        }
        http.post("api/v1/admin/teacher/wishes?name=" + name.value, wish)
        .then(response => {
          console.log("posted " + wish)
        }).catch(error => {
          showAlert("Произошла ошибка: " + error)
        })
      }
    }).catch(error => {
      showAlert("Произошла ошибка: " + error)
    })
}
</script>

<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-col>
      <h2 class="ma-2">Добавить преподавателя к предмету</h2>

      <v-row>
        <ItemsList
        title="Преподаватель"
        :items="teachers"
        @choose="selectTeacher">
        </ItemsList>

        <ItemsList
        title="Предмет"
        :items="subjects"
        @choose="selectSubject">
        </ItemsList>
      </v-row>

      <div>
        <v-text-field
          label="Сколько раз в неделю"
          :rules="[(value => value > 0)]"
          hide-details="auto"
          v-model="possibleTimes"
          type="number"
          single-line
        ></v-text-field>
      </div>
      <v-spacer></v-spacer>
      
      <v-btn 
      variant="flat" 
      class="ma-2"
      :to="{path: '/'}"
      @click="postNewLink()"
      :disabled="selectedTeacher < 1 || selectedSubject < 1 || possibleTimes < 1"
      >
        Создать
      </v-btn>
    </v-col>
  </v-container>
</template>

<script setup>
import { http } from '@/http-common';
import { ref } from 'vue'
import { showAlert } from '@/store/globalAlert.js'
import ItemsList from '@/components/ItemsList.vue';

const teachers = ref()
const selectedTeacher = ref(-1)
const subjects = ref()
const selectedSubject = ref(-1)

const possibleTimes = ref(1)

function selectTeacher(id) {
  selectedTeacher.value = id
}

function selectSubject(id) {
  selectedSubject.value = id
}


http.get("api/v1/admin/teacher/all")
  .then(response => {
    teachers.value = response.data
  }).catch(function (error) {
    showAlert("Произошла ошибка: " + error)
  })

http.get("api/v1/admin/semesterplans/subject/all?id=1")
  .then(response => {
    subjects.value = response.data
  }).catch(function (error) {
    showAlert("Произошла ошибка: " + error)
  })

function postNewLink() {
  http.post("api/v1/admin/semesterplans/subject/teacher?id=1&subject_id="+selectedSubject.value+"&teacher_id="+selectedTeacher.value+"&possibleTimes="+possibleTimes.value)
    .then(response => {
      if (response.status == 200) {
        showAlert("Предмет добавлен к преподавателю!")
      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}

</script>

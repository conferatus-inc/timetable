<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-col>
      <h2 class="ma-2">Добавить преподавателя к предмету</h2>

      <v-row>
        <ItemsList
        title="Преподаватель"
        :items="groups"
        @choose="selectGroup">
        </ItemsList>

        <ItemsList
        title="Предмет"
        :items="subjects"
        @choose="selectSubject">
        </ItemsList>
      </v-row>
      
      <v-btn 
      variant="flat" 
      class="ma-2"
      :to="{path: '/'}"
      @click="postNewLink()"
      :disabled="selectedGroup < 1 || selectedSubject < 1 || possibleTimes < 1"
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

const groups = ref()
const selectedGroup = ref(-1)
const subjects = ref()
const selectedSubject = ref(-1)

const possibleTimes = ref(1)

function selectGroup(id) {
  selectedGroup.value = id
}

function selectSubject(id) {
  selectedSubject.value = id
}


http.get("api/v1/admin/group/all")
  .then(response => {
    groups.value = response.data
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
  http.post("api/v1/admin/semesterplans/subject/group?id=1&subject_id="+selectedSubject.value+"&group_id="+selectedGroup.value)
    .then(response => {
      if (response.status == 200) {
        showAlert("Предмет добавлен к группе!")
      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}

</script>

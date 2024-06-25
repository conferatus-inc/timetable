<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-col>
      <h2 class="ma-2">Добавить пользователя к ВУЗу</h2>

      <!-- <v-row> -->
        <v-text-field
          label="Пользователь"
          v-model="selectedUser"
          type="number"
          single-line
        ></v-text-field>
        <v-spacer></v-spacer>

        <ItemsList
        title="Университет"
        :items="universities"
        @choose="selectUniversity">
        </ItemsList>
      <!-- </v-row> -->
      
      <v-btn 
      variant="flat" 
      class="ma-2"
      :to="{path: '/'}"
      @click="postNewLink()"
      :disabled="selectedUser < 1 || selectedUniversity < 1"
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

const users = ref()
const selectedUser = ref(-1)
const universities = ref()
const selectedUniversity = ref(-1)

const possibleTimes = ref(1)

function selectUsers(id) {
  selectedUser.value = id
}

function selectUniversity(id) {
  selectedUniversity.value = id
}


http.get("api/v1/admin/users/all")
  .then(response => {
    users.value = response.data
  }).catch(function (error) {
    showAlert("Произошла ошибка: " + error)
  })

http.get("api/v1/admin/universities")
  .then(response => {
    universities.value = response.data
  }).catch(function (error) {
    showAlert("Произошла ошибка: " + error)
  })

function postNewLink() {
  http.post("api/v1/admin/universities/link?universityId="+selectedUniversity.value+"&userId="+selectedUser.value)
    .then(response => { 
      if (response.status == 200) {
        showAlert("Группа добавлена к университету!")
      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}

</script>

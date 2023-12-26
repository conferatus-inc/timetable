<template>
  <!-- <HelloWorld /> -->
  <v-container>
    <v-row>
      <v-col>
        <h2 class="ma-2">Добавить группу</h2>
        <div>
          <v-text-field
            label="Название"
            :rules="rules"
            hide-details="auto"
            v-model="name"
          ></v-text-field>
        </div>
        <v-spacer></v-spacer>
        
        <v-btn 
        variant="flat" 
        class="ma-2"
        :to="{path: '/'}"
        @click="postNewGroup()"
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

const name = ref('')

function postNewGroup() {
  http.post("api/v1/admin/group?name=" + name.value)
    .then(response => {
      if (response.status == 200) {
        showAlert("Группа добавлена!")
        const groupId = response.data.id

        http.post("api/v1/admin/semesterplans/groups?id=1&group_id=" + groupId)
          .then(response2 => {
            if (response2.status == 200) {
              showAlert("Группа добавлена!")
            } else {
              showAlert(response2.statusText)
            }
          }).catch(function (error) {
            showAlert("Произошла ошибка: " + error)
          })

      } else {
        showAlert(response.statusText)
      }
    }).catch(function (error) {
      showAlert("Произошла ошибка: " + error)
    })
}

</script>

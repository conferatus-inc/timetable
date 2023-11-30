<template>
  <div>
    <v-container>
      <v-row>
        <v-col>
          <h2 class="ma-2">{{ title }}</h2>
          <v-card class="ma-2">
            <v-table>
              <thead>
                <tr>
                  <th>Понедельник</th>
                  <th>Вторник</th>
                  <th>Среда</th>
                  <th>Четверг</th>
                  <th>Пятница</th>
                  <th>Суббота</th> 
                </tr>
              </thead>
              <tbody>
                <tr 
                  v-for="column in arr2D"
                >
                  <td
                    v-for="item in column"
                  >

                    <v-card density="compact" width=150 variant='text'
                    >
                      <v-card-item density='compact'>
                        <v-card-title>{{ item.subject }}&nbsp</v-card-title>
                        <v-card-subtitle>
                          <router-link class="act" :to="'/teacher/' + item.teacher">{{ item.teacher }}</router-link>&nbsp
                        </v-card-subtitle>
                        <v-card-text>
                          <router-link class="act" :to="'/audience/' + item.audience">{{ item.audience }}</router-link>&nbsp
                        </v-card-text>
                      </v-card-item>
                    </v-card>
                  </td>
                </tr>
              </tbody>
            </v-table>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
  const props = defineProps({
    title: {
      type: String,
      default: "untitled"
    },
    pathPrefix: {
      type: String, // pls include / and all of these
      default: "not needed now"
      // required: true
    },
    items: {
      required: true
    }
  })

  import { ref } from 'vue'
  import router from '@/router';

  const arr2D = ref('')
  const items = props.items

  arr2D.value = Array(items.days).fill(null).map(() => Array(items.lessonsPerDay).fill(
    {subject : ' ', teacher : ' ', audience : ' ', group : ' '}
  ))

  items.cells.forEach(cell => arr2D.value[cell.day][cell.lesson] = 
    {subject : cell.subject.name, teacher : cell.teacher.name, audience : cell.audience.name, group : cell.group.name })
    
</script>

<style>
.act {
  border-radius: 4px;
  color: black;
  text-decoration: none;
}
</style>
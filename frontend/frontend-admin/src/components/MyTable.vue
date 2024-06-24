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
                          {{ item.teacher }}&nbsp
                        </v-card-subtitle>
                        <v-card-text>
                          {{ item.audience }}&nbsp
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

  const arr2D = ref('')
  const items = props.items

  arr2D.value = Array(6).fill(null).map(() => Array(6).fill(
    {subject : ' ', teacher : ' ', audience : ' ', group : ' '}
  ))

  items.timeListDTO.cells.forEach(cell => arr2D.value[cell.day_index][cell.time_index] = 
    {subject : cell.name, teacher : cell.teacher.name, audience : cell.audience.name, group : "no grp" })//cell.group.name })
    
</script>

<style>
.act {
  border-radius: 4px;
  color: black;
  text-decoration: none;
}
</style>
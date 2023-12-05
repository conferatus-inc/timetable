<template>
  <div>
    <v-container>
      <v-row>
        <v-col>
          <h2 class='ma-2'>{{ title }}</h2>
          <v-card class='ma-2' max-width='300'>
            <v-list lines='one'>
              <v-list-subheader>Выберите из списка</v-list-subheader>
              <v-list-item
                v-for="item in items"
                :key="item.id"
                :title="item.name"
                :to="{ path: pathPrefix + item.name }"
              ></v-list-item>
            </v-list>
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
    required: true
  },
  items: {
    required: true
  }
})

</script>

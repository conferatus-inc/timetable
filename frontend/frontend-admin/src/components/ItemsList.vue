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
                :click="$router.push({ path: pathPrefix + item.name })"
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
  items: {
    required: true
  },
  onClick: {
    required: true // "$router.push({ path: pathPrefix + item.name })"
  }
})

</script>

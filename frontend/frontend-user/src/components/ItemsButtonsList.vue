<template>
    <div v-for="item in items" :key="item.id">
      <v-spacer></v-spacer>
      <v-btn 
      variant="outlined" 
      class="ma-1"
      >
        {{ item.name }}
      </v-btn>
    </div>
</template>

<script> // todo unused
export default {
  name: 'items-buttons-list',
  props: {
    items: {
      type: Object,
      required: true,
    },
  },
  created() {
    this.$emit('onClick', this.customHeaderButtons);
  }
}
</script>
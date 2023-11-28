<template>
  <v-main>
    <v-fade-transition appear hide-on-leave="true">
      <router-view />
    </v-fade-transition>
  </v-main>

  <v-footer class="d-flex align-end">
    <v-row justify="center" no-gutters>
      <v-col class="text-center mt-4" cols="12">
        <p class="text-disabled font-weight-light">{{ new Date().getFullYear() }} Conferatus Inc. All rights reserved.</p>
      </v-col>
    </v-row>
  </v-footer>
</template>

<script setup>
  //
</script>

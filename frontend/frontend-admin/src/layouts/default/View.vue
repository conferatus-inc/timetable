<template>
  <v-main>
    <v-fade-transition appear hide-on-leave="true">
      <router-view />
    </v-fade-transition>
  </v-main>
  
  <v-footer class="d-flex align-end">
    <v-col class="text-center mt-4" cols="12">
      <v-row justify="center" no-gutters>
        <v-fade-transition appear>
          <v-alert
          v-model="alertEnabled"
          >{{ alertText }}</v-alert>
        </v-fade-transition>
      </v-row>

      <v-row justify="center" no-gutters>
        <p class="text-disabled font-weight-light">{{ new Date().getFullYear() }} Conferatus Inc. All rights reserved.</p>
      </v-row>
    </v-col>
  </v-footer>
</template>

<script setup>
import { alertEnabled, alertText } from '@/store/globalAlert';
</script>

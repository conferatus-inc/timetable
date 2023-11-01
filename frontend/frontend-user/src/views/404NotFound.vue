<template>
  <my-error-container :errorMessage="msg"></my-error-container>
</template>

<script setup>
// todo все перенести в компонент
  import MyErrorContainer from '@/components/MyErrorContainer.vue'
  // import { ref } from vue 

  const msg = '404 Not Found'
</script>

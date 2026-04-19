<template>
  <div>
    Today is {{formattedDate}}
  </div>
</template>

<script setup lang="ts">
const formattedDate = new Date().toISOString().split("T")[0];
</script>
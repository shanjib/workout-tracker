<template>
  <NavigationBar
      title="Workout"
  />
  <div>
    Today is {{ formattedDate }}
  </div>
</template>

<script setup lang="ts">
import NavigationBar from "../components/NavigationBar.vue";
const formattedDate = new Date().toISOString().split("T")[0];
</script>
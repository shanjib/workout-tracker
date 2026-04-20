<template>
  <Header/>
  <div>
    Today is {{ formattedDate }}
  </div>
  <div>
    <button type="submit" @click="submitLogout">Logout</button>
  </div>
</template>

<script setup lang="ts">
import Header from "../components/Header.vue";
import {useRouter} from "vue-router";
import {signOut} from 'aws-amplify/auth'

const router = useRouter();
const formattedDate = new Date().toISOString().split("T")[0];

async function submitLogout() {
  await signOut();
  await router.push("/");
}
</script>
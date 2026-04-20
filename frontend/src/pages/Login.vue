<template>
  <Header/>
  <div>
    <label>Email</label>
    <input
        v-model="email"
    />
  </div>
  <div>
    <label>Password</label>
    <input
        v-model="password"
    />
  </div>
  <div>
    <button type="submit" @click="submitLogin">Login</button>
  </div>
  <button @click="goToSignup">Signup</button>
  <div v-if="error">
    {{ error }}
  </div>
</template>

<script setup lang="ts">
import Header from "../components/Header.vue";
import {ref} from "vue";
import {signIn} from 'aws-amplify/auth'
import {useRouter} from "vue-router";

const router = useRouter();
const email = ref();
const password = ref();
const error = ref<String>();

function submitLogin() {
  let signInOutputPromise = signIn({
    username: email.value,
    password: password.value
  });
  signInOutputPromise
      .then(() => {
        router.push("/workouts");
      })
      .catch((e) => {
        error.value = e;
      })
}

function goToSignup() {
  router.push("/signup");
}
</script>

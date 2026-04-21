<template>
  <NavigationBar
      title="Sign Up!"
      :showNothing=true
      :showLoginButton=false
  />
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
    <button type="submit" @click="submitSignup">Submit</button>
  </div>
  <div v-if="error">
    {{ error }}
  </div>
</template>

<script setup lang="ts">
import NavigationBar from "../components/NavigationBar.vue";
import {ref} from "vue";
import {signUp} from 'aws-amplify/auth'
import {useRouter} from "vue-router";

const router = useRouter();
const email = ref();
const password = ref();
const error = ref<String>();

function submitSignup() {
  let signUpOutputPromise = signUp({
    username: email.value,
    password: password.value,
  });
  signUpOutputPromise
      .then(() => {
        router.push({ path: '/confirm', query: { email: email.value } });
      })
      .catch(e => {
        error.value = e;
      })
}
</script>

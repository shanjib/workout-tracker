<template>
  <NavigationBar
      title="Confirm Your Account"
      :showNothing=true
  />
  <div>
    <label>Confirmation Code</label>
    <input
        v-model="code"
    />
  </div>
  <div>
    <button type="submit" @click="submitConfirm">Confirm</button>
  </div>
  <div v-if="error">
    {{ error }}
  </div>
</template>

<script setup lang="ts">
import NavigationBar from "../components/NavigationBar.vue";
import {useRouter} from "vue-router";
import {ref} from "vue";
import {confirmSignUp} from 'aws-amplify/auth'
import { useRoute } from 'vue-router'

const route = useRoute()
const email = route.query.email as string
const router = useRouter();
const code = ref();
const error = ref<String>();

function submitConfirm() {
  let confirmSignUpOutputPromise = confirmSignUp({
    username: email,
    confirmationCode: code.value
  });
  confirmSignUpOutputPromise
      .then((result) => {
        if (result.isSignUpComplete) {
          router.push("/");
        } else {
          error.value = result.toString();
        }
      })
      .catch((e) => {
        error.value = e;
      })
}
</script>

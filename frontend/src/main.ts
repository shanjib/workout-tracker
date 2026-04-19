import { createApp } from 'vue'
import { Amplify } from 'aws-amplify'
import amplifyConfig from './amplifyconfiguration'
import './style.css'
import App from './App.vue'
import router from './router'

Amplify.configure(amplifyConfig)

createApp(App)
    .use(router)
    .mount('#app')

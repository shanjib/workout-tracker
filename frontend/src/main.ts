import { createApp } from 'vue'
import { Amplify } from 'aws-amplify'
import amplifyConfig from './amplifyconfiguration'
import './style.css'
import App from './App.vue'

Amplify.configure(amplifyConfig)

createApp(App).mount('#app')

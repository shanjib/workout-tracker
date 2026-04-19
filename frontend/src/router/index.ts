import { createRouter, createWebHistory } from 'vue-router'
import Welcome from '../pages/Welcome.vue'
import LoginView from '../pages/Login.vue'
import SignupView from '../pages/Signup.vue'
import ConfirmView from '../pages/Confirm.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Welcome },
        { path: '/login', component: LoginView },
        { path: '/signup', component: SignupView },
        { path: '/confirm', component: ConfirmView },
    ]
})

export default router
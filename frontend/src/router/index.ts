import { createRouter, createWebHistory } from 'vue-router'
import { fetchAuthSession } from 'aws-amplify/auth'
import Welcome from '../pages/Welcome.vue'
import LoginView from '../pages/Login.vue'
import SignupView from '../pages/Signup.vue'
import ConfirmView from '../pages/Confirm.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: LoginView },
        { path: '/login', component: LoginView },
        { path: '/signup', component: SignupView },
        { path: '/confirm', component: ConfirmView },
        { path: '/workouts', component: Welcome },
    ]
})

router.beforeEach(async (to) => {
    const publicRoutes = ['/', '/login', '/signup', '/confirm']
    const isPublicRoute = publicRoutes.includes(to.path)

    if (!isPublicRoute) {
        try {
            const session = await fetchAuthSession({ forceRefresh: false })
            const isAuthenticated = !!session.tokens?.accessToken
            if (!isAuthenticated) {
                return '/login'
            }
        } catch {
            return '/login'
        }
    }
})

export default router
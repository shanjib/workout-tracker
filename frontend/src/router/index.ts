import { createRouter, createWebHistory } from 'vue-router'
import { fetchAuthSession } from 'aws-amplify/auth'
import WorkoutHome from '../pages/WorkoutHome.vue'
import LoginView from '../pages/Login.vue'
import SignupView from '../pages/Signup.vue'
import ConfirmView from '../pages/Confirm.vue'
import Welcome from "../pages/Welcome.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Welcome },
        { path: '/login', component: LoginView },
        { path: '/signup', component: SignupView },
        { path: '/confirm', component: ConfirmView },
        { path: '/workouts', component: WorkoutHome },
        { path: '/profile', component: WorkoutHome },
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
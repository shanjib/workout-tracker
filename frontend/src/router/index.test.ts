import { describe, it, expect, vi, beforeEach } from 'vitest'
import * as amplifyAuth from 'aws-amplify/auth'

vi.mock('aws-amplify/auth')

describe('route guard', () => {

    beforeEach(() => {
        vi.resetAllMocks()
        vi.resetModules()
    })

    it('allows authenticated user to access protected route', async () => {
        vi.spyOn(amplifyAuth, 'fetchAuthSession').mockResolvedValue({
            tokens: {
                accessToken: { toString: () => 'fake-token' }
            }
        } as any)

        const { default: router } = await import('./index')
        await router.push('/workouts')
        await router.isReady()

        expect(router.currentRoute.value.path).toBe('/workouts')
    })

    it('redirects unauthenticated user to login', async () => {
        vi.spyOn(amplifyAuth, 'fetchAuthSession').mockResolvedValue({
            tokens: undefined
        } as any)

        const { default: router } = await import('./index')
        await router.push('/workouts')
        await router.isReady()

        expect(router.currentRoute.value.path).toBe('/login')
    })

    it('redirects to login when fetchAuthSession throws', async () => {
        vi.spyOn(amplifyAuth, 'fetchAuthSession').mockRejectedValue(
            new Error('No session')
        )

        const { default: router } = await import('./index')
        await router.push('/workouts')
        await router.isReady()

        expect(router.currentRoute.value.path).toBe('/login')
    })

})

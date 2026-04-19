import axios, { type AxiosInstance } from 'axios'
import { useAuthStore } from '@/stores/auth'

const api: AxiosInstance = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' }
})

api.interceptors.request.use((config) => {
    const auth = useAuthStore()
    if (auth.token) {
        config.headers.Authorization = `Bearer ${auth.token}`
    }
    return config
})

api.interceptors.response.use(
    (res) => res,
    async (error) => {
        const auth = useAuthStore()
        const original = error.config

        if (error.response?.status === 401 && !original._retry && auth.refreshToken) {
            original._retry = true
            try {
                await auth.refresh()
                original.headers.Authorization = `Bearer ${auth.token}`
                return api(original)
            } catch {
                auth.logout()
            }
        }
        return Promise.reject(error)
    }
)

export default api

import axios from 'axios'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { jwtDecode } from 'jwt-decode'
import type { LoginRequest, UsuarioSesion } from '@/types'
import { RolUsuario } from '@/types'

interface JwtPayload {
    sub: string
    nombre: string
    apellidos: string
    rol: RolUsuario
    exp: number
}

function usuarioDesdeToken(token: string): UsuarioSesion | null {
    try {
        const payload = jwtDecode<JwtPayload>(token)
        return {
            id: 0, // el JWT no suele incluir el id, se puede añadir si se necesita
            nombre: payload.nombre,
            apellidos: payload.apellidos,
            email: payload.sub,
            rol: payload.rol
        }
    } catch {
        return null
    }
}

export const useAuthStore = defineStore('auth', () => {
    const token = ref<string | null>(localStorage.getItem('token'))
    const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
    const usuario = ref<UsuarioSesion | null>(
        token.value ? usuarioDesdeToken(token.value) : null
    )

    const isAuthenticated = computed(() => !!token.value)
    const isAdmin = computed(() => usuario.value?.rol === RolUsuario.ADMIN)
    const nombreCompleto = computed(() => {
        if (!usuario.value) return ''
        return `${usuario.value.nombre} ${usuario.value.apellidos}`.trim()
    })

    async function login(req: LoginRequest): Promise<void> {
        const res = await axios.post<{ accessToken: string; refreshToken: string }>(
            '/api/auth/login',
            req
        )
        _guardarSesion(res.data.accessToken, res.data.refreshToken)
    }

    async function refresh(): Promise<void> {
        const res = await axios.post<{ accessToken: string; refreshToken: string }>(
            '/api/auth/refresh',
            { refreshToken: refreshToken.value }
        )
        _guardarSesion(res.data.accessToken, res.data.refreshToken)
    }

    async function logout(): Promise<void> {
        try {
            if (token.value) {
                await axios.post('/api/auth/logout', null, {
                    headers: { Authorization: `Bearer ${token.value}` }
                })
            }
        } finally {
            _limpiarSesion()
        }
    }

    function _guardarSesion(accessToken: string, newRefreshToken: string): void {
        token.value = accessToken
        refreshToken.value = newRefreshToken
        usuario.value = usuarioDesdeToken(accessToken)
        localStorage.setItem('token', accessToken)
        localStorage.setItem('refreshToken', newRefreshToken)
    }

    function _limpiarSesion(): void {
        token.value = null
        refreshToken.value = null
        usuario.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
    }

    return { token, refreshToken, usuario, isAuthenticated, isAdmin, nombreCompleto, login, refresh, logout }
})

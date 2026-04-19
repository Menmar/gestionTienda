import api from './ApiService'
import type { CambioPasswordRequest, PageResponse, ResetPasswordRequest, UsuarioRequest, UsuarioResponse } from '@/types'

const UsuarioService = {
    async listar(page = 0, size = 20): Promise<PageResponse<UsuarioResponse>> {
        const res = await api.get<PageResponse<UsuarioResponse>>('/usuarios', {
            params: { page, size }
        })
        return res.data
    },

    async obtener(id: number): Promise<UsuarioResponse> {
        const res = await api.get<UsuarioResponse>(`/usuarios/${id}`)
        return res.data
    },

    async crear(req: UsuarioRequest): Promise<UsuarioResponse> {
        const res = await api.post<UsuarioResponse>('/usuarios', req)
        return res.data
    },

    async actualizar(id: number, req: UsuarioRequest): Promise<UsuarioResponse> {
        const res = await api.put<UsuarioResponse>(`/usuarios/${id}`, req)
        return res.data
    },

    async eliminar(id: number): Promise<void> {
        await api.delete(`/usuarios/${id}`)
    },

    async resetPassword(id: number, req: ResetPasswordRequest): Promise<void> {
        await api.patch(`/usuarios/${id}/password`, req)
    },

    async cambiarPasswordPropia(req: CambioPasswordRequest): Promise<void> {
        await api.patch('/usuarios/me/password', req)
    }
}

export default UsuarioService

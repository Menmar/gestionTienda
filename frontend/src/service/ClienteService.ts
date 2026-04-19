import api from './ApiService'
import type { ClienteRequest, ClienteResponse, PageResponse } from '@/types'

const ClienteService = {
    async listar(nombre?: string, page = 0, size = 20): Promise<PageResponse<ClienteResponse>> {
        const res = await api.get<PageResponse<ClienteResponse>>('/clientes', {
            params: { nombre, page, size }
        })
        return res.data
    },

    async obtener(id: number): Promise<ClienteResponse> {
        const res = await api.get<ClienteResponse>(`/clientes/${id}`)
        return res.data
    },

    async buscarPorTelefono(telefono: string): Promise<ClienteResponse> {
        const res = await api.get<ClienteResponse>(`/clientes/telefono/${telefono}`)
        return res.data
    },

    async crear(req: ClienteRequest): Promise<ClienteResponse> {
        const res = await api.post<ClienteResponse>('/clientes', req)
        return res.data
    },

    async actualizar(id: number, req: ClienteRequest): Promise<ClienteResponse> {
        const res = await api.put<ClienteResponse>(`/clientes/${id}`, req)
        return res.data
    }
}

export default ClienteService

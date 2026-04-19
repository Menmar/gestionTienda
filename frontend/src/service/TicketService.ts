import api from './ApiService'
import type { CambioEstadoRequest, PageResponse, TicketRequest, TicketResponse } from '@/types'
import { type EstadoTicket, type TipoTicket } from '@/types'

export interface TicketFiltros {
    estado?: EstadoTicket
    tipo?: TipoTicket
    clienteId?: number
    page?: number
    size?: number
}

const TicketService = {
    async listar(filtros: TicketFiltros = {}): Promise<PageResponse<TicketResponse>> {
        const res = await api.get<PageResponse<TicketResponse>>('/tickets', { params: filtros })
        return res.data
    },

    async obtener(id: number): Promise<TicketResponse> {
        const res = await api.get<TicketResponse>(`/tickets/${id}`)
        return res.data
    },

    async crear(req: TicketRequest): Promise<TicketResponse> {
        const res = await api.post<TicketResponse>('/tickets', req)
        return res.data
    },

    async cambiarEstado(id: number, req: CambioEstadoRequest): Promise<TicketResponse> {
        const res = await api.patch<TicketResponse>(`/tickets/${id}/estado`, req)
        return res.data
    },

    async descargarPdf(id: number): Promise<Blob> {
        const res = await api.get(`/tickets/${id}/pdf`, { responseType: 'blob' })
        return res.data
    },

    abrirPdf(blob: Blob, numeroTicket: string): void {
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `ticket-${numeroTicket}.pdf`
        a.click()
        URL.revokeObjectURL(url)
    }
}

export default TicketService

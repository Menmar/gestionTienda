import api from './ApiService'
import type { PageResponse, ResumenEstadisticaResponse, TicketPendienteRecogidaResponse } from '@/types'

const EstadisticaService = {
    async resumen(
        desde: string,
        hasta: string,
        establecimientoId?: number
    ): Promise<ResumenEstadisticaResponse> {
        const res = await api.get<ResumenEstadisticaResponse>('/estadisticas/resumen', {
            params: { desde, hasta, establecimientoId }
        })
        return res.data
    },

    async pendientesRecogida(
        diasDesde?: number,
        establecimientoId?: number,
        page = 0,
        size = 20
    ): Promise<PageResponse<TicketPendienteRecogidaResponse>> {
        const res = await api.get<PageResponse<TicketPendienteRecogidaResponse>>(
            '/estadisticas/pendientes-recogida',
            { params: { diasDesde, establecimientoId, page, size } }
        )
        return res.data
    }
}

export default EstadisticaService

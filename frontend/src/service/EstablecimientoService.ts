import api from './ApiService'
import type {
    EstablecimientoRequest,
    EstablecimientoResponse,
    PrecioEstablecimientoRequest,
    PrecioEstablecimientoResponse,
    PreciosSugeridosResponse
} from '@/types'

const EstablecimientoService = {
    async listar(): Promise<EstablecimientoResponse[]> {
        const res = await api.get<EstablecimientoResponse[]>('/establecimientos')
        return res.data
    },

    async obtener(id: number): Promise<EstablecimientoResponse> {
        const res = await api.get<EstablecimientoResponse>(`/establecimientos/${id}`)
        return res.data
    },

    async crear(req: EstablecimientoRequest): Promise<EstablecimientoResponse> {
        const res = await api.post<EstablecimientoResponse>('/establecimientos', req)
        return res.data
    },

    async actualizar(id: number, req: EstablecimientoRequest): Promise<EstablecimientoResponse> {
        const res = await api.put<EstablecimientoResponse>(`/establecimientos/${id}`, req)
        return res.data
    },

    async eliminar(id: number): Promise<void> {
        await api.delete(`/establecimientos/${id}`)
    },

    async asignarEmpleado(id: number, usuarioId: number): Promise<void> {
        await api.post(`/establecimientos/${id}/empleados/${usuarioId}`)
    },

    async quitarEmpleado(id: number, usuarioId: number): Promise<void> {
        await api.delete(`/establecimientos/${id}/empleados/${usuarioId}`)
    },

    // Precios personalizados
    async getPreciosCalzado(id: number): Promise<PrecioEstablecimientoResponse[]> {
        const res = await api.get<PrecioEstablecimientoResponse[]>(`/establecimientos/${id}/precios/calzado`)
        return res.data
    },
    async upsertPrecioCalzado(id: number, req: PrecioEstablecimientoRequest): Promise<PrecioEstablecimientoResponse> {
        const res = await api.put<PrecioEstablecimientoResponse>(`/establecimientos/${id}/precios/calzado`, req)
        return res.data
    },
    async deletePrecioCalzado(id: number, tipoReparacionId: number): Promise<void> {
        await api.delete(`/establecimientos/${id}/precios/calzado/${tipoReparacionId}`)
    },

    async getPreciosCostura(id: number): Promise<PrecioEstablecimientoResponse[]> {
        const res = await api.get<PrecioEstablecimientoResponse[]>(`/establecimientos/${id}/precios/costura`)
        return res.data
    },
    async upsertPrecioCostura(id: number, req: PrecioEstablecimientoRequest): Promise<PrecioEstablecimientoResponse> {
        const res = await api.put<PrecioEstablecimientoResponse>(`/establecimientos/${id}/precios/costura`, req)
        return res.data
    },
    async deletePrecioCostura(id: number, tipoCosturaId: number): Promise<void> {
        await api.delete(`/establecimientos/${id}/precios/costura/${tipoCosturaId}`)
    },

    async getPreciosLlaves(id: number): Promise<PrecioEstablecimientoResponse[]> {
        const res = await api.get<PrecioEstablecimientoResponse[]>(`/establecimientos/${id}/precios/llaves`)
        return res.data
    },
    async upsertPrecioLlave(id: number, req: PrecioEstablecimientoRequest): Promise<PrecioEstablecimientoResponse> {
        const res = await api.put<PrecioEstablecimientoResponse>(`/establecimientos/${id}/precios/llaves`, req)
        return res.data
    },
    async deletePrecioLlave(id: number, tipoLlaveId: number): Promise<void> {
        await api.delete(`/establecimientos/${id}/precios/llaves/${tipoLlaveId}`)
    },

    // Precios sugeridos
    async getPreciosSugeridosCalzado(id: number): Promise<PreciosSugeridosResponse[]> {
        const res = await api.get<PreciosSugeridosResponse[]>(`/establecimientos/${id}/precios-sugeridos/calzado`)
        return res.data
    },
    async getPreciosSugeridosCostura(id: number): Promise<PreciosSugeridosResponse[]> {
        const res = await api.get<PreciosSugeridosResponse[]>(`/establecimientos/${id}/precios-sugeridos/costura`)
        return res.data
    },
    async getPreciosSugeridosLlaves(id: number): Promise<PreciosSugeridosResponse[]> {
        const res = await api.get<PreciosSugeridosResponse[]>(`/establecimientos/${id}/precios-sugeridos/llaves`)
        return res.data
    }
}

export default EstablecimientoService

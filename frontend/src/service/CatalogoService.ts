import api from './ApiService'
import type {
    FamiliaReparacionRequest,
    FamiliaReparacionResponse,
    TipoCosturaRequest,
    TipoCosturaResponse,
    TipoLlaveRequest,
    TipoLlaveResponse,
    TipoReparacionCalzadoRequest,
    TipoReparacionCalzadoResponse
} from '@/types'

const CatalogoService = {
    // Familias
    async listarFamilias(): Promise<FamiliaReparacionResponse[]> {
        const res = await api.get<FamiliaReparacionResponse[]>('/catalogo/familias')
        return res.data
    },
    async crearFamilia(req: FamiliaReparacionRequest): Promise<FamiliaReparacionResponse> {
        const res = await api.post<FamiliaReparacionResponse>('/catalogo/familias', req)
        return res.data
    },
    async actualizarFamilia(id: number, req: FamiliaReparacionRequest): Promise<FamiliaReparacionResponse> {
        const res = await api.put<FamiliaReparacionResponse>(`/catalogo/familias/${id}`, req)
        return res.data
    },
    async eliminarFamilia(id: number): Promise<void> {
        await api.delete(`/catalogo/familias/${id}`)
    },

    // Calzado
    async listarCalzado(familiaId?: number): Promise<TipoReparacionCalzadoResponse[]> {
        const res = await api.get<TipoReparacionCalzadoResponse[]>('/catalogo/reparaciones-calzado', {
            params: { familiaId }
        })
        return res.data
    },
    async crearCalzado(req: TipoReparacionCalzadoRequest): Promise<TipoReparacionCalzadoResponse> {
        const res = await api.post<TipoReparacionCalzadoResponse>('/catalogo/reparaciones-calzado', req)
        return res.data
    },
    async actualizarCalzado(id: number, req: TipoReparacionCalzadoRequest): Promise<TipoReparacionCalzadoResponse> {
        const res = await api.put<TipoReparacionCalzadoResponse>(`/catalogo/reparaciones-calzado/${id}`, req)
        return res.data
    },
    async eliminarCalzado(id: number): Promise<void> {
        await api.delete(`/catalogo/reparaciones-calzado/${id}`)
    },

    // Costura
    async listarCosturas(): Promise<TipoCosturaResponse[]> {
        const res = await api.get<TipoCosturaResponse[]>('/catalogo/costuras')
        return res.data
    },
    async crearCostura(req: TipoCosturaRequest): Promise<TipoCosturaResponse> {
        const res = await api.post<TipoCosturaResponse>('/catalogo/costuras', req)
        return res.data
    },
    async actualizarCostura(id: number, req: TipoCosturaRequest): Promise<TipoCosturaResponse> {
        const res = await api.put<TipoCosturaResponse>(`/catalogo/costuras/${id}`, req)
        return res.data
    },
    async eliminarCostura(id: number): Promise<void> {
        await api.delete(`/catalogo/costuras/${id}`)
    },

    // Llaves
    async listarLlaves(): Promise<TipoLlaveResponse[]> {
        const res = await api.get<TipoLlaveResponse[]>('/catalogo/llaves')
        return res.data
    },
    async crearLlave(req: TipoLlaveRequest): Promise<TipoLlaveResponse> {
        const res = await api.post<TipoLlaveResponse>('/catalogo/llaves', req)
        return res.data
    },
    async actualizarLlave(id: number, req: TipoLlaveRequest): Promise<TipoLlaveResponse> {
        const res = await api.put<TipoLlaveResponse>(`/catalogo/llaves/${id}`, req)
        return res.data
    },
    async eliminarLlave(id: number): Promise<void> {
        await api.delete(`/catalogo/llaves/${id}`)
    }
}

export default CatalogoService

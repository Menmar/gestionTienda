// ─── Enums ─────────────────────────────────────────────────────────────���──────

export enum TipoTicket {
    CALZADO = 'CALZADO',
    COSTURA = 'COSTURA',
    LLAVE = 'LLAVE'
}

export enum EstadoTicket {
    PENDIENTE = 'PENDIENTE',
    EN_PROCESO = 'EN_PROCESO',
    LISTO = 'LISTO',
    ENTREGADO = 'ENTREGADO',
    CANCELADO = 'CANCELADO'
}

export enum CanalNotificacion {
    EMAIL = 'EMAIL',
    WHATSAPP = 'WHATSAPP',
    TELEGRAM = 'TELEGRAM'
}

export enum RolUsuario {
    ADMIN = 'ADMIN',
    EMPLEADO = 'EMPLEADO'
}

// ─── Auth ─────────────────────────────────────────────────────────────────────

export interface LoginRequest {
    email: string
    password: string
}

export interface LoginResponse {
    accessToken: string
    refreshToken: string
}

export interface RefreshRequest {
    refreshToken: string
}

// Usuario en sesión (derivado del JWT o de /usuarios/me si existe)
export interface UsuarioSesion {
    id: number
    nombre: string
    apellidos: string
    email: string
    rol: RolUsuario
}

// ─── Paginación ───────────────────────────────────────────────────────────────

export interface PageResponse<T> {
    content: T[]
    page: number
    size: number
    totalElements: number
    totalPages: number
    last: boolean
}

// ─── Cliente ──────────────────────────────────────────────────────────────────

export interface ClienteRequest {
    nombre: string
    apellidos?: string
    telefono: string
    email?: string
    canalesNotificacion?: CanalNotificacion[]
    telegramChatId?: string
}

export interface ClienteResponse {
    id: number
    nombre: string
    apellidos?: string
    telefono: string
    email?: string
    canalesNotificacion: CanalNotificacion[]
    createdAt: string
}

// ─── Usuario ──────────────────────────────────────────────────────────────────

export interface UsuarioRequest {
    nombre: string
    apellidos: string
    email: string
    passwordInicial?: string
    rol: RolUsuario
}

export interface UsuarioResponse {
    id: number
    nombre: string
    apellidos: string
    email: string
    rol: RolUsuario
    activo: boolean
    createdAt: string
}

export interface CambioPasswordRequest {
    passwordActual: string
    passwordNueva: string
}

export interface ResetPasswordRequest {
    passwordNueva: string
}

// ─── Catálogo ─────────────────────────────────────────────────────────────────

export interface FamiliaReparacionRequest {
    nombre: string
}

export interface FamiliaReparacionResponse {
    id: number
    nombre: string
    activo: boolean
}

export interface TipoReparacionCalzadoRequest {
    nombre: string
    precioBase: number
    familiaId: number
}

export interface TipoReparacionCalzadoResponse {
    id: number
    nombre: string
    precioBase: number
    familiaId: number
    familiaNombre: string
    activo: boolean
}

export interface TipoCosturaRequest {
    nombre: string
    precioBase: number
}

export interface TipoCosturaResponse {
    id: number
    nombre: string
    precioBase: number
    activo: boolean
}

export interface TipoLlaveRequest {
    nombre: string
    precioBase: number
}

export interface TipoLlaveResponse {
    id: number
    nombre: string
    precioBase: number
    activo: boolean
}

// ─── Establecimiento ──────────────────────────────────────────────────────────

export interface EstablecimientoRequest {
    nombre: string
    direccion: string
    telefono?: string
}

export interface EstablecimientoResponse {
    id: number
    nombre: string
    direccion: string
    telefono?: string
    activo: boolean
}

export interface PrecioEstablecimientoRequest {
    itemId: number
    precio: number
}

export interface PrecioEstablecimientoResponse {
    id: number
    establecimientoId: number
    itemId: number
    itemNombre: string
    precioBase: number
    precioPersonalizado: number
}

export interface PreciosSugeridosResponse {
    itemId: number
    itemNombre: string
    precioBase: number
    precioEstablecimiento?: number
}

// ─── Ticket ───────────────────────────────────────────────────────────────────

export interface LineaResponse {
    id: number
    tipoNombre: string
    cantidad: number
    precioUnitario: number
    descuento: number
    subtotal: number
    descripcion?: string
}

export interface LineaCalzadoRequest {
    tipoReparacionId: number
    cantidad: number
    precioManual?: number
    descuento?: number
    descripcion?: string
}

export interface LineaCosturaRequest {
    tipoCosturaId: number
    cantidad: number
    precioManual?: number
    descuento?: number
    descripcion?: string
}

export interface LineaLlaveRequest {
    tipoLlaveId: number
    cantidad: number
    precioManual?: number
    descuento?: number
}

export interface FotoResponse {
    id: number
    ticketId: number
    nombreFichero: string
    createdAt: string
}

export interface TicketRequest {
    tipo: TipoTicket
    clienteId: number
    establecimientoId?: number
    fechaPrevista?: string
    observaciones?: string
    descuentoTotal?: number
    aplicarIva?: boolean
    porcentajeIva?: number
    lineasCalzado?: LineaCalzadoRequest[]
    lineasCostura?: LineaCosturaRequest[]
    lineasLlave?: LineaLlaveRequest[]
}

export interface TicketResponse {
    id: number
    numeroTicket: string
    tipo: TipoTicket
    clienteId: number
    clienteNombre: string
    clienteTelefono: string
    empleadoId?: number
    empleadoNombre?: string
    establecimientoId?: number
    establecimientoNombre?: string
    establecimientoDireccion?: string
    establecimientoTelefono?: string
    estado: EstadoTicket
    precioBase: number
    descuentoTotal: number
    aplicarIva: boolean
    porcentajeIva: number
    precioTotal: number
    fechaEntrada: string
    fechaPrevista?: string
    fechaEntrega?: string
    observaciones?: string
    createdAt: string
    updatedAt: string
    lineasCalzado: LineaResponse[]
    lineasCostura: LineaResponse[]
    lineasLlave: LineaResponse[]
    fotos: FotoResponse[]
}

export interface CambioEstadoRequest {
    estado: EstadoTicket
}

// ─── Estadísticas ─────────────────────────────────────────────────────────────

export interface ResumenEstadisticaResponse {
    totalAbiertos: number
    pendientes: number
    enProceso: number
    listos: number
    entregados: number
    cancelados: number
}

export interface TicketPendienteRecogidaResponse {
    id: number
    numeroTicket: string
    clienteNombre: string
    clienteTelefono: string
    establecimientoNombre?: string
    fechaEntrada: string
    fechaPrevista?: string
    diasEspera: number
}

// ─── Helpers de UI ────────────────────────────────────────────────────────────

export const ESTADO_TICKET_LABELS: Record<EstadoTicket, string> = {
    [EstadoTicket.PENDIENTE]: 'Pendiente',
    [EstadoTicket.EN_PROCESO]: 'En proceso',
    [EstadoTicket.LISTO]: 'Listo',
    [EstadoTicket.ENTREGADO]: 'Entregado',
    [EstadoTicket.CANCELADO]: 'Cancelado'
}

export const ESTADO_TICKET_SEVERITY: Record<EstadoTicket, string> = {
    [EstadoTicket.PENDIENTE]: 'warn',
    [EstadoTicket.EN_PROCESO]: 'info',
    [EstadoTicket.LISTO]: 'success',
    [EstadoTicket.ENTREGADO]: 'secondary',
    [EstadoTicket.CANCELADO]: 'danger'
}

export const TIPO_TICKET_LABELS: Record<TipoTicket, string> = {
    [TipoTicket.CALZADO]: 'Calzado',
    [TipoTicket.COSTURA]: 'Costura',
    [TipoTicket.LLAVE]: 'Llaves'
}

export const TRANSICIONES_VALIDAS: Record<EstadoTicket, EstadoTicket[]> = {
    [EstadoTicket.PENDIENTE]: [EstadoTicket.EN_PROCESO, EstadoTicket.CANCELADO],
    [EstadoTicket.EN_PROCESO]: [EstadoTicket.LISTO, EstadoTicket.CANCELADO],
    [EstadoTicket.LISTO]: [EstadoTicket.ENTREGADO, EstadoTicket.CANCELADO],
    [EstadoTicket.ENTREGADO]: [],
    [EstadoTicket.CANCELADO]: []
}

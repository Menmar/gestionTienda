<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'
import ClienteService from '@/service/ClienteService'
import EstablecimientoService from '@/service/EstablecimientoService'
import CatalogoService from '@/service/CatalogoService'
import TicketService from '@/service/TicketService'
import {
    CanalNotificacion, EstadoTicket,
    ESTADO_TICKET_LABELS, ESTADO_TICKET_SEVERITY,
    TIPO_TICKET_LABELS, TRANSICIONES_VALIDAS, TipoTicket
} from '@/types'
import type {
    ClienteResponse, EstablecimientoResponse,
    LineaCalzadoRequest, LineaCosturaRequest, LineaLlaveRequest,
    PreciosSugeridosResponse, TicketRequest, TicketResponse,
    TipoReparacionCalzadoResponse, TipoCosturaResponse, TipoLlaveResponse
} from '@/types'

const toast = useToast()
const confirm = useConfirm()

// ─── Lista ────────────────────────────────────────────────────────────────────
const tickets = ref<TicketResponse[]>([])
const totalRecords = ref(0)
const cargando = ref(false)
const filtroEstado = ref<EstadoTicket | null>(null)
const filtroTipo = ref<TipoTicket | null>(null)
const page = ref(0)
const rows = ref(15)

const estadoOptions = Object.values(EstadoTicket).map(e => ({ label: ESTADO_TICKET_LABELS[e], value: e }))
const tipoOptions = Object.values(TipoTicket).map(t => ({ label: TIPO_TICKET_LABELS[t], value: t }))

async function cargarTickets() {
    cargando.value = true
    try {
        const res = await TicketService.listar({
            estado: filtroEstado.value ?? undefined,
            tipo: filtroTipo.value ?? undefined,
            page: page.value,
            size: rows.value
        })
        tickets.value = res.content
        totalRecords.value = res.totalElements
    } finally {
        cargando.value = false
    }
}

function onPage(event: { page: number; rows: number }) {
    page.value = event.page
    rows.value = event.rows
    cargarTickets()
}

// ─── Crear ticket ─────────────────────────────────────────────────────────────
const dlgCrear = ref(false)
const establecimientos = ref<EstablecimientoResponse[]>([])
const clienteBusqueda = ref('')
const clienteEncontrado = ref<ClienteResponse | null>(null)
const clienteBuscando = ref(false)

// Catálogos
const calzadoItems = ref<TipoReparacionCalzadoResponse[]>([])
const costuraItems = ref<TipoCosturaResponse[]>([])
const llaveItems = ref<TipoLlaveResponse[]>([])
const preciosSugeridos = ref<PreciosSugeridosResponse[]>([])

const nuevoTicket = ref<TicketRequest>({
    tipo: TipoTicket.CALZADO,
    clienteId: 0,
    establecimientoId: undefined,
    aplicarIva: false,
    porcentajeIva: 21,
    descuentoTotal: 0,
    lineasCalzado: [],
    lineasCostura: [],
    lineasLlave: []
})

const lineasActuales = computed(() => {
    if (nuevoTicket.value.tipo === TipoTicket.CALZADO) return nuevoTicket.value.lineasCalzado!
    if (nuevoTicket.value.tipo === TipoTicket.COSTURA) return nuevoTicket.value.lineasCostura!
    return nuevoTicket.value.lineasLlave!
})

async function buscarCliente() {
    if (!clienteBusqueda.value.trim()) return
    clienteBuscando.value = true
    try {
        clienteEncontrado.value = await ClienteService.buscarPorTelefono(clienteBusqueda.value.trim())
        nuevoTicket.value.clienteId = clienteEncontrado.value.id
    } catch {
        toast.add({ severity: 'warn', summary: 'No encontrado', detail: 'No hay cliente con ese teléfono', life: 3000 })
        clienteEncontrado.value = null
    } finally {
        clienteBuscando.value = false
    }
}

async function onTipoChange() {
    nuevoTicket.value.lineasCalzado = []
    nuevoTicket.value.lineasCostura = []
    nuevoTicket.value.lineasLlave = []
    await cargarPreciosSugeridos()
}

async function onEstablecimientoChange() {
    await cargarPreciosSugeridos()
}

async function cargarPreciosSugeridos() {
    const estId = nuevoTicket.value.establecimientoId
    if (!estId) {
        preciosSugeridos.value = []
        return
    }
    try {
        if (nuevoTicket.value.tipo === TipoTicket.CALZADO) {
            preciosSugeridos.value = await EstablecimientoService.getPreciosSugeridosCalzado(estId)
        } else if (nuevoTicket.value.tipo === TipoTicket.COSTURA) {
            preciosSugeridos.value = await EstablecimientoService.getPreciosSugeridosCostura(estId)
        } else {
            preciosSugeridos.value = await EstablecimientoService.getPreciosSugeridosLlaves(estId)
        }
    } catch {
        preciosSugeridos.value = []
    }
}

function precioSugerido(itemId: number): number | undefined {
    const s = preciosSugeridos.value.find(p => p.itemId === itemId)
    return s?.precioEstablecimiento ?? s?.precioBase
}

function agregarLinea() {
    if (nuevoTicket.value.tipo === TipoTicket.CALZADO) {
        nuevoTicket.value.lineasCalzado!.push({ tipoReparacionId: 0, cantidad: 1 })
    } else if (nuevoTicket.value.tipo === TipoTicket.COSTURA) {
        nuevoTicket.value.lineasCostura!.push({ tipoCosturaId: 0, cantidad: 1 })
    } else {
        nuevoTicket.value.lineasLlave!.push({ tipoLlaveId: 0, cantidad: 1 })
    }
}

function quitarLinea(idx: number) {
    lineasActuales.value.splice(idx, 1)
}

function onItemChange(linea: LineaCalzadoRequest | LineaCosturaRequest | LineaLlaveRequest) {
    const itemId = ('tipoReparacionId' in linea) ? linea.tipoReparacionId
        : ('tipoCosturaId' in linea) ? linea.tipoCosturaId
        : (linea as LineaLlaveRequest).tipoLlaveId
    const sugerido = precioSugerido(itemId)
    if (sugerido !== undefined) linea.precioManual = sugerido
}

function abrirCrear() {
    nuevoTicket.value = {
        tipo: TipoTicket.CALZADO, clienteId: 0, aplicarIva: false,
        porcentajeIva: 21, descuentoTotal: 0,
        lineasCalzado: [], lineasCostura: [], lineasLlave: []
    }
    clienteBusqueda.value = ''
    clienteEncontrado.value = null
    preciosSugeridos.value = []
    dlgCrear.value = true
}

async function guardarTicket() {
    if (!nuevoTicket.value.clienteId) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Busca y selecciona un cliente', life: 3000 })
        return
    }
    try {
        await TicketService.crear(nuevoTicket.value)
        toast.add({ severity: 'success', summary: 'Creado', detail: 'Ticket creado correctamente', life: 3000 })
        dlgCrear.value = false
        cargarTickets()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error al crear ticket', life: 4000 })
    }
}

// ─── Cambiar estado ───────────────────────────────────────────────────────────
const dlgEstado = ref(false)
const ticketSeleccionado = ref<TicketResponse | null>(null)
const nuevoEstado = ref<EstadoTicket | null>(null)

const transicionesDisponibles = computed(() => {
    if (!ticketSeleccionado.value) return []
    return TRANSICIONES_VALIDAS[ticketSeleccionado.value.estado].map(e => ({
        label: ESTADO_TICKET_LABELS[e], value: e
    }))
})

function abrirCambioEstado(ticket: TicketResponse) {
    ticketSeleccionado.value = ticket
    nuevoEstado.value = null
    dlgEstado.value = true
}

async function confirmarCambioEstado() {
    if (!ticketSeleccionado.value || !nuevoEstado.value) return
    try {
        await TicketService.cambiarEstado(ticketSeleccionado.value.id, { estado: nuevoEstado.value })
        toast.add({ severity: 'success', summary: 'Actualizado', detail: 'Estado cambiado', life: 3000 })
        dlgEstado.value = false
        cargarTickets()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error al cambiar estado', life: 4000 })
    }
}

// ─── PDF ──────────────────────────────────────────────────────────────────────
async function descargarPdf(ticket: TicketResponse) {
    try {
        const blob = await TicketService.descargarPdf(ticket.id)
        TicketService.abrirPdf(blob, ticket.numeroTicket)
    } catch {
        toast.add({ severity: 'error', summary: 'Error', detail: 'No se pudo generar el PDF', life: 3000 })
    }
}

// ─── Init ─────────────────────────────────────────────────────────────────────
onMounted(async () => {
    const [ests, cal, cos, lla] = await Promise.all([
        EstablecimientoService.listar(),
        CatalogoService.listarCalzado(),
        CatalogoService.listarCosturas(),
        CatalogoService.listarLlaves()
    ])
    establecimientos.value = ests
    calzadoItems.value = cal
    costuraItems.value = cos
    llaveItems.value = lla
    cargarTickets()
})

// Helper para obtener el id del item en una línea
function getItemId(linea: LineaCalzadoRequest | LineaCosturaRequest | LineaLlaveRequest): number {
    if ('tipoReparacionId' in linea) return linea.tipoReparacionId
    if ('tipoCosturaId' in linea) return linea.tipoCosturaId
    return (linea as LineaLlaveRequest).tipoLlaveId
}

function setItemId(linea: LineaCalzadoRequest | LineaCosturaRequest | LineaLlaveRequest, val: number) {
    if ('tipoReparacionId' in linea) (linea as LineaCalzadoRequest).tipoReparacionId = val
    else if ('tipoCosturaId' in linea) (linea as LineaCosturaRequest).tipoCosturaId = val
    else (linea as LineaLlaveRequest).tipoLlaveId = val
    onItemChange(linea)
}

const itemsActuales = computed(() => {
    if (nuevoTicket.value.tipo === TipoTicket.CALZADO) return calzadoItems.value
    if (nuevoTicket.value.tipo === TipoTicket.COSTURA) return costuraItems.value
    return llaveItems.value
})
</script>

<template>
    <div>
        <div class="flex align-items-center justify-content-between mb-4">
            <h2 class="text-2xl font-bold m-0">Tickets</h2>
            <Button label="Nuevo ticket" icon="pi pi-plus" @click="abrirCrear" />
        </div>

        <!-- Filtros -->
        <div class="surface-card border-round p-3 mb-4 flex flex-wrap gap-3 align-items-center">
            <Select v-model="filtroEstado" :options="estadoOptions" option-label="label" option-value="value"
                placeholder="Todos los estados" show-clear style="min-width:180px" @change="cargarTickets" />
            <Select v-model="filtroTipo" :options="tipoOptions" option-label="label" option-value="value"
                placeholder="Todos los tipos" show-clear style="min-width:160px" @change="cargarTickets" />
            <Button icon="pi pi-refresh" text @click="cargarTickets" v-tooltip="'Recargar'" />
        </div>

        <!-- Tabla -->
        <div class="surface-card border-round shadow-1">
            <DataTable :value="tickets" :loading="cargando" lazy :total-records="totalRecords"
                :rows="rows" paginator @page="onPage" striped-rows>
                <Column field="numeroTicket" header="Nº Ticket" style="width:130px" />
                <Column field="tipo" header="Tipo">
                    <template #body="{ data }">{{ TIPO_TICKET_LABELS[data.tipo as TipoTicket] }}</template>
                </Column>
                <Column field="clienteNombre" header="Cliente" />
                <Column field="clienteTelefono" header="Teléfono" />
                <Column field="establecimientoNombre" header="Establecimiento" />
                <Column field="estado" header="Estado">
                    <template #body="{ data }">
                        <Tag :severity="ESTADO_TICKET_SEVERITY[data.estado as EstadoTicket]"
                            :value="ESTADO_TICKET_LABELS[data.estado as EstadoTicket]" />
                    </template>
                </Column>
                <Column field="fechaEntrada" header="Entrada" />
                <Column field="fechaPrevista" header="Previsto" />
                <Column field="precioTotal" header="Total">
                    <template #body="{ data }">{{ data.precioTotal?.toFixed(2) }} €</template>
                </Column>
                <Column header="Acciones" style="width:130px">
                    <template #body="{ data }">
                        <div class="flex gap-1">
                            <Button icon="pi pi-sync" size="small" text severity="info"
                                v-tooltip="'Cambiar estado'"
                                :disabled="TRANSICIONES_VALIDAS[data.estado as EstadoTicket].length === 0"
                                @click="abrirCambioEstado(data)" />
                            <Button icon="pi pi-file-pdf" size="small" text severity="danger"
                                v-tooltip="'Descargar PDF'" @click="descargarPdf(data)" />
                        </div>
                    </template>
                </Column>
                <template #empty>No hay tickets que mostrar.</template>
            </DataTable>
        </div>

        <!-- Dialog: Crear ticket -->
        <Dialog v-model:visible="dlgCrear" header="Nuevo ticket" :style="{ width: '700px' }" modal>
            <div class="flex flex-column gap-4">
                <!-- Cliente -->
                <div>
                    <label class="font-medium block mb-2">Buscar cliente por teléfono</label>
                    <div class="flex gap-2">
                        <InputText v-model="clienteBusqueda" placeholder="Teléfono" class="flex-1"
                            @keyup.enter="buscarCliente" />
                        <Button label="Buscar" icon="pi pi-search" :loading="clienteBuscando" @click="buscarCliente" />
                    </div>
                    <div v-if="clienteEncontrado" class="mt-2 p-2 surface-100 border-round text-sm">
                        <i class="pi pi-user mr-2 text-primary" />
                        <strong>{{ clienteEncontrado.nombre }} {{ clienteEncontrado.apellidos }}</strong>
                        — {{ clienteEncontrado.telefono }}
                    </div>
                </div>

                <!-- Tipo + Establecimiento -->
                <div class="grid grid-cols-2 gap-3">
                    <div>
                        <label class="font-medium block mb-2">Tipo</label>
                        <Select v-model="nuevoTicket.tipo" :options="tipoOptions" option-label="label"
                            option-value="value" class="w-full" @change="onTipoChange" />
                    </div>
                    <div>
                        <label class="font-medium block mb-2">Establecimiento</label>
                        <Select v-model="nuevoTicket.establecimientoId" :options="establecimientos"
                            option-label="nombre" option-value="id" class="w-full" show-clear
                            placeholder="Sin asignar" @change="onEstablecimientoChange" />
                    </div>
                </div>

                <!-- Líneas -->
                <div>
                    <div class="flex justify-content-between align-items-center mb-2">
                        <label class="font-medium">Líneas</label>
                        <Button label="Añadir línea" icon="pi pi-plus" size="small" text @click="agregarLinea" />
                    </div>
                    <div v-for="(linea, idx) in lineasActuales" :key="idx"
                        class="flex gap-2 align-items-center mb-2">
                        <Select :model-value="getItemId(linea)"
                            @update:model-value="(v: number) => setItemId(linea, v)"
                            :options="itemsActuales" option-label="nombre" option-value="id"
                            placeholder="Tipo" style="min-width:180px" />
                        <InputNumber v-model="linea.cantidad" :min="1" :max="99"
                            show-buttons style="width:90px" />
                        <InputNumber v-model="linea.precioManual" mode="currency" currency="EUR"
                            placeholder="Precio" style="width:110px" />
                        <InputNumber v-model="linea.descuento" suffix="%" :min="0" :max="100"
                            placeholder="Dto%" style="width:90px" />
                        <Button icon="pi pi-trash" text severity="danger" size="small"
                            @click="quitarLinea(idx)" />
                    </div>
                    <p v-if="lineasActuales.length === 0" class="text-color-secondary text-sm">
                        Sin líneas — el ticket se creará sin reparaciones detalladas.
                    </p>
                </div>

                <!-- Descuento + IVA + Fecha + Observaciones -->
                <div class="grid grid-cols-2 gap-3">
                    <div>
                        <label class="font-medium block mb-2">Descuento total (%)</label>
                        <InputNumber v-model="nuevoTicket.descuentoTotal" suffix="%" :min="0" :max="100" class="w-full" />
                    </div>
                    <div>
                        <label class="font-medium block mb-2">Fecha prevista</label>
                        <DatePicker v-model="nuevoTicket.fechaPrevista" date-format="dd/mm/yy" class="w-full" />
                    </div>
                </div>
                <div class="flex align-items-center gap-3">
                    <Checkbox v-model="nuevoTicket.aplicarIva" :binary="true" input-id="iva" />
                    <label for="iva">Aplicar IVA</label>
                    <InputNumber v-if="nuevoTicket.aplicarIva" v-model="nuevoTicket.porcentajeIva"
                        suffix="%" :min="0" :max="100" style="width:100px" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Observaciones</label>
                    <Textarea v-model="nuevoTicket.observaciones" rows="2" class="w-full" auto-resize />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" text @click="dlgCrear = false" />
                <Button label="Crear ticket" icon="pi pi-check" @click="guardarTicket" />
            </template>
        </Dialog>

        <!-- Dialog: Cambiar estado -->
        <Dialog v-model:visible="dlgEstado" header="Cambiar estado" :style="{ width: '380px' }" modal>
            <div v-if="ticketSeleccionado">
                <p class="mb-3">
                    Ticket <strong>{{ ticketSeleccionado.numeroTicket }}</strong> — estado actual:
                    <Tag :severity="ESTADO_TICKET_SEVERITY[ticketSeleccionado.estado]"
                        :value="ESTADO_TICKET_LABELS[ticketSeleccionado.estado]" />
                </p>
                <Select v-model="nuevoEstado" :options="transicionesDisponibles"
                    option-label="label" option-value="value" placeholder="Selecciona nuevo estado" class="w-full" />
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgEstado = false" />
                <Button label="Confirmar" icon="pi pi-check" :disabled="!nuevoEstado"
                    @click="confirmarCambioEstado" />
            </template>
        </Dialog>
    </div>
</template>

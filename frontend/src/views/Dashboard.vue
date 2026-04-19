<script setup lang="ts">
import { onMounted, ref } from 'vue'
import EstablecimientoService from '@/service/EstablecimientoService'
import EstadisticaService from '@/service/EstadisticaService'
import type { EstablecimientoResponse, ResumenEstadisticaResponse, TicketPendienteRecogidaResponse } from '@/types'

const establecimientos = ref<EstablecimientoResponse[]>([])
const establecimientoId = ref<number | null>(null)
const hoy = ref<ResumenEstadisticaResponse | null>(null)
const semana = ref<ResumenEstadisticaResponse | null>(null)
const mes = ref<ResumenEstadisticaResponse | null>(null)
const pendientes = ref<TicketPendienteRecogidaResponse[]>([])
const cargando = ref(true)

function fmt(d: Date): string {
    return d.toISOString().split('T')[0]
}

async function cargar() {
    cargando.value = true
    try {
        const ahora = new Date()
        const inicioSemana = new Date(ahora)
        inicioSemana.setDate(ahora.getDate() - ahora.getDay())
        const inicioMes = new Date(ahora.getFullYear(), ahora.getMonth(), 1)
        const estId = establecimientoId.value ?? undefined

        const [r1, r2, r3, r4] = await Promise.all([
            EstadisticaService.resumen(fmt(ahora), fmt(ahora), estId),
            EstadisticaService.resumen(fmt(inicioSemana), fmt(ahora), estId),
            EstadisticaService.resumen(fmt(inicioMes), fmt(ahora), estId),
            EstadisticaService.pendientesRecogida(undefined, estId, 0, 5)
        ])
        hoy.value = r1
        semana.value = r2
        mes.value = r3
        pendientes.value = r4.content
    } finally {
        cargando.value = false
    }
}

onMounted(async () => {
    establecimientos.value = await EstablecimientoService.listar()
    await cargar()
})
</script>

<template>
    <div>
        <div class="flex flex-wrap align-items-center justify-content-between gap-3 mb-5">
            <h2 class="text-2xl font-bold m-0">Dashboard</h2>
            <Select
                v-model="establecimientoId"
                :options="establecimientos"
                option-label="nombre"
                option-value="id"
                placeholder="Todos los establecimientos"
                show-clear
                style="min-width: 220px"
                @change="cargar"
            />
        </div>

        <div class="grid grid-cols-12 gap-4 mb-5">
            <div class="col-span-12 md:col-span-6 lg:col-span-3">
                <div class="surface-card border-round-xl p-4 shadow-1">
                    <div class="flex justify-content-between align-items-start mb-3">
                        <div>
                            <p class="text-color-secondary font-medium mb-1">Tickets hoy</p>
                            <span class="text-4xl font-bold">{{ hoy?.totalAbiertos ?? 0 }}</span>
                        </div>
                        <div class="flex align-items-center justify-content-center border-round-lg bg-blue-100" style="width:3rem;height:3rem">
                            <i class="pi pi-ticket text-blue-500 text-xl" />
                        </div>
                    </div>
                    <div class="flex gap-3 text-sm text-color-secondary">
                        <span><i class="pi pi-clock mr-1" />{{ hoy?.pendientes ?? 0 }} pendientes</span>
                        <span><i class="pi pi-wrench mr-1" />{{ hoy?.enProceso ?? 0 }} en proceso</span>
                    </div>
                </div>
            </div>
            <div class="col-span-12 md:col-span-6 lg:col-span-3">
                <div class="surface-card border-round-xl p-4 shadow-1">
                    <div class="flex justify-content-between align-items-start mb-3">
                        <div>
                            <p class="text-color-secondary font-medium mb-1">Esta semana</p>
                            <span class="text-4xl font-bold">{{ semana?.totalAbiertos ?? 0 }}</span>
                        </div>
                        <div class="flex align-items-center justify-content-center border-round-lg bg-orange-100" style="width:3rem;height:3rem">
                            <i class="pi pi-calendar-week text-orange-500 text-xl" />
                        </div>
                    </div>
                    <div class="flex gap-3 text-sm text-color-secondary">
                        <span><i class="pi pi-check-circle mr-1" />{{ semana?.listos ?? 0 }} listos</span>
                        <span><i class="pi pi-send mr-1" />{{ semana?.entregados ?? 0 }} entregados</span>
                    </div>
                </div>
            </div>
            <div class="col-span-12 md:col-span-6 lg:col-span-3">
                <div class="surface-card border-round-xl p-4 shadow-1">
                    <div class="flex justify-content-between align-items-start mb-3">
                        <div>
                            <p class="text-color-secondary font-medium mb-1">Este mes</p>
                            <span class="text-4xl font-bold">{{ mes?.totalAbiertos ?? 0 }}</span>
                        </div>
                        <div class="flex align-items-center justify-content-center border-round-lg bg-cyan-100" style="width:3rem;height:3rem">
                            <i class="pi pi-calendar text-cyan-500 text-xl" />
                        </div>
                    </div>
                    <div class="flex gap-3 text-sm text-color-secondary">
                        <span><i class="pi pi-times-circle mr-1" />{{ mes?.cancelados ?? 0 }} cancelados</span>
                    </div>
                </div>
            </div>
            <div class="col-span-12 md:col-span-6 lg:col-span-3">
                <div class="surface-card border-round-xl p-4 shadow-1">
                    <div class="flex justify-content-between align-items-start mb-3">
                        <div>
                            <p class="text-color-secondary font-medium mb-1">Pendientes de recoger</p>
                            <span class="text-4xl font-bold">{{ pendientes.length }}</span>
                        </div>
                        <div class="flex align-items-center justify-content-center border-round-lg bg-pink-100" style="width:3rem;height:3rem">
                            <i class="pi pi-exclamation-triangle text-pink-500 text-xl" />
                        </div>
                    </div>
                    <p class="text-sm text-color-secondary m-0">Más de 30 días sin recoger</p>
                </div>
            </div>
        </div>

        <div class="surface-card border-round-xl shadow-1 p-4">
            <h3 class="text-lg font-semibold mb-3">Tickets pendientes de recogida (≥30 días)</h3>
            <DataTable :value="pendientes" :loading="cargando" size="small" striped-rows>
                <Column field="numeroTicket" header="Nº Ticket" />
                <Column field="clienteNombre" header="Cliente" />
                <Column field="clienteTelefono" header="Teléfono" />
                <Column field="establecimientoNombre" header="Establecimiento" />
                <Column field="fechaEntrada" header="Entrada" />
                <Column field="diasEspera" header="Días espera">
                    <template #body="{ data }">
                        <Tag :severity="data.diasEspera > 60 ? 'danger' : 'warn'" :value="`${data.diasEspera} días`" />
                    </template>
                </Column>
                <template #empty>No hay tickets pendientes de recogida.</template>
            </DataTable>
        </div>
    </div>
</template>

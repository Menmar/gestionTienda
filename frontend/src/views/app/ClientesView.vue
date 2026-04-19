<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'
import ClienteService from '@/service/ClienteService'
import { CanalNotificacion } from '@/types'
import type { ClienteRequest, ClienteResponse } from '@/types'

const toast = useToast()

const clientes = ref<ClienteResponse[]>([])
const totalRecords = ref(0)
const cargando = ref(false)
const busqueda = ref('')
const page = ref(0)
const rows = ref(15)

const canalOptions = [
    { label: 'Email', value: CanalNotificacion.EMAIL },
    { label: 'WhatsApp', value: CanalNotificacion.WHATSAPP },
    { label: 'Telegram', value: CanalNotificacion.TELEGRAM }
]

async function cargar() {
    cargando.value = true
    try {
        const res = await ClienteService.listar(busqueda.value || undefined, page.value, rows.value)
        clientes.value = res.content
        totalRecords.value = res.totalElements
    } finally {
        cargando.value = false
    }
}

function onPage(event: { page: number; rows: number }) {
    page.value = event.page
    rows.value = event.rows
    cargar()
}

// ─── Dialog crear/editar ──────────────────────────────────────────────────────
const dlg = ref(false)
const editandoId = ref<number | null>(null)
const form = ref<ClienteRequest>({
    nombre: '', apellidos: '', telefono: '', email: '',
    canalesNotificacion: [], telegramChatId: ''
})

function abrirCrear() {
    editandoId.value = null
    form.value = { nombre: '', apellidos: '', telefono: '', email: '', canalesNotificacion: [], telegramChatId: '' }
    dlg.value = true
}

function abrirEditar(cliente: ClienteResponse) {
    editandoId.value = cliente.id
    form.value = {
        nombre: cliente.nombre,
        apellidos: cliente.apellidos ?? '',
        telefono: cliente.telefono,
        email: cliente.email ?? '',
        canalesNotificacion: [...cliente.canalesNotificacion],
        telegramChatId: ''
    }
    dlg.value = true
}

const requiereTelegram = ref(false)

function onCanalesChange() {
    requiereTelegram.value = form.value.canalesNotificacion?.includes(CanalNotificacion.TELEGRAM) ?? false
}

async function guardar() {
    try {
        if (editandoId.value) {
            await ClienteService.actualizar(editandoId.value, form.value)
            toast.add({ severity: 'success', summary: 'Actualizado', detail: 'Cliente actualizado', life: 3000 })
        } else {
            await ClienteService.crear(form.value)
            toast.add({ severity: 'success', summary: 'Creado', detail: 'Cliente creado', life: 3000 })
        }
        dlg.value = false
        cargar()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error al guardar', life: 4000 })
    }
}

onMounted(cargar)
</script>

<template>
    <div>
        <div class="flex align-items-center justify-content-between mb-4">
            <h2 class="text-2xl font-bold m-0">Clientes</h2>
            <Button label="Nuevo cliente" icon="pi pi-plus" @click="abrirCrear" />
        </div>

        <!-- Búsqueda -->
        <div class="surface-card border-round p-3 mb-4 flex gap-3 align-items-center">
            <IconField class="flex-1">
                <InputIcon class="pi pi-search" />
                <InputText v-model="busqueda" placeholder="Buscar por nombre o apellidos..."
                    class="w-full" @keyup.enter="cargar" />
            </IconField>
            <Button label="Buscar" icon="pi pi-search" @click="cargar" />
            <Button icon="pi pi-refresh" text @click="() => { busqueda = ''; cargar() }" v-tooltip="'Limpiar'" />
        </div>

        <div class="surface-card border-round shadow-1">
            <DataTable :value="clientes" :loading="cargando" lazy :total-records="totalRecords"
                :rows="rows" paginator @page="onPage" striped-rows>
                <Column field="nombre" header="Nombre" />
                <Column field="apellidos" header="Apellidos" />
                <Column field="telefono" header="Teléfono" />
                <Column field="email" header="Email" />
                <Column header="Notificaciones">
                    <template #body="{ data }">
                        <div class="flex gap-1 flex-wrap">
                            <Tag v-for="canal in data.canalesNotificacion" :key="canal"
                                :value="canal" severity="info" />
                        </div>
                    </template>
                </Column>
                <Column header="Acciones" style="width:100px">
                    <template #body="{ data }">
                        <Button icon="pi pi-pencil" size="small" text @click="abrirEditar(data)" />
                    </template>
                </Column>
                <template #empty>No hay clientes que mostrar.</template>
            </DataTable>
        </div>

        <!-- Dialog -->
        <Dialog v-model:visible="dlg" :header="editandoId ? 'Editar cliente' : 'Nuevo cliente'"
            :style="{ width: '480px' }" modal>
            <div class="flex flex-column gap-4">
                <div class="grid grid-cols-2 gap-3">
                    <div>
                        <label class="font-medium block mb-2">Nombre *</label>
                        <InputText v-model="form.nombre" class="w-full" />
                    </div>
                    <div>
                        <label class="font-medium block mb-2">Apellidos</label>
                        <InputText v-model="form.apellidos" class="w-full" />
                    </div>
                </div>
                <div>
                    <label class="font-medium block mb-2">Teléfono *</label>
                    <InputText v-model="form.telefono" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Email</label>
                    <InputText v-model="form.email" type="email" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Canales de notificación</label>
                    <div class="flex gap-3 flex-wrap">
                        <div v-for="opt in canalOptions" :key="opt.value" class="flex align-items-center gap-2">
                            <Checkbox :input-id="opt.value" v-model="form.canalesNotificacion"
                                :value="opt.value" @change="onCanalesChange" />
                            <label :for="opt.value">{{ opt.label }}</label>
                        </div>
                    </div>
                </div>
                <div v-if="requiereTelegram || form.canalesNotificacion?.includes(CanalNotificacion.TELEGRAM)">
                    <label class="font-medium block mb-2">Telegram Chat ID</label>
                    <InputText v-model="form.telegramChatId" class="w-full" placeholder="Ej: 123456789" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlg = false" />
                <Button :label="editandoId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardar" />
            </template>
        </Dialog>
    </div>
</template>

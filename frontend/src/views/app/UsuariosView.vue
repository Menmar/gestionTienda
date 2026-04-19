<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'
import UsuarioService from '@/service/UsuarioService'
import { RolUsuario } from '@/types'
import type { UsuarioRequest, UsuarioResponse } from '@/types'

const toast = useToast()
const confirm = useConfirm()

const usuarios = ref<UsuarioResponse[]>([])
const totalRecords = ref(0)
const cargando = ref(false)
const page = ref(0)
const rows = ref(15)

const rolOptions = [
    { label: 'Administrador', value: RolUsuario.ADMIN },
    { label: 'Empleado', value: RolUsuario.EMPLEADO }
]

async function cargar() {
    cargando.value = true
    try {
        const res = await UsuarioService.listar(page.value, rows.value)
        usuarios.value = res.content
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
const form = ref<UsuarioRequest>({
    nombre: '', apellidos: '', email: '', passwordInicial: '', rol: RolUsuario.EMPLEADO
})

function abrirCrear() {
    editandoId.value = null
    form.value = { nombre: '', apellidos: '', email: '', passwordInicial: '', rol: RolUsuario.EMPLEADO }
    dlg.value = true
}

function abrirEditar(usuario: UsuarioResponse) {
    editandoId.value = usuario.id
    form.value = { nombre: usuario.nombre, apellidos: usuario.apellidos, email: usuario.email, rol: usuario.rol }
    dlg.value = true
}

async function guardar() {
    try {
        if (editandoId.value) {
            await UsuarioService.actualizar(editandoId.value, form.value)
            toast.add({ severity: 'success', summary: 'Actualizado', detail: 'Usuario actualizado', life: 3000 })
        } else {
            await UsuarioService.crear(form.value)
            toast.add({ severity: 'success', summary: 'Creado', detail: 'Usuario creado', life: 3000 })
        }
        dlg.value = false
        cargar()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error al guardar', life: 4000 })
    }
}

// ─── Reset password ───────────────────────────────────────────────────────────
const dlgReset = ref(false)
const resetId = ref<number | null>(null)
const nuevaPassword = ref('')

function abrirReset(usuario: UsuarioResponse) {
    resetId.value = usuario.id
    nuevaPassword.value = ''
    dlgReset.value = true
}

async function confirmarReset() {
    if (!resetId.value || !nuevaPassword.value) return
    try {
        await UsuarioService.resetPassword(resetId.value, { passwordNueva: nuevaPassword.value })
        toast.add({ severity: 'success', summary: 'OK', detail: 'Contraseña restablecida', life: 3000 })
        dlgReset.value = false
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

// ─── Eliminar ─────────────────────────────────────────────────────────────────
function eliminar(usuario: UsuarioResponse) {
    confirm.require({
        message: `¿Eliminar al usuario ${usuario.nombre} ${usuario.apellidos}?`,
        header: 'Confirmar eliminación',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await UsuarioService.eliminar(usuario.id)
                toast.add({ severity: 'success', summary: 'Eliminado', detail: 'Usuario eliminado', life: 3000 })
                cargar()
            } catch (e: unknown) {
                const err = e as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

onMounted(cargar)
</script>

<template>
    <div>
        <div class="flex align-items-center justify-content-between mb-4">
            <h2 class="text-2xl font-bold m-0">Usuarios</h2>
            <Button label="Nuevo usuario" icon="pi pi-plus" @click="abrirCrear" />
        </div>

        <div class="surface-card border-round shadow-1">
            <DataTable :value="usuarios" :loading="cargando" lazy :total-records="totalRecords"
                :rows="rows" paginator @page="onPage" striped-rows>
                <Column field="nombre" header="Nombre" />
                <Column field="apellidos" header="Apellidos" />
                <Column field="email" header="Email" />
                <Column field="rol" header="Rol">
                    <template #body="{ data }">
                        <Tag :value="data.rol === RolUsuario.ADMIN ? 'Admin' : 'Empleado'"
                            :severity="data.rol === RolUsuario.ADMIN ? 'warn' : 'info'" />
                    </template>
                </Column>
                <Column field="activo" header="Activo">
                    <template #body="{ data }">
                        <i :class="data.activo ? 'pi pi-check-circle text-green-500' : 'pi pi-times-circle text-red-500'" />
                    </template>
                </Column>
                <Column header="Acciones" style="width:140px">
                    <template #body="{ data }">
                        <div class="flex gap-1">
                            <Button icon="pi pi-pencil" size="small" text @click="abrirEditar(data)" v-tooltip="'Editar'" />
                            <Button icon="pi pi-lock" size="small" text severity="warn"
                                @click="abrirReset(data)" v-tooltip="'Reset contraseña'" />
                            <Button icon="pi pi-trash" size="small" text severity="danger"
                                @click="eliminar(data)" v-tooltip="'Eliminar'" />
                        </div>
                    </template>
                </Column>
                <template #empty>No hay usuarios que mostrar.</template>
            </DataTable>
        </div>

        <!-- Dialog crear/editar -->
        <Dialog v-model:visible="dlg" :header="editandoId ? 'Editar usuario' : 'Nuevo usuario'"
            :style="{ width: '440px' }" modal>
            <div class="flex flex-column gap-4">
                <div class="grid grid-cols-2 gap-3">
                    <div>
                        <label class="font-medium block mb-2">Nombre *</label>
                        <InputText v-model="form.nombre" class="w-full" />
                    </div>
                    <div>
                        <label class="font-medium block mb-2">Apellidos *</label>
                        <InputText v-model="form.apellidos" class="w-full" />
                    </div>
                </div>
                <div>
                    <label class="font-medium block mb-2">Email *</label>
                    <InputText v-model="form.email" type="email" class="w-full" />
                </div>
                <div v-if="!editandoId">
                    <label class="font-medium block mb-2">Contraseña inicial *</label>
                    <Password v-model="form.passwordInicial" class="w-full" :feedback="false" toggle-mask input-class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Rol *</label>
                    <Select v-model="form.rol" :options="rolOptions" option-label="label" option-value="value" class="w-full" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlg = false" />
                <Button :label="editandoId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardar" />
            </template>
        </Dialog>

        <!-- Dialog reset password -->
        <Dialog v-model:visible="dlgReset" header="Restablecer contraseña" :style="{ width: '360px' }" modal>
            <div>
                <label class="font-medium block mb-2">Nueva contraseña *</label>
                <Password v-model="nuevaPassword" class="w-full" :feedback="true" toggle-mask input-class="w-full" />
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgReset = false" />
                <Button label="Restablecer" icon="pi pi-check" severity="warn"
                    :disabled="!nuevaPassword || nuevaPassword.length < 8"
                    @click="confirmarReset" />
            </template>
        </Dialog>

        <ConfirmDialog />
    </div>
</template>

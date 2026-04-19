<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'
import EstablecimientoService from '@/service/EstablecimientoService'
import UsuarioService from '@/service/UsuarioService'
import CatalogoService from '@/service/CatalogoService'
import type {
    EstablecimientoRequest, EstablecimientoResponse,
    PrecioEstablecimientoResponse, UsuarioResponse,
    TipoReparacionCalzadoResponse, TipoCosturaResponse, TipoLlaveResponse
} from '@/types'

const toast = useToast()
const confirm = useConfirm()

const establecimientos = ref<EstablecimientoResponse[]>([])
const cargando = ref(false)

async function cargar() {
    cargando.value = true
    try { establecimientos.value = await EstablecimientoService.listar() }
    finally { cargando.value = false }
}

// ─── CRUD establecimiento ──────────────────────────────────────────────────────
const dlg = ref(false)
const editandoId = ref<number | null>(null)
const form = ref<EstablecimientoRequest>({ nombre: '', direccion: '', telefono: '' })

function abrirCrear() {
    editandoId.value = null
    form.value = { nombre: '', direccion: '', telefono: '' }
    dlg.value = true
}

function abrirEditar(e: EstablecimientoResponse) {
    editandoId.value = e.id
    form.value = { nombre: e.nombre, direccion: e.direccion, telefono: e.telefono ?? '' }
    dlg.value = true
}

async function guardar() {
    try {
        if (editandoId.value) {
            await EstablecimientoService.actualizar(editandoId.value, form.value)
            toast.add({ severity: 'success', summary: 'OK', detail: 'Establecimiento actualizado', life: 3000 })
        } else {
            await EstablecimientoService.crear(form.value)
            toast.add({ severity: 'success', summary: 'OK', detail: 'Establecimiento creado', life: 3000 })
        }
        dlg.value = false
        cargar()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

function eliminar(e: EstablecimientoResponse) {
    confirm.require({
        message: `¿Eliminar "${e.nombre}"?`,
        header: 'Confirmar eliminación',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await EstablecimientoService.eliminar(e.id)
                toast.add({ severity: 'success', summary: 'OK', detail: 'Eliminado', life: 3000 })
                cargar()
                if (estSeleccionado.value?.id === e.id) estSeleccionado.value = null
            } catch (err: unknown) {
                const e2 = err as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: e2.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

// ─── Panel de detalle ─────────────────────────────────────────────────────────
const estSeleccionado = ref<EstablecimientoResponse | null>(null)
const tabActiva = ref(0)

const todosEmpleados = ref<UsuarioResponse[]>([])
const empleadosAsignados = ref<UsuarioResponse[]>([])
const empleadoAñadir = ref<number | null>(null)

const preciosCalzado = ref<PrecioEstablecimientoResponse[]>([])
const preciosCostura = ref<PrecioEstablecimientoResponse[]>([])
const preciosLlaves = ref<PrecioEstablecimientoResponse[]>([])

const calzadoItems = ref<TipoReparacionCalzadoResponse[]>([])
const costuraItems = ref<TipoCosturaResponse[]>([])
const llaveItems = ref<TipoLlaveResponse[]>([])

async function seleccionarEst(e: EstablecimientoResponse) {
    estSeleccionado.value = e
    tabActiva.value = 0
    await cargarDetalle(e.id)
}

async function cargarDetalle(id: number) {
    const [empleados, pCal, pCos, pLla] = await Promise.all([
        UsuarioService.listar(0, 100),
        EstablecimientoService.getPreciosCalzado(id),
        EstablecimientoService.getPreciosCostura(id),
        EstablecimientoService.getPreciosLlaves(id)
    ])
    todosEmpleados.value = empleados.content
    // Los empleados asignados se derivan de la lista completa — en este MVP
    // usamos todos los usuarios como candidatos y mostramos los precios personalizados
    preciosCalzado.value = pCal
    preciosCostura.value = pCos
    preciosLlaves.value = pLla
}

async function asignarEmpleado() {
    if (!estSeleccionado.value || !empleadoAñadir.value) return
    try {
        await EstablecimientoService.asignarEmpleado(estSeleccionado.value.id, empleadoAñadir.value)
        toast.add({ severity: 'success', summary: 'OK', detail: 'Empleado asignado', life: 3000 })
        empleadoAñadir.value = null
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

async function quitarEmpleado(usuarioId: number) {
    if (!estSeleccionado.value) return
    try {
        await EstablecimientoService.quitarEmpleado(estSeleccionado.value.id, usuarioId)
        toast.add({ severity: 'success', summary: 'OK', detail: 'Empleado quitado', life: 3000 })
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

// ─── Precios inline ───────────────────────────────────────────────────────────
async function upsertPrecio(tipo: 'calzado' | 'costura' | 'llaves', itemId: number, precio: number) {
    if (!estSeleccionado.value || !precio) return
    const id = estSeleccionado.value.id
    try {
        if (tipo === 'calzado') {
            await EstablecimientoService.upsertPrecioCalzado(id, { itemId, precio })
            preciosCalzado.value = await EstablecimientoService.getPreciosCalzado(id)
        } else if (tipo === 'costura') {
            await EstablecimientoService.upsertPrecioCostura(id, { itemId, precio })
            preciosCostura.value = await EstablecimientoService.getPreciosCostura(id)
        } else {
            await EstablecimientoService.upsertPrecioLlave(id, { itemId, precio })
            preciosLlaves.value = await EstablecimientoService.getPreciosLlaves(id)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Precio actualizado', life: 2000 })
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

async function deletePrecio(tipo: 'calzado' | 'costura' | 'llaves', itemId: number) {
    if (!estSeleccionado.value) return
    const id = estSeleccionado.value.id
    try {
        if (tipo === 'calzado') {
            await EstablecimientoService.deletePrecioCalzado(id, itemId)
            preciosCalzado.value = await EstablecimientoService.getPreciosCalzado(id)
        } else if (tipo === 'costura') {
            await EstablecimientoService.deletePrecioCostura(id, itemId)
            preciosCostura.value = await EstablecimientoService.getPreciosCostura(id)
        } else {
            await EstablecimientoService.deletePrecioLlave(id, itemId)
            preciosLlaves.value = await EstablecimientoService.getPreciosLlaves(id)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Precio eliminado', life: 2000 })
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

// Form nuevo precio
const nuevoPrecio = ref<{ itemId: number | null; precio: number | null }>({ itemId: null, precio: null })

async function añadirPrecio(tipo: 'calzado' | 'costura' | 'llaves') {
    if (!nuevoPrecio.value.itemId || !nuevoPrecio.value.precio) return
    await upsertPrecio(tipo, nuevoPrecio.value.itemId, nuevoPrecio.value.precio)
    nuevoPrecio.value = { itemId: null, precio: null }
}

onMounted(async () => {
    const [cal, cos, lla] = await Promise.all([
        CatalogoService.listarCalzado(),
        CatalogoService.listarCosturas(),
        CatalogoService.listarLlaves()
    ])
    calzadoItems.value = cal
    costuraItems.value = cos
    llaveItems.value = lla
    cargar()
})
</script>

<template>
    <div>
        <div class="flex align-items-center justify-content-between mb-4">
            <h2 class="text-2xl font-bold m-0">Establecimientos</h2>
            <Button label="Nuevo establecimiento" icon="pi pi-plus" @click="abrirCrear" />
        </div>

        <div class="grid grid-cols-12 gap-4">
            <!-- Lista -->
            <div class="col-span-12 md:col-span-4">
                <div class="surface-card border-round shadow-1">
                    <DataTable :value="establecimientos" :loading="cargando"
                        selection-mode="single" :selection="estSeleccionado"
                        @row-select="e => seleccionarEst(e.data)" striped-rows>
                        <Column field="nombre" header="Nombre">
                            <template #body="{ data }">
                                <div class="flex align-items-center justify-content-between">
                                    <span :class="{ 'font-bold text-primary': estSeleccionado?.id === data.id }">
                                        {{ data.nombre }}
                                    </span>
                                    <div class="flex gap-1">
                                        <Button icon="pi pi-pencil" size="small" text @click.stop="abrirEditar(data)" />
                                        <Button icon="pi pi-trash" size="small" text severity="danger" @click.stop="eliminar(data)" />
                                    </div>
                                </div>
                                <div class="text-sm text-color-secondary">{{ data.direccion }}</div>
                            </template>
                        </Column>
                        <template #empty>No hay establecimientos.</template>
                    </DataTable>
                </div>
            </div>

            <!-- Detalle -->
            <div class="col-span-12 md:col-span-8" v-if="estSeleccionado">
                <div class="surface-card border-round shadow-1 p-4">
                    <h3 class="text-lg font-semibold mb-3">{{ estSeleccionado.nombre }}</h3>
                    <Tabs v-model:value="tabActiva">
                        <TabList>
                            <Tab :value="0">Empleados</Tab>
                            <Tab :value="1">Precios calzado</Tab>
                            <Tab :value="2">Precios costura</Tab>
                            <Tab :value="3">Precios llaves</Tab>
                        </TabList>
                        <TabPanels>
                            <!-- Empleados -->
                            <TabPanel :value="0">
                                <div class="flex gap-2 mb-3">
                                    <Select v-model="empleadoAñadir" :options="todosEmpleados"
                                        :option-label="u => `${u.nombre} ${u.apellidos}`"
                                        option-value="id" placeholder="Selecciona empleado"
                                        class="flex-1" />
                                    <Button label="Asignar" icon="pi pi-user-plus" @click="asignarEmpleado" />
                                </div>
                                <p class="text-sm text-color-secondary">
                                    Usa el desplegable para asignar empleados. Para quitarlos, usa el botón de la lista de usuarios.
                                </p>
                                <div class="mt-3">
                                    <p class="font-medium mb-2">Todos los usuarios del sistema:</p>
                                    <div v-for="u in todosEmpleados" :key="u.id"
                                        class="flex align-items-center justify-content-between p-2 surface-100 border-round mb-1">
                                        <span>{{ u.nombre }} {{ u.apellidos }}
                                            <Tag class="ml-2" :value="u.rol" size="small" severity="secondary" />
                                        </span>
                                        <Button icon="pi pi-times" size="small" text severity="danger"
                                            v-tooltip="'Quitar de este establecimiento'"
                                            @click="quitarEmpleado(u.id)" />
                                    </div>
                                </div>
                            </TabPanel>

                            <!-- Precios calzado -->
                            <TabPanel :value="1">
                                <DataTable :value="preciosCalzado" size="small" class="mb-3">
                                    <Column field="itemNombre" header="Reparación" />
                                    <Column field="precioBase" header="Base">
                                        <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column field="precioPersonalizado" header="Personalizado">
                                        <template #body="{ data }">{{ data.precioPersonalizado?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column style="width:80px">
                                        <template #body="{ data }">
                                            <Button icon="pi pi-trash" size="small" text severity="danger"
                                                @click="deletePrecio('calzado', data.itemId)" />
                                        </template>
                                    </Column>
                                    <template #empty>Sin precios personalizados.</template>
                                </DataTable>
                                <div class="flex gap-2 align-items-center">
                                    <Select v-model="nuevoPrecio.itemId" :options="calzadoItems"
                                        option-label="nombre" option-value="id" placeholder="Reparación" style="flex:1" />
                                    <InputNumber v-model="nuevoPrecio.precio" mode="currency" currency="EUR"
                                        placeholder="Precio" style="width:130px" />
                                    <Button label="Añadir" icon="pi pi-plus" size="small" @click="añadirPrecio('calzado')" />
                                </div>
                            </TabPanel>

                            <!-- Precios costura -->
                            <TabPanel :value="2">
                                <DataTable :value="preciosCostura" size="small" class="mb-3">
                                    <Column field="itemNombre" header="Costura" />
                                    <Column field="precioBase" header="Base">
                                        <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column field="precioPersonalizado" header="Personalizado">
                                        <template #body="{ data }">{{ data.precioPersonalizado?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column style="width:80px">
                                        <template #body="{ data }">
                                            <Button icon="pi pi-trash" size="small" text severity="danger"
                                                @click="deletePrecio('costura', data.itemId)" />
                                        </template>
                                    </Column>
                                    <template #empty>Sin precios personalizados.</template>
                                </DataTable>
                                <div class="flex gap-2 align-items-center">
                                    <Select v-model="nuevoPrecio.itemId" :options="costuraItems"
                                        option-label="nombre" option-value="id" placeholder="Costura" style="flex:1" />
                                    <InputNumber v-model="nuevoPrecio.precio" mode="currency" currency="EUR"
                                        placeholder="Precio" style="width:130px" />
                                    <Button label="Añadir" icon="pi pi-plus" size="small" @click="añadirPrecio('costura')" />
                                </div>
                            </TabPanel>

                            <!-- Precios llaves -->
                            <TabPanel :value="3">
                                <DataTable :value="preciosLlaves" size="small" class="mb-3">
                                    <Column field="itemNombre" header="Tipo llave" />
                                    <Column field="precioBase" header="Base">
                                        <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column field="precioPersonalizado" header="Personalizado">
                                        <template #body="{ data }">{{ data.precioPersonalizado?.toFixed(2) }} €</template>
                                    </Column>
                                    <Column style="width:80px">
                                        <template #body="{ data }">
                                            <Button icon="pi pi-trash" size="small" text severity="danger"
                                                @click="deletePrecio('llaves', data.itemId)" />
                                        </template>
                                    </Column>
                                    <template #empty>Sin precios personalizados.</template>
                                </DataTable>
                                <div class="flex gap-2 align-items-center">
                                    <Select v-model="nuevoPrecio.itemId" :options="llaveItems"
                                        option-label="nombre" option-value="id" placeholder="Tipo llave" style="flex:1" />
                                    <InputNumber v-model="nuevoPrecio.precio" mode="currency" currency="EUR"
                                        placeholder="Precio" style="width:130px" />
                                    <Button label="Añadir" icon="pi pi-plus" size="small" @click="añadirPrecio('llaves')" />
                                </div>
                            </TabPanel>
                        </TabPanels>
                    </Tabs>
                </div>
            </div>
            <div class="col-span-12 md:col-span-8" v-else>
                <div class="surface-card border-round shadow-1 p-6 text-center text-color-secondary">
                    <i class="pi pi-building text-5xl mb-3 block" />
                    Selecciona un establecimiento para ver su detalle
                </div>
            </div>
        </div>

        <!-- Dialog CRUD -->
        <Dialog v-model:visible="dlg" :header="editandoId ? 'Editar establecimiento' : 'Nuevo establecimiento'"
            :style="{ width: '440px' }" modal>
            <div class="flex flex-column gap-4">
                <div>
                    <label class="font-medium block mb-2">Nombre *</label>
                    <InputText v-model="form.nombre" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Dirección *</label>
                    <InputText v-model="form.direccion" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Teléfono</label>
                    <InputText v-model="form.telefono" class="w-full" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlg = false" />
                <Button :label="editandoId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardar" />
            </template>
        </Dialog>

        <ConfirmDialog />
    </div>
</template>

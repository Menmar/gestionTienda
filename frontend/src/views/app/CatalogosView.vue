<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'
import CatalogoService from '@/service/CatalogoService'
import type {
    FamiliaReparacionResponse, TipoReparacionCalzadoResponse,
    TipoCosturaResponse, TipoLlaveResponse
} from '@/types'

const toast = useToast()
const confirm = useConfirm()
const tabActiva = ref(0)

// ─── Familias ─────────────────────────────────────────────────────────────────
const familias = ref<FamiliaReparacionResponse[]>([])
const dlgFamilia = ref(false)
const editFamiliaId = ref<number | null>(null)
const familiaForm = ref({ nombre: '' })

async function cargarFamilias() {
    familias.value = await CatalogoService.listarFamilias()
}

function abrirFamilia(f?: FamiliaReparacionResponse) {
    editFamiliaId.value = f?.id ?? null
    familiaForm.value = { nombre: f?.nombre ?? '' }
    dlgFamilia.value = true
}

async function guardarFamilia() {
    try {
        if (editFamiliaId.value) {
            await CatalogoService.actualizarFamilia(editFamiliaId.value, familiaForm.value)
        } else {
            await CatalogoService.crearFamilia(familiaForm.value)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Familia guardada', life: 2000 })
        dlgFamilia.value = false
        cargarFamilias()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

function eliminarFamilia(f: FamiliaReparacionResponse) {
    confirm.require({
        message: `¿Eliminar familia "${f.nombre}"?`,
        header: 'Confirmar',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await CatalogoService.eliminarFamilia(f.id)
                toast.add({ severity: 'success', summary: 'OK', detail: 'Eliminada', life: 2000 })
                cargarFamilias()
            } catch (err: unknown) {
                const e2 = err as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: e2.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

// ─── Calzado ──────────────────────────────────────────────────────────────────
const calzado = ref<TipoReparacionCalzadoResponse[]>([])
const dlgCalzado = ref(false)
const editCalzadoId = ref<number | null>(null)
const calzadoForm = ref({ nombre: '', precioBase: 0, familiaId: 0 })

async function cargarCalzado() {
    calzado.value = await CatalogoService.listarCalzado()
}

function abrirCalzado(item?: TipoReparacionCalzadoResponse) {
    editCalzadoId.value = item?.id ?? null
    calzadoForm.value = {
        nombre: item?.nombre ?? '',
        precioBase: item?.precioBase ?? 0,
        familiaId: item?.familiaId ?? (familias.value[0]?.id ?? 0)
    }
    dlgCalzado.value = true
}

async function guardarCalzado() {
    try {
        if (editCalzadoId.value) {
            await CatalogoService.actualizarCalzado(editCalzadoId.value, calzadoForm.value)
        } else {
            await CatalogoService.crearCalzado(calzadoForm.value)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Guardado', life: 2000 })
        dlgCalzado.value = false
        cargarCalzado()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

function eliminarCalzado(item: TipoReparacionCalzadoResponse) {
    confirm.require({
        message: `¿Eliminar "${item.nombre}"?`,
        header: 'Confirmar',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await CatalogoService.eliminarCalzado(item.id)
                toast.add({ severity: 'success', summary: 'OK', detail: 'Eliminado', life: 2000 })
                cargarCalzado()
            } catch (err: unknown) {
                const e2 = err as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: e2.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

// ─── Costura ──────────────────────────────────────────────────────────────────
const costuras = ref<TipoCosturaResponse[]>([])
const dlgCostura = ref(false)
const editCosturaId = ref<number | null>(null)
const costuraForm = ref({ nombre: '', precioBase: 0 })

async function cargarCosturas() {
    costuras.value = await CatalogoService.listarCosturas()
}

function abrirCostura(item?: TipoCosturaResponse) {
    editCosturaId.value = item?.id ?? null
    costuraForm.value = { nombre: item?.nombre ?? '', precioBase: item?.precioBase ?? 0 }
    dlgCostura.value = true
}

async function guardarCostura() {
    try {
        if (editCosturaId.value) {
            await CatalogoService.actualizarCostura(editCosturaId.value, costuraForm.value)
        } else {
            await CatalogoService.crearCostura(costuraForm.value)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Guardado', life: 2000 })
        dlgCostura.value = false
        cargarCosturas()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

function eliminarCostura(item: TipoCosturaResponse) {
    confirm.require({
        message: `¿Eliminar "${item.nombre}"?`,
        header: 'Confirmar',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await CatalogoService.eliminarCostura(item.id)
                toast.add({ severity: 'success', summary: 'OK', detail: 'Eliminado', life: 2000 })
                cargarCosturas()
            } catch (err: unknown) {
                const e2 = err as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: e2.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

// ─── Llaves ───────────────────────────────────────────────────────────────────
const llaves = ref<TipoLlaveResponse[]>([])
const dlgLlave = ref(false)
const editLlaveId = ref<number | null>(null)
const llaveForm = ref({ nombre: '', precioBase: 0 })

async function cargarLlaves() {
    llaves.value = await CatalogoService.listarLlaves()
}

function abrirLlave(item?: TipoLlaveResponse) {
    editLlaveId.value = item?.id ?? null
    llaveForm.value = { nombre: item?.nombre ?? '', precioBase: item?.precioBase ?? 0 }
    dlgLlave.value = true
}

async function guardarLlave() {
    try {
        if (editLlaveId.value) {
            await CatalogoService.actualizarLlave(editLlaveId.value, llaveForm.value)
        } else {
            await CatalogoService.crearLlave(llaveForm.value)
        }
        toast.add({ severity: 'success', summary: 'OK', detail: 'Guardado', life: 2000 })
        dlgLlave.value = false
        cargarLlaves()
    } catch (e: unknown) {
        const err = e as { response?: { data?: { detail?: string } } }
        toast.add({ severity: 'error', summary: 'Error', detail: err.response?.data?.detail ?? 'Error', life: 4000 })
    }
}

function eliminarLlave(item: TipoLlaveResponse) {
    confirm.require({
        message: `¿Eliminar "${item.nombre}"?`,
        header: 'Confirmar',
        icon: 'pi pi-trash',
        rejectClass: 'p-button-text',
        accept: async () => {
            try {
                await CatalogoService.eliminarLlave(item.id)
                toast.add({ severity: 'success', summary: 'OK', detail: 'Eliminado', life: 2000 })
                cargarLlaves()
            } catch (err: unknown) {
                const e2 = err as { response?: { data?: { detail?: string } } }
                toast.add({ severity: 'error', summary: 'Error', detail: e2.response?.data?.detail ?? 'Error', life: 4000 })
            }
        }
    })
}

onMounted(() => Promise.all([cargarFamilias(), cargarCalzado(), cargarCosturas(), cargarLlaves()]))
</script>

<template>
    <div>
        <h2 class="text-2xl font-bold mb-4">Catálogos</h2>

        <Tabs v-model:value="tabActiva">
            <TabList>
                <Tab :value="0">Familias</Tab>
                <Tab :value="1">Calzado</Tab>
                <Tab :value="2">Costura</Tab>
                <Tab :value="3">Llaves</Tab>
            </TabList>
            <TabPanels>

                <!-- FAMILIAS -->
                <TabPanel :value="0">
                    <div class="flex justify-content-end mb-3">
                        <Button label="Nueva familia" icon="pi pi-plus" @click="abrirFamilia()" />
                    </div>
                    <DataTable :value="familias" striped-rows>
                        <Column field="nombre" header="Nombre" />
                        <Column field="activo" header="Activo">
                            <template #body="{ data }">
                                <i :class="data.activo ? 'pi pi-check text-green-500' : 'pi pi-times text-red-500'" />
                            </template>
                        </Column>
                        <Column style="width:100px">
                            <template #body="{ data }">
                                <div class="flex gap-1">
                                    <Button icon="pi pi-pencil" size="small" text @click="abrirFamilia(data)" />
                                    <Button icon="pi pi-trash" size="small" text severity="danger" @click="eliminarFamilia(data)" />
                                </div>
                            </template>
                        </Column>
                        <template #empty>Sin familias.</template>
                    </DataTable>
                </TabPanel>

                <!-- CALZADO -->
                <TabPanel :value="1">
                    <div class="flex justify-content-end mb-3">
                        <Button label="Nuevo tipo calzado" icon="pi pi-plus" @click="abrirCalzado()" />
                    </div>
                    <DataTable :value="calzado" striped-rows>
                        <Column field="familiaNombre" header="Familia" />
                        <Column field="nombre" header="Nombre" />
                        <Column field="precioBase" header="Precio base">
                            <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                        </Column>
                        <Column field="activo" header="Activo">
                            <template #body="{ data }">
                                <i :class="data.activo ? 'pi pi-check text-green-500' : 'pi pi-times text-red-500'" />
                            </template>
                        </Column>
                        <Column style="width:100px">
                            <template #body="{ data }">
                                <div class="flex gap-1">
                                    <Button icon="pi pi-pencil" size="small" text @click="abrirCalzado(data)" />
                                    <Button icon="pi pi-trash" size="small" text severity="danger" @click="eliminarCalzado(data)" />
                                </div>
                            </template>
                        </Column>
                        <template #empty>Sin tipos de calzado.</template>
                    </DataTable>
                </TabPanel>

                <!-- COSTURA -->
                <TabPanel :value="2">
                    <div class="flex justify-content-end mb-3">
                        <Button label="Nuevo tipo costura" icon="pi pi-plus" @click="abrirCostura()" />
                    </div>
                    <DataTable :value="costuras" striped-rows>
                        <Column field="nombre" header="Nombre" />
                        <Column field="precioBase" header="Precio base">
                            <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                        </Column>
                        <Column field="activo" header="Activo">
                            <template #body="{ data }">
                                <i :class="data.activo ? 'pi pi-check text-green-500' : 'pi pi-times text-red-500'" />
                            </template>
                        </Column>
                        <Column style="width:100px">
                            <template #body="{ data }">
                                <div class="flex gap-1">
                                    <Button icon="pi pi-pencil" size="small" text @click="abrirCostura(data)" />
                                    <Button icon="pi pi-trash" size="small" text severity="danger" @click="eliminarCostura(data)" />
                                </div>
                            </template>
                        </Column>
                        <template #empty>Sin tipos de costura.</template>
                    </DataTable>
                </TabPanel>

                <!-- LLAVES -->
                <TabPanel :value="3">
                    <div class="flex justify-content-end mb-3">
                        <Button label="Nuevo tipo llave" icon="pi pi-plus" @click="abrirLlave()" />
                    </div>
                    <DataTable :value="llaves" striped-rows>
                        <Column field="nombre" header="Nombre" />
                        <Column field="precioBase" header="Precio base">
                            <template #body="{ data }">{{ data.precioBase?.toFixed(2) }} €</template>
                        </Column>
                        <Column field="activo" header="Activo">
                            <template #body="{ data }">
                                <i :class="data.activo ? 'pi pi-check text-green-500' : 'pi pi-times text-red-500'" />
                            </template>
                        </Column>
                        <Column style="width:100px">
                            <template #body="{ data }">
                                <div class="flex gap-1">
                                    <Button icon="pi pi-pencil" size="small" text @click="abrirLlave(data)" />
                                    <Button icon="pi pi-trash" size="small" text severity="danger" @click="eliminarLlave(data)" />
                                </div>
                            </template>
                        </Column>
                        <template #empty>Sin tipos de llave.</template>
                    </DataTable>
                </TabPanel>

            </TabPanels>
        </Tabs>

        <!-- Dialogs -->
        <Dialog v-model:visible="dlgFamilia" :header="editFamiliaId ? 'Editar familia' : 'Nueva familia'"
            :style="{ width: '360px' }" modal>
            <div>
                <label class="font-medium block mb-2">Nombre *</label>
                <InputText v-model="familiaForm.nombre" class="w-full" />
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgFamilia = false" />
                <Button :label="editFamiliaId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardarFamilia" />
            </template>
        </Dialog>

        <Dialog v-model:visible="dlgCalzado" :header="editCalzadoId ? 'Editar calzado' : 'Nuevo calzado'"
            :style="{ width: '400px' }" modal>
            <div class="flex flex-column gap-3">
                <div>
                    <label class="font-medium block mb-2">Familia *</label>
                    <Select v-model="calzadoForm.familiaId" :options="familias"
                        option-label="nombre" option-value="id" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Nombre *</label>
                    <InputText v-model="calzadoForm.nombre" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Precio base *</label>
                    <InputNumber v-model="calzadoForm.precioBase" mode="currency" currency="EUR" class="w-full" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgCalzado = false" />
                <Button :label="editCalzadoId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardarCalzado" />
            </template>
        </Dialog>

        <Dialog v-model:visible="dlgCostura" :header="editCosturaId ? 'Editar costura' : 'Nueva costura'"
            :style="{ width: '380px' }" modal>
            <div class="flex flex-column gap-3">
                <div>
                    <label class="font-medium block mb-2">Nombre *</label>
                    <InputText v-model="costuraForm.nombre" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Precio base *</label>
                    <InputNumber v-model="costuraForm.precioBase" mode="currency" currency="EUR" class="w-full" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgCostura = false" />
                <Button :label="editCosturaId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardarCostura" />
            </template>
        </Dialog>

        <Dialog v-model:visible="dlgLlave" :header="editLlaveId ? 'Editar llave' : 'Nueva llave'"
            :style="{ width: '380px' }" modal>
            <div class="flex flex-column gap-3">
                <div>
                    <label class="font-medium block mb-2">Nombre *</label>
                    <InputText v-model="llaveForm.nombre" class="w-full" />
                </div>
                <div>
                    <label class="font-medium block mb-2">Precio base *</label>
                    <InputNumber v-model="llaveForm.precioBase" mode="currency" currency="EUR" class="w-full" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" text @click="dlgLlave = false" />
                <Button :label="editLlaveId ? 'Guardar' : 'Crear'" icon="pi pi-check" @click="guardarLlave" />
            </template>
        </Dialog>

        <ConfirmDialog />
    </div>
</template>

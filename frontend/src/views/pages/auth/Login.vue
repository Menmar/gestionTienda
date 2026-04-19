<script setup lang="ts">
import FloatingConfigurator from '@/components/FloatingConfigurator.vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const toast = useToast()
const auth = useAuthStore()

const email = ref('')
const password = ref('')
const loading = ref(false)

async function iniciarSesion() {
    if (!email.value || !password.value) {
        toast.add({ severity: 'warn', summary: 'Campos requeridos', detail: 'Introduce email y contraseña', life: 3000 })
        return
    }
    loading.value = true
    try {
        await auth.login({ email: email.value, password: password.value })
        router.push('/')
    } catch {
        toast.add({ severity: 'error', summary: 'Acceso denegado', detail: 'Email o contraseña incorrectos', life: 4000 })
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <FloatingConfigurator />
    <div class="bg-surface-50 dark:bg-surface-950 flex items-center justify-center min-h-screen min-w-[100vw] overflow-hidden">
        <div class="flex flex-col items-center justify-center">
            <div style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                <div class="w-full bg-surface-0 dark:bg-surface-900 py-20 px-8 sm:px-20" style="border-radius: 53px">
                    <div class="text-center mb-8">
                        <div class="text-surface-900 dark:text-surface-0 text-3xl font-medium mb-4">Gestión Tienda</div>
                        <span class="text-muted-color font-medium">Inicia sesión para continuar</span>
                    </div>

                    <form @submit.prevent="iniciarSesion">
                        <label for="email" class="block text-surface-900 dark:text-surface-0 text-xl font-medium mb-2">Email</label>
                        <InputText id="email" v-model="email" type="email" placeholder="correo@ejemplo.com"
                            class="w-full md:w-[30rem] mb-8" autocomplete="email" />

                        <label for="password" class="block text-surface-900 dark:text-surface-0 font-medium text-xl mb-2">Contraseña</label>
                        <Password id="password" v-model="password" placeholder="Contraseña"
                            :toggle-mask="true" class="mb-8" fluid :feedback="false"
                            autocomplete="current-password" />

                        <Button type="submit" label="Entrar" class="w-full" :loading="loading" />
                    </form>
                </div>
            </div>
        </div>
    </div>
    <Toast />
</template>

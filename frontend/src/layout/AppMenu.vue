<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import AppMenuItem from './AppMenuItem.vue'

const auth = useAuthStore()

const model = computed(() => {
    const items: { label: string; items: { label: string; icon: string; to: string }[] }[] = [
        {
            label: 'Principal',
            items: [
                { label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/' },
                { label: 'Tickets', icon: 'pi pi-fw pi-ticket', to: '/tickets' },
                { label: 'Clientes', icon: 'pi pi-fw pi-users', to: '/clientes' }
            ]
        }
    ]

    if (auth.isAdmin) {
        items.push({
            label: 'Administración',
            items: [
                { label: 'Usuarios', icon: 'pi pi-fw pi-user-edit', to: '/usuarios' },
                { label: 'Establecimientos', icon: 'pi pi-fw pi-building', to: '/establecimientos' },
                { label: 'Catálogos', icon: 'pi pi-fw pi-list', to: '/catalogos' }
            ]
        })
    }

    return items
})
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item.label || i">
            <app-menu-item v-if="!item.separator" :item="item" :index="i" />
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

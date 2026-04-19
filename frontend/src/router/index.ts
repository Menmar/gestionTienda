import AppLayout from '@/layout/AppLayout.vue'
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            meta: { requiresAuth: true },
            children: [
                {
                    path: '/',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue')
                },
                {
                    path: '/tickets',
                    name: 'tickets',
                    component: () => import('@/views/app/TicketsView.vue')
                },
                {
                    path: '/clientes',
                    name: 'clientes',
                    component: () => import('@/views/app/ClientesView.vue')
                },
                {
                    path: '/usuarios',
                    name: 'usuarios',
                    component: () => import('@/views/app/UsuariosView.vue'),
                    meta: { requiresAdmin: true }
                },
                {
                    path: '/establecimientos',
                    name: 'establecimientos',
                    component: () => import('@/views/app/EstablecimientosView.vue'),
                    meta: { requiresAdmin: true }
                },
                {
                    path: '/catalogos',
                    name: 'catalogos',
                    component: () => import('@/views/app/CatalogosView.vue'),
                    meta: { requiresAdmin: true }
                }
            ]
        },
        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/pages/auth/Login.vue')
        },
        {
            path: '/auth/access',
            name: 'accessDenied',
            component: () => import('@/views/pages/auth/Access.vue')
        },
        {
            path: '/auth/error',
            name: 'error',
            component: () => import('@/views/pages/auth/Error.vue')
        },
        {
            path: '/pages/notfound',
            name: 'notfound',
            component: () => import('@/views/pages/NotFound.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/pages/notfound'
        }
    ]
})

router.beforeEach((to, _from, next) => {
    const auth = useAuthStore()
    if (to.meta.requiresAuth && !auth.isAuthenticated) return next({ name: 'login' })
    if (to.meta.requiresAdmin && !auth.isAdmin) return next({ name: 'accessDenied' })
    if (to.name === 'login' && auth.isAuthenticated) return next({ name: 'dashboard' })
    next()
})

export default router

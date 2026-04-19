import { defineStore } from 'pinia'
import { reactive, computed } from 'vue'

interface LayoutConfig {
    preset: string
    primary: string
    surface: string | null
    darkTheme: boolean
    menuMode: 'static' | 'overlay'
}

interface LayoutState {
    staticMenuInactive: boolean
    overlayMenuActive: boolean
    profileSidebarVisible: boolean
    configSidebarVisible: boolean
    sidebarExpanded: boolean
    menuHoverActive: boolean
    activeMenuItem: string | null
    activePath: string | null
    mobileMenuActive: boolean
}

export const useLayoutStore = defineStore('layout', () => {
    const layoutConfig = reactive<LayoutConfig>({
        preset: 'Aura',
        primary: 'emerald',
        surface: null,
        darkTheme: false,
        menuMode: 'static'
    })

    const layoutState = reactive<LayoutState>({
        staticMenuInactive: false,
        overlayMenuActive: false,
        profileSidebarVisible: false,
        configSidebarVisible: false,
        sidebarExpanded: false,
        menuHoverActive: false,
        activeMenuItem: null,
        activePath: null,
        mobileMenuActive: false
    })

    const isDarkTheme = computed(() => layoutConfig.darkTheme)
    const isDesktop = () => window.innerWidth > 991
    const hasOpenOverlay = computed(() => layoutState.overlayMenuActive)

    const toggleDarkMode = () => {
        if (!document.startViewTransition) {
            executeDarkModeToggle()
            return
        }
        document.startViewTransition(() => executeDarkModeToggle())
    }

    const executeDarkModeToggle = () => {
        layoutConfig.darkTheme = !layoutConfig.darkTheme
        document.documentElement.classList.toggle('app-dark')
    }

    const toggleMenu = () => {
        if (isDesktop()) {
            if (layoutConfig.menuMode === 'static') {
                layoutState.staticMenuInactive = !layoutState.staticMenuInactive
            }
            if (layoutConfig.menuMode === 'overlay') {
                layoutState.overlayMenuActive = !layoutState.overlayMenuActive
            }
        } else {
            layoutState.mobileMenuActive = !layoutState.mobileMenuActive
        }
    }

    const hideMobileMenu = () => {
        layoutState.mobileMenuActive = false
    }

    const changeMenuMode = (mode: 'static' | 'overlay') => {
        layoutConfig.menuMode = mode
        layoutState.staticMenuInactive = false
        layoutState.mobileMenuActive = false
        layoutState.sidebarExpanded = false
        layoutState.menuHoverActive = false
    }

    return {
        layoutConfig,
        layoutState,
        isDarkTheme,
        isDesktop,
        hasOpenOverlay,
        toggleDarkMode,
        toggleMenu,
        hideMobileMenu,
        changeMenuMode
    }
})

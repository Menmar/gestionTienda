import { useLayoutStore } from '@/stores/layout'

// Re-export the store's API with the same shape the Sakai components expect
export function useLayout() {
    const store = useLayoutStore()
    return {
        layoutConfig: store.layoutConfig,
        layoutState: store.layoutState,
        isDarkTheme: store.isDarkTheme,
        isDesktop: store.isDesktop,
        hasOpenOverlay: store.hasOpenOverlay,
        toggleDarkMode: store.toggleDarkMode,
        toggleMenu: store.toggleMenu,
        hideMobileMenu: store.hideMobileMenu,
        changeMenuMode: store.changeMenuMode,
        toggleConfigSidebar: () => {
            store.layoutState.configSidebarVisible = !store.layoutState.configSidebarVisible
        }
    }
}

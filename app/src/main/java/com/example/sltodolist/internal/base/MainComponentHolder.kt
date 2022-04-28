package com.example.sltodolist.internal.base

@Suppress("unused")
abstract class MainComponentHolder<out Component> : ComponentHolder<Component> {

    protected abstract fun provideInternal(): Component

    @Volatile
    private var pair: Pair<Component, Int>? = null

    override fun provideComponent(): Component {
        synchronized(this) {
            val localPair = pair
            val component: Component
            if (localPair == null) {
                component = provideInternal()
                logComponentAction("initialized", component)
                pair = Pair(component, 1)
            } else {
                component = localPair.first
                pair = Pair(component, localPair.second + 1)
            }
            return component
        }
    }

    fun onDependencyReleased() {
        synchronized(this) {
            val localPair = pair
            localPair?.let {
                if (localPair.second == 1) {
                    logComponentAction("released", localPair.first)
                    pair = null
                }
                pair = Pair(localPair.first, localPair.second - 1)
            }
        }
    }

    fun destroyComponent() {
        synchronized(this) {
            pair?.let { logComponentAction("destroyed", it.first) }
            pair = null
        }
    }
}
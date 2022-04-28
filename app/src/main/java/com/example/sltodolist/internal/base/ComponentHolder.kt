package com.example.sltodolist.internal.base

import com.example.sltodolist.extension.logd

interface ComponentHolder<out Component> {

    fun provideComponent(): Component

}

fun <T> ComponentHolder<T>.logComponentAction(actionName: String, component: T) {
    component ?: return
    logd(tag = "ComponentHolder") {
        val componentAsAny = component as Any
        val clazz = componentAsAny::class.java
        val componentInterface = clazz.interfaces.firstOrNull()
        val identityHashCode = System.identityHashCode(componentInterface)

        "Component $actionName: ${componentInterface?.simpleName}{@$identityHashCode}"
    }
}
package com.example.sltodolist.internal.app

import android.app.Application
import com.example.sltodolist.internal.base.MainComponentHolder

class AppComponentHolder(
    private val application: Application
) : MainComponentHolder<AppComponent>() {

    override fun provideInternal(): AppComponent {
        return DaggerAppComponent
            .builder()
            .roomModule(RoomModule(application))
            .build()
    }

}
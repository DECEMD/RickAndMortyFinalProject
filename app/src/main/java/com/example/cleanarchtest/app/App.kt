package com.example.cleanarchtest.app

import android.app.Application
import com.example.cleanarchtest.di.othertodelete.AppComponent
import com.example.cleanarchtest.di.othertodelete.AppModule
import com.example.cleanarchtest.di.othertodelete.DaggerAppComponent
import com.example.cleanarchtest.di.othertodelete.DataModule

//точка входа даггера в приложение + манифест
class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .dataModule(DataModule())
            .build()
    }
}
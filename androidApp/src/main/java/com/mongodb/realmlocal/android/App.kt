package com.mongodb.realmlocal.android

import android.app.Application
import com.mongodb.realmlocal.Repo
import kotlinx.coroutines.runBlocking

class App : Application() {

    private val realmRepo = Repo()

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            realmRepo.doAppSignIn()
        }
    }

}
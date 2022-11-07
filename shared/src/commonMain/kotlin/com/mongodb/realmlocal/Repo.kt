package com.mongodb.realmlocal

import CommonFlow
import asCommonFlow
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class Repo {

    private val schemeClass = setOf(User::class)

    private val appDataService by lazy {
        val appConfiguration =
            AppConfiguration.Builder("devicesyncapp-ndahz").log(LogLevel.ALL).build()
        App.create(appConfiguration)
    }

    private val realm by lazy {
        val user = appDataService.currentUser!!

        val configuration =
            SyncConfiguration.Builder(user = user, schema = schemeClass).name("local-db")
                .schemaVersion(1)
                .log(LogLevel.ALL)
                .initialSubscriptions { realm ->
                    add(realm.query<User>(), name = "userStream", updateExisting = true)
                }
                .build()

        Realm.open(configuration = configuration)
    }

    suspend fun doAppSignIn() {
        withContext(Dispatchers.Default) {
            appDataService.login(Credentials.anonymous())
        }
    }

    /* private val realm by lazy {
         val configuration = RealmConfiguration.Builder(schema = schemeClass).name("local-db")
             .schemaVersion(1)
             .log(LogLevel.ALL)
             *//*.encryptionKey()*//*
            .build()
        Realm.open(configuration = configuration)
    }*/

    suspend fun saveUserInfo(user: User) {
        realm.write {
            copyToRealm(user, updatePolicy = UpdatePolicy.ALL)
        }
    }

    fun getUsersAsFlow(): CommonFlow<List<User>> {
        return realm.query(clazz = User::class).asFlow().map {
            when (it) {
                is InitialResults -> it.list
                is UpdatedResults -> it.list
            }
        }.asCommonFlow()
    }

    suspend fun getUsersAsList(): List<User> {
        return withContext(Dispatchers.Default) {
            realm.query(clazz = User::class).find().map {
                it
            }
        }
    }

}
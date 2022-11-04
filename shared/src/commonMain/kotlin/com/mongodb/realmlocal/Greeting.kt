package com.mongodb.realmlocal

class Greeting {
    private val platform: Platform = getPlatform()

    private val repo = Repo()

    fun greeting(): String {
        return "Hello, ${platform.name}!"
    }
}
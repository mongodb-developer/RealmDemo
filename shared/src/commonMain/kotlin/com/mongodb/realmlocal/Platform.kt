package com.mongodb.realmlocal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
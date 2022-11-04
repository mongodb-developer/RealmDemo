package com.mongodb.realmlocal

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey


class User : RealmObject {

    @PrimaryKey
    var _id: RealmUUID = RealmUUID.random()
    var name: String = ""
    var twitterHandle: String? = null

}
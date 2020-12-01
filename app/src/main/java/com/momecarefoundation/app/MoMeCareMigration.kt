package com.momecarefoundation.app

import io.realm.DynamicRealm
import io.realm.RealmMigration

/**
 * Created by Monarchy on 03/11/2017.
 */
class MoMeCareMigration : RealmMigration {

    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        // Access the Realm schema in order to create, modify or delete classes and their fields.
        val schema = realm!!.schema
        if (oldVersion == 4L) {
        }
    }
}

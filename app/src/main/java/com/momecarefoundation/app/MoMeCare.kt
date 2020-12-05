package com.momecarefoundation.app

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.multidex.MultiDex
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.realm.Realm
import io.realm.RealmConfiguration

class MoMeCare : Application() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        var locationReceived: Location? = null
        private var tag = "MoMeCare"
    }


    override fun onCreate() {
        super.onCreate()


        MultiDex.install(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(1)
            .migration(MoMeCareMigration())
            .deleteRealmIfMigrationNeeded()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(realmConfiguration)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    locationReceived = location
                }
            return
        }


    }
}
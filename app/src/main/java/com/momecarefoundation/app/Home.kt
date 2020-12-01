package com.momecarefoundation.app


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.momecarefoundation.app.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {

    private fun requestLocationPermission() {
        //MARK: ask permission
        requestMultiplePermissions.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            // Do something if some permissions granted or denied
            permissions.entries.forEach {
                // Do checking here
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        //MARK: navigation listeners
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)

        //MARK: set the selected
        bottomNavigation.selectedItemId = R.id.pageSurveys
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                HomeFragment()
            ).commit()
        }


        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                // You can use the API that requires the permission.
            }
            else -> {
                requestLocationPermission()
            }
        }

    }

    //MARK: create a listener for the bottom navigation
    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = HomeFragment()
            when (item.itemId) {
                R.id.pageGroups -> {
                    selectedFragment = HomeFragment()
                }
                R.id.pageSurveys -> {
                    selectedFragment = HomeFragment()
                }
                R.id.pageResponds -> {
                    selectedFragment = HomeFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                selectedFragment!!
            ).commit()
            true
        }

}
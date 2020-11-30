package com.momecarefoundation.app


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.momecarefoundation.app.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {
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
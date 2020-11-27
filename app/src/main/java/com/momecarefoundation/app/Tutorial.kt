package com.momecarefoundation.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momecarefoundation.app.R
import com.momecarefoundation.app.extension.autoScroll
import kotlinx.android.synthetic.main.activity_tutorial.*

class Tutorial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)


        // Set the pager with an adapter
        tutorialPager!!.offscreenPageLimit = 4
        tutorialPager!!.adapter = TutorialPagerAdapter(supportFragmentManager)
        circleIndicator.setupWithViewPager(tutorialPager, true)

        tutorialPager.autoScroll(3000)
    }
}
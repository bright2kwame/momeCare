package com.momecarefoundation.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class TutorialPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return TutorialScreenFragment.newInstance(IMAGE_RES_IDS[position], TITLES_RES_IDS[position], MESSAGE_RES_IDS[position])
    }

    override fun getCount(): Int {
        return IMAGE_RES_IDS.size
    }

    companion object {

        // Images resources
        private val IMAGE_RES_IDS = intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher)

        // title resources
        private val TITLES_RES_IDS = intArrayOf(R.string.tutorial_one, R.string.tutorial_two)

        // message resources
        private val MESSAGE_RES_IDS = intArrayOf(R.string.tutorial_one_message, R.string.tutorial_two_message)
    }
}

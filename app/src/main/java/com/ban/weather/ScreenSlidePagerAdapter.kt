package com.ban.weather

import android.util.Log
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    private val TAG = javaClass.simpleName

    private lateinit var listFragment : List<WeatherFragment>

    override fun getItemCount() = listFragment.size

    override fun createFragment(position: Int): WeatherFragment = listFragment[position]

    fun updateData(data: List<WeatherFragment>) {
        Log.d(TAG, "[updateData] >> ${data.size}")
        listFragment = data
    }
}

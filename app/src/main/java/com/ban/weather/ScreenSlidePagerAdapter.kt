package com.ban.weather

import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    private lateinit var listFragment : List<WeatherFragment>

    override fun getItemCount() = listFragment.size

    override fun createFragment(position: Int): WeatherFragment = listFragment[position]

    fun updateData(data: List<WeatherFragment>) {
        listFragment = data
    }
}

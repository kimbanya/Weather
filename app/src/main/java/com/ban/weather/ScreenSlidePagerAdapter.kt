package com.ban.weather

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val TAG = javaClass.simpleName

//    private lateinit var listFragment : List<WeatherFragment>
    var listWeatherFragment : ArrayList<WeatherFragment> = ArrayList()

    override fun getItemCount() : Int {
        return listWeatherFragment.size
    }

    override fun createFragment(position: Int): WeatherFragment = listWeatherFragment[position]

    fun addFragment(fragment: WeatherFragment) {
        listWeatherFragment.add(fragment)
        notifyItemInserted(listWeatherFragment.size-1)
    }

    fun removeFragment() {
        listWeatherFragment.removeLast()
        notifyItemRemoved(listWeatherFragment.size)
    }

    fun updateData(data: List<WeatherFragment>) {
        Log.d(TAG, "[updateData] >> ${data.size}")
        listWeatherFragment = data as ArrayList<WeatherFragment>
    }
}

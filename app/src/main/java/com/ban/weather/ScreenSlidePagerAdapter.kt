package com.ban.weather

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val TAG = javaClass.simpleName

    private var listWeatherFragment : ArrayList<WeatherFragment> = ArrayList()
//    private lateinit var listWeatherFragment : List<WeatherFragment>

    override fun getItemCount() : Int = listWeatherFragment.size

    override fun createFragment(position: Int): WeatherFragment = listWeatherFragment[position]

    fun updateData(data: ArrayList<WeatherFragment>) {
        Log.d(TAG, "[updateData] >> ${data.size}")
        listWeatherFragment.addAll(data)
    }

    /*
    fun addFragment(fragment: WeatherFragment) {
        listWeatherFragment.add(fragment)
        notifyItemInserted(listWeatherFragment.size-1)
    }

    fun removeFragment() {
        listWeatherFragment.removeLast()
        notifyItemRemoved(listWeatherFragment.size)
    }
     */

}

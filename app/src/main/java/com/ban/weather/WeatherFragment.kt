package com.ban.weather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ban.weather.databinding.FragmentWeatherBinding
import com.ban.weather.models.ConsolidatedWeatherModel
import com.ban.weather.models.WeatherResponseModel
import com.bumptech.glide.Glide

private const val ARG_PARAM1 = "param1"

class WeatherFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : FragmentWeatherBinding

    private lateinit var recyclerViewAdapter: WeatherRecyclerViewAdapter

    private lateinit var param1: WeatherResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        // recycler view
        recyclerView()
        updateTodayView(param1)
        updateRecyclerView(param1?.consolidatedWeather!!)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: WeatherResponseModel) =
            WeatherFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTodayView(result: WeatherResponseModel?) {
        val todayWeather = result!!.consolidatedWeather[0]

        val title = binding.tvLocationTitle
        val theTemp = binding.tvTheTemp
        val weatherState = binding.tvWeatherState
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp
        val weatherIcon = binding.ivWeatherIcon
        val weatherAbbrString = todayWeather.weatherStateAbbr

        setImageResource(weatherIcon, weatherAbbrString)
        title.text = result!!.title
        theTemp.text = "Temperature : ${todayWeather.theTemp.toInt()}'"
        weatherState.text = "Weather: ${todayWeather.weatherStateName}"
        maxTemp.text = "High temp : ${todayWeather.maxTemp.toInt()}'"
        minTemp.text = "Low temp : ${todayWeather.minTemp.toInt()}'"
    }

    fun setImageResource(imageView: ImageView, category:String?) {
        var url = "https://www.metaweather.com/static/img/weather/png/%s.png"
        when (category) {
            "sn" -> { url = java.lang.String.format(url, "sn")}
            "sl" -> {url = java.lang.String.format(url, "sl")}
            "h" -> {url = java.lang.String.format(url, "h")}
            "t" -> {url = java.lang.String.format(url, "t")}
            "hr" -> {url = java.lang.String.format(url, "hr")}
            "lr" -> {url = java.lang.String.format(url, "lr")}
            "s" -> {url = java.lang.String.format(url, "s")}
            "hc" -> {url = java.lang.String.format(url, "hc")}
            "lc" -> {url = java.lang.String.format(url, "lc")}
            else -> {
                url = java.lang.String.format(url, "c")
            }
        }
        Glide.with(imageView.context).load(url).into(imageView)
    }

    private fun updateRecyclerView(dataList: List<ConsolidatedWeatherModel>) {
        val tempList: MutableList<ConsolidatedWeatherModel> = dataList as MutableList<ConsolidatedWeatherModel>
        tempList.removeAt(0)
        recyclerViewAdapter.updateList(tempList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        Log.d(TAG, "[recyclerView]")

        recyclerViewAdapter = WeatherRecyclerViewAdapter(this@WeatherFragment)
        binding.rvFutureWeatherInfo.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }
}
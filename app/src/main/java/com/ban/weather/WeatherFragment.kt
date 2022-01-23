package com.ban.weather

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ban.weather.adapters.WeatherRecyclerViewAdapter
import com.ban.weather.databinding.FragmentWeatherBinding
import com.ban.weather.models.ConsolidatedWeatherModel
import com.ban.weather.models.WeatherResponseModel

private const val mKEY = "WeatherFragment"

class WeatherFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : FragmentWeatherBinding

    private lateinit var recyclerViewAdapter: WeatherRecyclerViewAdapter

    private lateinit var weatherData: WeatherResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weatherData = it.getParcelable(mKEY)!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)

        recyclerView()
        updateTodayView(weatherData)
        updateRecyclerView(weatherData?.consolidatedWeather!!)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(weatherData: WeatherResponseModel) = WeatherFragment().apply {
            arguments = Bundle().apply {
                putParcelable(mKEY, weatherData)
            }
        }
    }

    private fun updateTodayView(result: WeatherResponseModel?) {
        val todayWeather = result!!.consolidatedWeather[0]
        val title = binding.tvLocationTitle
        val theTemp = binding.tvTheTemp
        val weatherState = binding.tvWeatherState
        val maxTemp = binding.tvMaxTemp
        val minTemp = binding.tvMinTemp
        val weatherIcon = binding.ivWeatherIcon
        val weatherAbbrString = todayWeather.weatherStateAbbr

        recyclerViewAdapter.setImageResource(weatherIcon, weatherAbbrString)

        title.text = result!!.title
        theTemp.text = "Now: ${todayWeather.theTemp.toInt()}°C"
        weatherState.text = "${todayWeather.weatherStateName}"
        maxTemp.text = "High: ${todayWeather.maxTemp.toInt()}°C"
        minTemp.text = "Low: ${todayWeather.minTemp.toInt()}°C"
    }

    private fun updateRecyclerView(dataList: List<ConsolidatedWeatherModel>) {
        val tempList: MutableList<ConsolidatedWeatherModel> = dataList as MutableList<ConsolidatedWeatherModel>
        tempList.removeAt(0)
        recyclerViewAdapter.updateList(tempList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun recyclerView() {
        recyclerViewAdapter = WeatherRecyclerViewAdapter(this@WeatherFragment)
        val recyclerView = binding.rvFutureWeatherInfo
        recyclerView.apply {
            recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }
}
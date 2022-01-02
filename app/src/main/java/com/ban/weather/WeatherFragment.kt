package com.ban.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ban.weather.models.WeatherResponseModel

private const val ARG_PARAM1 = "param1"

class WeatherFragment : Fragment() {
    private var param1: WeatherResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)


        // recycler view

        // view


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
}
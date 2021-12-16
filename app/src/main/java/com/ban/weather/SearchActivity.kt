package com.ban.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ban.weather.databinding.ActivitySearchBinding
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory

class SearchActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivitySearchBinding

    private val viewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra("test")
        var testText = binding.tvTestText.apply {
            text = message
        }

        viewModel.searchedCities.observe(this, {
            Log.d(TAG, "[observe]")
            testText.text = "city name searched: ${it[0].title} / woeid: ${it[0].woeid}"
        })

        binding.btSearchButton.setOnClickListener {
            val keyword = binding.etSearchKeyword

            hideKeyboard(keyword)
            searchCities(keyword.text.toString())
        }

    }

    fun searchCities(cityName: String) {
        viewModel.getCities(cityName) // Example:Tokyo
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
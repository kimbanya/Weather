package com.ban.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ban.weather.databinding.ActivitySearchBinding
import com.ban.weather.models.CityInfo
import com.ban.weather.view_models.MainViewModel
import com.ban.weather.view_models.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), ItemClickListener {

    private val TAG = javaClass.simpleName

    private lateinit var recyclerViewAdapter: SearchRecyclerViewAdapter

    private lateinit var binding : ActivitySearchBinding

    var numberOfCity = -1

    private val viewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.searchedCities.observe(this, {
            Log.d(TAG, "[observe]")
            initRecycler(it)
        })

        binding.btSearchButton.setOnClickListener {
            val keyword = binding.etSearchKeyword
            val keywordToString = keyword.text.toString()
            hideKeyboard(keyword)
            searchCities(keywordToString)
            refresh(keywordToString)
        }

        addObservers()
        refresh()

        // Intent test
//        val message = intent.getStringExtra("test")
//        var testText = binding.tvTestText.apply {
//            text = message
//        }
    }

    private fun addObservers() {
        viewModel.allCities.observe(this, {
            Log.d(TAG, "[addObservers] num of total cities ${it.size}")
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(dataList: List<SearchCityResponseModel>) {
        Log.d(TAG, "[initRecycler]")
        recyclerViewAdapter = SearchRecyclerViewAdapter(dataList, this, this)
        binding.rvSearchedCityList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    fun searchCities(cityName: String) {
        viewModel.getCities(cityName)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onItemClickListener(data: CityInfo) {
        Toast.makeText(this, "${data.cityName} / ${data.woeid}", Toast.LENGTH_SHORT).show()
        viewModel.saveCity(data)
    }

    fun refresh(searchKeyword: String = "") {
        searchCities(searchKeyword)
    }
}
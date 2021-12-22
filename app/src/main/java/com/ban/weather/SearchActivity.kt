package com.ban.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    var numberOfCitiesSaved = -1

    private val viewModel : MainViewModel by viewModels { MainViewModelFactory((application as WeatherApplication).repository) }

//    init {
//        if (numberOfCitiesSaved > 0) {
//            val favoriteList : List<CityInfo> = viewModel.favoriteList.value!!
//            initRecycler(favoriteList)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.presentingListMerged.observe(this, {
            Log.d(TAG, "[observe]")
            initRecycler(it)
        })

        binding.btSearchButton.setOnClickListener {
            val keyword = binding.etSearchKeyword
            val keywordToString = keyword.text.toString()
            hideKeyboard(keyword)
            searchCities(keywordToString)
        }

        addObservers()


        // Intent test
//        val message = intent.getStringExtra("test")
//        var testText = binding.tvTestText.apply {
//            text = message
//        }
    }

    private fun addObservers() {
        viewModel.numberOfCitiesSearched.observe(this,{
            Log.d(TAG, "[addObservers] : the number of cities searched => $it")
        })

        viewModel.favoriteList.observe(this, {
            Log.d(TAG, "[addObservers] : saved cities on DB => ${it.size}")
            numberOfCitiesSaved = it.size
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler(dataList: List<CityInfo>) {
        Log.d(TAG, "[initRecycler]")
        recyclerViewAdapter = SearchRecyclerViewAdapter(dataList, this, this)
        binding.rvSearchedCityList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun searchCities(cityName: String) {
        viewModel.getCities(cityName)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onItemClickListener(data: CityInfo) {
        Log.d(TAG, "[onItemClickListner] >> ${data.cityName}, ${data.isFavorite}")

        // save or delete process
        if (data.isFavorite) {
            viewModel.saveCity(data)
        } else {
            viewModel.deleteCity(data.woeid)
        }
    }

}
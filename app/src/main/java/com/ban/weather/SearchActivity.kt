package com.ban.weather

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ban.weather.adapters.SearchRecyclerViewAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSearchButton.setOnClickListener {
            val keyword = binding.etSearchKeyword
            val keywordToString = keyword.text.toString()
            hideSoftKeyboard()
            searchCities(keywordToString)
        }

        val editText = binding.etSearchKeyword
        editText.setOnEditorActionListener { textView, action, event ->
            var handled = false

            if(action == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                editText.clearFocus()
                editText.isCursorVisible = false
                searchCities(editText.text.toString())
                handled = true
            }
            handled
        }

        addObservers()
        initRecycler()
    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
    */

    private fun addObservers() {
        viewModel.numberOfCitiesSearched.observe(this,{
            Log.d(TAG, "[addObservers] >> numberOfCitiesSearched $it")
        })

        viewModel.favoriteList.observe(this, {
            Log.d(TAG, "[addObservers] >> favoriteList ${it.size}")
            numberOfCitiesSaved = it.size
            updateRecyclerView(it)
        })

        viewModel.presentingListMerged.observe(this, {
            Log.d(TAG, "[addObservers] >> presentingListMerged")
            updateRecyclerView(it)
        })

    }

    private fun updateRecyclerView(dataList: List<CityInfo>) {
        recyclerViewAdapter.updateList(dataList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        recyclerViewAdapter = SearchRecyclerViewAdapter(this, this)
        binding.rvSearchedCityList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun searchCities(cityName: String) {
        viewModel.getCities(cityName)
    }

    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
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
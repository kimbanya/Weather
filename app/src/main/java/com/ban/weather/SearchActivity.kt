package com.ban.weather

import android.annotation.SuppressLint
import android.app.Activity
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

        /*
        binding.etSearchKeyword.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN || keyCode == KeyEvent.KEYCODE_ENTER) { // KeyCode:67
                Log.d(TAG, "[setOnKeyListener] >> ${event.action} // $keyCode")

                return@OnKeyListener true
            }else {
                return@OnKeyListener false
            }
        })
         */

        /*
        // etSearchKeyword 완료 클릭 시
        binding.etSearchKeyword.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.btSearchButton.performClick()
                handled = true
            }
            handled
        }
         */

//        binding.etSearchKeyword.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                Log.d(TAG, "[onKey]")
//                val keyword = binding.etSearchKeyword
//                hideSoftKeyboard()
//                keyword.clearFocus()
//                keyword.isCursorVisible = false
//                searchCities(keyword.text.toString())
//            }
//            true
//        }

        /*
        binding.etSearchKeyword.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.d(TAG, "[onKey]")
                val keyword = binding.etSearchKeyword
                hideSoftKeyboard()
                keyword.clearFocus()
                keyword.isCursorVisible = false
                searchCities(keyword.text.toString())
                return@OnKeyListener true
            }
            false
        })
         */

        /*
        binding.etSearchKeyword.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                if (event!!.action == KeyEvent.KEYCODE_ENTER) {
                    Log.d(TAG, "[onKey]")
                    val keyword = binding.etSearchKeyword
                    hideSoftKeyboard()
                    keyword.clearFocus()
                    keyword.isCursorVisible = false
                    searchCities(keyword.text.toString())
                    return true
                }
                return false
            }
        })
         */

        addObservers()
        initRecycler()

    }

    private fun addObservers() {
        viewModel.numberOfCitiesSearched.observe(this,{
            Log.d(TAG, "[addObservers] : the number of cities searched => $it")
        })

        viewModel.favoriteList.observe(this, {
            Log.d(TAG, "[addObservers] : saved cities on DB => ${it.size}")
            numberOfCitiesSaved = it.size
            updateRecyclerView(it)
        })

        viewModel.presentingListMerged.observe(this, {
            Log.d(TAG, "[observe]")
            updateRecyclerView(it)
        })

    }

    private fun updateRecyclerView(dataList: List<CityInfo>) {
        recyclerViewAdapter.updateList(dataList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        Log.d(TAG, "[initRecycler]")
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

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
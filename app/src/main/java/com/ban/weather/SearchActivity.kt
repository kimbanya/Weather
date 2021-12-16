package com.ban.weather

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.ban.weather.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var binding : ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val message = intent.getStringExtra("test")
        var testText = binding.tvTestText.apply {
            text = message
        }

        binding.btSearchButton.setOnClickListener {
            val keyword = binding.etSearchKeyword
            testText.text = keyword.text

            hideKeyboard(keyword)
        }

    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
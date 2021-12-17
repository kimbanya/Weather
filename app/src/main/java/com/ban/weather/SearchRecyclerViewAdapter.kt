package com.ban.weather

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ban.weather.databinding.ItemSearchBinding

class SearchRecyclerViewAdapter(val dataList: List<SearchCityResponseModel>, val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.simpleName

    private var cityDisplayInfoList: List<SearchCityResponseModel> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "[onCreateViewHolder]")

        val binding : ItemSearchBinding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "[onBindViewHolder]")

        (holder as SearchViewHolder).bind(dataList[position], context)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "[getItemCount]")

        return cityDisplayInfoList.size
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        val cityName = binding.tvCityTitle
        val setPreffered = binding.ivSaveFavoriteButton

        fun bind(position: SearchCityResponseModel, context: Context) {
            cityName.text = position.title
//            setPreffered.setImageResource()
            Log.d(TAG, "[SearchViewHolder inner class > bind]")
        }
    }

}
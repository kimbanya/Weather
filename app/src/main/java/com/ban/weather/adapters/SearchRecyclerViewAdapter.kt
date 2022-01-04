package com.ban.weather.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ban.weather.ItemClickListener
import com.ban.weather.R
import com.ban.weather.databinding.ItemSearchBinding
import com.ban.weather.models.CityInfo

class SearchRecyclerViewAdapter(
    private val context: Context, val itemClickListener: ItemClickListener
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.simpleName

    private var cityDisplayInfoList: List<CityInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding : ItemSearchBinding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchViewHolder).bind(cityDisplayInfoList[position], context)
    }

    override fun getItemCount(): Int = cityDisplayInfoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data : List<CityInfo>) {
        cityDisplayInfoList = data
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cityName = binding.tvCityTitle
        private val saveButton = binding.ivSaveFavoriteButton

        fun bind(position: CityInfo, context: Context) {
            cityName.text = position.cityName

            // All Data to Present
            if (position.isFavorite) {
                saveButton.setImageResource(R.drawable.img_save_btn_after_clicked)
            } else {
                saveButton.setImageResource(R.drawable.img_save_btn_before_clicked)
            }

            // Save City Process
            saveButton.setOnClickListener {
                position.isFavorite = !position.isFavorite
                if (position.isFavorite) {
                    saveButton.setImageResource(R.drawable.img_save_btn_after_clicked)
                } else {
                    saveButton.setImageResource(R.drawable.img_save_btn_before_clicked)
                }
                itemClickListener.onItemClickListener(position)
            }
        }
    }

}
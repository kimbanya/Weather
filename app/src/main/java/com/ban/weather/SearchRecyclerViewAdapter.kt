package com.ban.weather

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ban.weather.databinding.ItemSearchBinding
import com.ban.weather.models.CityInfo

class SearchRecyclerViewAdapter(
    val context: Context,
    val itemClickListener: ItemClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = javaClass.simpleName

    private var cityDisplayInfoList: List<CityInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "[onCreateViewHolder]")

        val binding : ItemSearchBinding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "[onBindViewHolder]")

        (holder as SearchViewHolder).bind(cityDisplayInfoList[position], context)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "[getItemCount]")

        return cityDisplayInfoList.size
    }

    fun updateList(data : List<CityInfo>) {
        cityDisplayInfoList = data
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cityName = binding.tvCityTitle
        private val saveButton = binding.ivSaveFavoriteButton

        fun bind(position: CityInfo, context: Context) {
            cityName.text = position.cityName

            // all presenting data
            if (position.isFavorite) {
                saveButton.setImageResource(R.drawable.img_save_btn_after_clicked)
            } else {
                saveButton.setImageResource(R.drawable.img_save_btn_before_clicked)
            }

            // process when save button clicked
            saveButton.setOnClickListener {
                position.isFavorite = !position.isFavorite
                if (position.isFavorite) {
                    saveButton.setImageResource(R.drawable.img_save_btn_after_clicked)
                } else {
                    saveButton.setImageResource(R.drawable.img_save_btn_before_clicked)
                }

                itemClickListener.onItemClickListener(position)
            }
            Log.d(TAG, "[SearchViewHolder inner class > bind]")
        }
    }

}
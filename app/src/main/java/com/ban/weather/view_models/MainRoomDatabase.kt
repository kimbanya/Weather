package com.ban.weather.view_models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ban.weather.models.CityInfo

@Database(entities = [CityInfo::class], version = 1)
abstract class MainRoomDatabase : RoomDatabase() {

    abstract fun mainDao() : MainDao

    companion object {
        @Volatile
        private var INSTANCE : MainRoomDatabase? = null

        fun getDatabase(context : Context) : MainRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainRoomDatabase::class.java,
                    "database").build()

                INSTANCE = instance
                return instance
            }
        }
    }

}
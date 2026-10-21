package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ShopOfferEntity::class,
        PriceHistoryEntity::class,
        PriceAlertEntity::class,
        GitHubReleaseLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Ps5Database : RoomDatabase() {
    abstract fun ps5Dao(): Ps5Dao

    companion object {
        @Volatile
        private var INSTANCE: Ps5Database? = null

        fun getInstance(context: Context): Ps5Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Ps5Database::class.java,
                    "ps5_pro_radar.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

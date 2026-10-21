package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Ps5Dao {
    @Query("SELECT * FROM shop_offers ORDER BY CASE id WHEN 'mediamarkt' THEN 1 WHEN 'saturn' THEN 2 WHEN 'otto' THEN 3 WHEN 'amazon' THEN 4 WHEN 'psdirect' THEN 5 WHEN 'ebay_scalper' THEN 6 WHEN 'ebay_used' THEN 7 ELSE 8 END")
    fun getAllShopOffers(): Flow<List<ShopOfferEntity>>

    @Query("SELECT * FROM shop_offers ORDER BY CASE id WHEN 'mediamarkt' THEN 1 WHEN 'saturn' THEN 2 WHEN 'otto' THEN 3 WHEN 'amazon' THEN 4 WHEN 'psdirect' THEN 5 WHEN 'ebay_scalper' THEN 6 WHEN 'ebay_used' THEN 7 ELSE 8 END")
    suspend fun getAllShopOffersOnce(): List<ShopOfferEntity>

    @Query("SELECT COUNT(*) FROM shop_offers")
    suspend fun getShopOffersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopOffers(offers: List<ShopOfferEntity>)

    @Query("DELETE FROM shop_offers WHERE id = 'ebay'")
    suspend fun cleanupLegacyEbayRow()

    @Update
    suspend fun updateShopOffer(offer: ShopOfferEntity)

    @Query("SELECT * FROM price_history ORDER BY dayOffset ASC")
    fun getAllPriceHistory(): Flow<List<PriceHistoryEntity>>

    @Query("SELECT * FROM price_history ORDER BY dayOffset ASC")
    suspend fun getAllPriceHistoryOnce(): List<PriceHistoryEntity>

    @Query("SELECT COUNT(*) FROM price_history")
    suspend fun getPriceHistoryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceHistory(entries: List<PriceHistoryEntity>)

    @Query("DELETE FROM price_history")
    suspend fun clearAllPriceHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSinglePriceHistory(entry: PriceHistoryEntity)

    @Query("SELECT * FROM price_alerts WHERE id = 1")
    fun getPriceAlert(): Flow<PriceAlertEntity?>

    @Query("SELECT * FROM price_alerts WHERE id = 1")
    suspend fun getPriceAlertOnce(): PriceAlertEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPriceAlert(alert: PriceAlertEntity)
}

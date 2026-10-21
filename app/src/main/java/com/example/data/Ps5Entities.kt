package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AvailabilityStatus {
    OUT_OF_STOCK_SCALPERS,      // Nicht verfügbar wegen Scalpern (MediaMarkt, Saturn, etc.)
    SCALPER_THIRD_PARTY_ONLY,   // Nur überteuerte Marketplace-/Scalper-Händler gelistet
    IN_STOCK_MSRP               // Regulär im Lager zum Normalpreis / UVP verfügbar -> Jetzt zuschlagen!
}

@Entity(tableName = "shop_offers")
data class ShopOfferEntity(
    @PrimaryKey val id: String,
    val shopName: String,
    val shopSubtitle: String,
    val currentPrice: Double,
    val msrpPrice: Double = 799.99,
    val availabilityStatus: String, // AvailabilityStatus.name
    val statusBadgeText: String,
    val scalperAnalysisNote: String,
    val productUrl: String,
    val deliveryEstimate: String,
    val isOfficialRetailer: Boolean = true,
    val lastHttpStatus: Int = 200,
    val lastCheckedTimestamp: Long = System.currentTimeMillis()
) {
    val parsedStatus: AvailabilityStatus
        get() = try {
            AvailabilityStatus.valueOf(availabilityStatus)
        } catch (_: Exception) {
            AvailabilityStatus.OUT_OF_STOCK_SCALPERS
        }

    val isAvailableAtFairPrice: Boolean
        get() = parsedStatus == AvailabilityStatus.IN_STOCK_MSRP && currentPrice <= msrpPrice + 5.0
}

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOffset: Int, // -90 to 0 (0 = Heute)
    val dateLabel: String,
    val officialMarketPrice: Double, // Regulärer Marktpreis (799,99 € oder wenn vergriffen letzter Listenpreis)
    val lowestAvailablePrice: Double, // Tatsächlich kaufbarer Preis inkl. Scalper-Aufschlag wenn ausverkauft
    val scalperPeakPrice: Double, // Scalper-Marktpreis (920 € - 1099 €)
    val officialInStockCount: Int, // Wie viele der 5 Hauptmärkte hatten Lagerbestand
    val eventNote: String
)

@Entity(tableName = "price_alerts")
data class PriceAlertEntity(
    @PrimaryKey val id: Int = 1,
    val targetPrice: Double = 799.99,
    val alertOnAnyRestock: Boolean = true,
    val watchMediaMarkt: Boolean = true,
    val watchSaturn: Boolean = true,
    val watchOtto: Boolean = true,
    val watchAmazon: Boolean = true,
    val watchPsDirect: Boolean = true,
    val isEnabled: Boolean = true,
    val lastTriggeredBanner: String = "Wartet auf neue PS5 Pro Lieferung zum UVP (799,99 €) bei MediaMarkt & Saturn."
)

@Entity(tableName = "github_releases")
data class GitHubReleaseLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val repoFullName: String,
    val tagName: String,
    val releaseName: String,
    val releaseHtmlUrl: String,
    val apkDownloadUrl: String,
    val apkFileName: String,
    val apkSizeFormatted: String,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

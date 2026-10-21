package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AvailabilityStatus
import com.example.data.PriceAlertEntity
import com.example.data.PriceHistoryEntity
import com.example.data.Ps5Database
import com.example.data.Ps5Repository
import com.example.data.ShopOfferEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

enum class ChartRangeFilter(val label: String, val pointCount: Int) {
    TODAY_LIVE("Heute (Uhrzeit)", 7),
    DAYS_7("7 Tage", 14),
    DAYS_30("30 Tage", 36)
}

enum class AppTab {
    SHOPS,
    DIAGRAMM,
    INFO_GUIDE,
    SCALPER_SCRAPER,
    ALARM_SETTINGS
}

data class MarketRecommendation(
    val shouldBuyNow: Boolean,
    val headline: String,
    val subheadline: String,
    val detailedAdvice: String,
    val officialInStockShops: List<String>,
    val ebayLiveScalperPrice: Double,
    val ebayNormalUsedPrice: Double,
    val scalperMarkupEuro: Double,
    val lastUpdatedLabel: String
)

data class Ps5UiState(
    val currentTab: AppTab = AppTab.SHOPS,
    val officialShopOffers: List<ShopOfferEntity> = emptyList(),
    val ebayScraperOffers: List<ShopOfferEntity> = emptyList(),
    val allOffers: List<ShopOfferEntity> = emptyList(),
    val filteredHistory: List<PriceHistoryEntity> = emptyList(),
    val selectedRange: ChartRangeFilter = ChartRangeFilter.TODAY_LIVE,
    val selectedPointIndex: Int = -1,
    val isRefreshingLive: Boolean = false,
    val secondsUntilNextAutoRefresh: Int = 15,
    val autoSyncCycleCount: Int = 1,
    val showQuickInfoDialog: Boolean = false,
    val recommendation: MarketRecommendation = MarketRecommendation(
        shouldBuyNow = false,
        headline = "NICHT EMPFOHLEN BEI SCALPERN – AUF UVP ODER GEBRAUCHT-DEAL ACHTEN!",
        subheadline = "MediaMarkt, Saturn, OTTO, Amazon & PlayStation.com: Derzeit nicht verfügbar",
        detailedAdvice = "Bei MediaMarkt, Saturn, OTTO, Amazon und PlayStation.com ist die PS5 Pro aktuell nicht verfügbar. Auf eBay verlangen Scalper über 1.000 € (NICHT kaufen!), während normale gebrauchte PS5 Pro Konsolen bei eBay bereits unter 799,99 € gehandelt werden.",
        officialInStockShops = emptyList(),
        ebayLiveScalperPrice = 1049.00,
        ebayNormalUsedPrice = 749.00,
        scalperMarkupEuro = 249.01,
        lastUpdatedLabel = "Live"
    ),
    val priceAlert: PriceAlertEntity = PriceAlertEntity(),
    val statusBannerMessage: String? = null
)

class Ps5TrackerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Ps5Repository

    private val _currentTab = MutableStateFlow(AppTab.SHOPS)
    private val _selectedRange = MutableStateFlow(ChartRangeFilter.TODAY_LIVE)
    private val _selectedPointIndex = MutableStateFlow(-1)
    private val _isRefreshingLive = MutableStateFlow(false)
    private val _secondsUntilNextRefresh = MutableStateFlow(15)
    private val _autoSyncCycleCount = MutableStateFlow(1)
    private val _showQuickInfoDialog = MutableStateFlow(false)
    private val _statusBannerMessage = MutableStateFlow<String?>(null)

    init {
        val db = Ps5Database.getInstance(application)
        repository = Ps5Repository(db.ps5Dao())
        Ps5NotificationHelper.ensureChannelCreated(application)

        viewModelScope.launch {
            repository.initializeIfNeeded()

            // Continuous self-updating loop that refreshes MediaMarkt, Saturn, OTTO, Amazon, PlayStation.com & eBay automatically
            while (isActive) {
                _isRefreshingLive.value = true
                val newlyAvailable = repository.refreshRealLiveMarketData()
                _isRefreshingLive.value = false
                _autoSyncCycleCount.value += 1

                if (newlyAvailable.isNotEmpty() && uiState.value.priceAlert.isEnabled) {
                    val shopsList = newlyAvailable.joinToString(", ")
                    Ps5NotificationHelper.sendStockNotification(
                        context = getApplication(),
                        title = "PS5 Pro wieder verfügbar! Jetzt kaufen!",
                        message = "Die PlayStation 5 Pro ist jetzt bei $shopsList zum UVP (799,99 €) verfügbar!"
                    )
                }

                for (remaining in 15 downTo 1) {
                    _secondsUntilNextRefresh.value = remaining
                    delay(1000L)
                }
            }
        }
    }

    val uiState: StateFlow<Ps5UiState> = combine(
        repository.shopOffers,
        repository.priceHistory,
        repository.priceAlert,
        _currentTab
    ) { offers, history, alert, tab ->
        Quadruple(offers, history, alert ?: PriceAlertEntity(), tab)
    }.combine(
        combine(
            _selectedRange,
            _selectedPointIndex,
            _isRefreshingLive,
            _statusBannerMessage
        ) { range, pointIdx, refreshing, banner ->
            Quadruple(range, pointIdx, refreshing, banner)
        }
    ) { firstQuad, secondQuad ->
        Pair(firstQuad, secondQuad)
    }.combine(
        combine(_secondsUntilNextRefresh, _autoSyncCycleCount, _showQuickInfoDialog) { sec, count, showInfo ->
            Triple(sec, count, showInfo)
        }
    ) { (firstQuad, secondQuad), (sec, cycleCount, showInfo) ->
        val (offers, history, alert, tab) = firstQuad
        val (range, pointIdx, refreshing, banner) = secondQuad

        val filteredHist = history.takeLast(range.pointCount)
        val safePointIdx = if (pointIdx in filteredHist.indices) pointIdx else filteredHist.lastIndex

        val officialOffers = offers.filter { it.isOfficialRetailer }
        val ebayOffers = offers.filter { !it.isOfficialRetailer }

        Ps5UiState(
            currentTab = tab,
            officialShopOffers = officialOffers,
            ebayScraperOffers = ebayOffers,
            allOffers = offers,
            filteredHistory = filteredHist,
            selectedRange = range,
            selectedPointIndex = safePointIdx,
            isRefreshingLive = refreshing,
            secondsUntilNextAutoRefresh = sec,
            autoSyncCycleCount = cycleCount,
            showQuickInfoDialog = showInfo,
            recommendation = computeLiveRecommendation(offers, filteredHist),
            priceAlert = alert,
            statusBannerMessage = banner
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Ps5UiState()
    )

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setQuickInfoDialogVisible(visible: Boolean) {
        _showQuickInfoDialog.value = visible
    }

    fun selectChartRange(range: ChartRangeFilter) {
        _selectedRange.value = range
        _selectedPointIndex.value = -1
    }

    fun selectChartPoint(index: Int) {
        _selectedPointIndex.value = index
    }

    fun clearBannerMessage() {
        _statusBannerMessage.value = null
    }

    fun showStatusMessage(message: String) {
        _statusBannerMessage.value = message
    }

    fun savePriceAlert(alert: PriceAlertEntity) {
        viewModelScope.launch {
            repository.updatePriceAlertSettings(alert)
            _statusBannerMessage.value = "Handy-Benachrichtigungen & Scraper-Einstellungen gespeichert!"
        }
    }

    fun triggerManualRefresh() {
        if (_isRefreshingLive.value) return
        viewModelScope.launch {
            _isRefreshingLive.value = true
            val newlyAvailable = repository.refreshRealLiveMarketData()
            _isRefreshingLive.value = false
            _autoSyncCycleCount.value += 1
            _secondsUntilNextRefresh.value = 15
            if (newlyAvailable.isNotEmpty()) {
                _statusBannerMessage.value = "Verfügbar bei: ${newlyAvailable.joinToString(", ")}!"
            } else {
                _statusBannerMessage.value = "Live-Check abgeschlossen: Alle 5 Märkte & eBay aktualisiert."
            }
        }
    }

    private fun computeLiveRecommendation(
        offers: List<ShopOfferEntity>,
        history: List<PriceHistoryEntity>
    ): MarketRecommendation {
        val availableOfficialStores = offers.filter {
            it.isOfficialRetailer && it.parsedStatus == AvailabilityStatus.IN_STOCK_MSRP
        }
        val latestPoint = history.lastOrNull()
        val ebayScalperOffer = offers.find { it.id == "ebay_scalper" }
        val ebayUsedOffer = offers.find { it.id == "ebay_used" }

        val scalperPrice = ebayScalperOffer?.currentPrice ?: latestPoint?.scalperPeakPrice ?: 1049.00
        val normalUsedPrice = ebayUsedOffer?.currentPrice ?: latestPoint?.lowestAvailablePrice ?: 749.00
        val markup = (scalperPrice - 799.99).coerceAtLeast(0.0)
        val lastUpdatedLabel = latestPoint?.dateLabel ?: "Gerade eben"

        return if (availableOfficialStores.isNotEmpty()) {
            val shopNames = availableOfficialStores.map { it.shopName }
            MarketRecommendation(
                shouldBuyNow = true,
                headline = "EMPFOHLEN – JETZT KAUFEN! (799,99 € UVP VERFÜGBAR)",
                subheadline = "Offiziell auf Lager bei: ${shopNames.joinToString(", ")}",
                detailedAdvice = "Live-Check ($lastUpdatedLabel Uhr): Die PlayStation 5 Pro ist jetzt bei ${shopNames.joinToString(", ")} zum regulären UVP-Preis von 799,99 € verfügbar! Jetzt direkt zuschlagen und überteuerte eBay-Scalper meiden.",
                officialInStockShops = shopNames,
                ebayLiveScalperPrice = scalperPrice,
                ebayNormalUsedPrice = normalUsedPrice,
                scalperMarkupEuro = markup,
                lastUpdatedLabel = lastUpdatedLabel
            )
        } else {
            MarketRecommendation(
                shouldBuyNow = false,
                headline = "ACHTUNG SCALPER-PREISE (${String.format(Locale.GERMANY, "%.2f €", scalperPrice)}) – NICHT BEI SCALPERN KAUFEN!",
                subheadline = "MediaMarkt, Saturn, OTTO, Amazon & PlayStation.com: Derzeit nicht verfügbar",
                detailedAdvice = "Stand $lastUpdatedLabel Uhr: Bei MediaMarkt, Saturn, OTTO, Amazon und PlayStation.com ist Neuware (799,99 € UVP) aktuell nicht verfügbar. eBay-Scalper verlangen ${String.format(Locale.GERMANY, "%.2f €", scalperPrice)} (+${String.format(Locale.GERMANY, "%.0f €", markup)} Aufpreis – NICHT empfohlen!). Normale gebrauchte PS5 Pro Angebote auf eBay liegen aktuell bei ca. ${String.format(Locale.GERMANY, "%.2f €", normalUsedPrice)}.",
                officialInStockShops = emptyList(),
                ebayLiveScalperPrice = scalperPrice,
                ebayNormalUsedPrice = normalUsedPrice,
                scalperMarkupEuro = markup,
                lastUpdatedLabel = lastUpdatedLabel
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

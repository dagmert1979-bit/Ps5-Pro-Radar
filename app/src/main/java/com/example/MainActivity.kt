package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.MarketRecommendation
import com.example.ui.Ps5NotificationHelper
import com.example.ui.Ps5TrackerViewModel
import com.example.ui.components.HeroPs5PriceChartCard
import com.example.ui.components.ShopOfferCard
import com.example.ui.screens.InfoGuideScreen
import com.example.ui.screens.PriceAlertScreen
import com.example.ui.screens.QuickInfoGlassDialog
import com.example.ui.theme.ActiveNavRedBottom
import com.example.ui.theme.ActiveNavRedRim
import com.example.ui.theme.ActiveNavRedTop
import com.example.ui.theme.CrimsonButtonBorder
import com.example.ui.theme.CrimsonButtonGlass
import com.example.ui.theme.CrimsonGlowMid
import com.example.ui.theme.CrimsonGlowTop
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassDivider
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCardElevated
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ProgressTrackDark
import com.example.ui.theme.PsCyanGlow
import com.example.ui.theme.PsElectricBlue
import com.example.ui.theme.PsIndigoAccent
import com.example.ui.theme.PsPeriwinkleGlow
import com.example.ui.theme.ScalperAmber
import com.example.ui.theme.StockEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Ps5ProRadarApp()
            }
        }
    }
}

@Composable
fun Ps5ProRadarApp(
    viewModel: Ps5TrackerViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (state.currentTab != AppTab.SHOPS) {
        BackHandler {
            viewModel.selectTab(AppTab.SHOPS)
        }
    }

    fun openExternalUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    // Kurz-Info Glass Popup Dialog when clicking the top-right "?" button
    if (state.showQuickInfoDialog) {
        QuickInfoGlassDialog(
            recommendation = state.recommendation,
            secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
            onDismiss = { viewModel.setQuickInfoDialogVisible(false) },
            onOpenFullGuide = { viewModel.selectTab(AppTab.INFO_GUIDE) }
        )
    }

    // Root Box with Deep Obsidian Background + Dual-Tone Ruby/Cyber-Indigo Ambient Glow
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        // Multi-layered Atmospheric Top Aura (Ruby-Crimson + Subtle PlayStation Indigo sheen)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(370.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CrimsonGlowTop,
                        CrimsonGlowMid,
                        ObsidianBg
                    )
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ActiveNavRedTop.copy(alpha = 0.28f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.82f, size.height * 0.18f),
                    radius = size.width * 0.65f
                ),
                center = Offset(size.width * 0.82f, size.height * 0.18f),
                radius = size.width * 0.65f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        PsIndigoAccent.copy(alpha = 0.16f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.18f, size.height * 0.35f),
                    radius = size.width * 0.55f
                ),
                center = Offset(size.width * 0.18f, size.height * 0.35f),
                radius = size.width * 0.55f
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                Ps5TopHeaderBar(
                    scalperPrice = state.recommendation.ebayLiveScalperPrice,
                    usedPrice = state.recommendation.ebayNormalUsedPrice,
                    shouldBuyNow = state.recommendation.shouldBuyNow,
                    onQuestionMarkClick = {
                        viewModel.setQuickInfoDialogVisible(true)
                    },
                    onBellClick = {
                        val sent = Ps5NotificationHelper.sendStockNotification(
                            context = context,
                            title = "PS5 Pro Radar • Handy-Alarm bereit",
                            message = "Du wirst sofort benachrichtigt, sobald MediaMarkt, Saturn, OTTO, Amazon oder PlayStation.com die PS5 Pro zum UVP (799,99 €) auf Lager hat!"
                        )
                        if (sent) {
                            viewModel.showStatusMessage("Handy-Benachrichtigung an Statusleiste gesendet!")
                        } else {
                            viewModel.selectTab(AppTab.ALARM_SETTINGS)
                            viewModel.showStatusMessage("Bitte hier die Handy-Benachrichtigung erlauben.")
                        }
                    },
                    onRefreshClick = {
                        viewModel.triggerManualRefresh()
                    }
                )
            },
            bottomBar = {
                Ps5FloatingBottomBar(
                    currentTab = state.currentTab,
                    onSelectTab = viewModel::selectTab
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedVisibility(visible = state.statusBannerMessage != null) {
                    state.statusBannerMessage?.let { msg ->
                        Surface(
                            color = MidnightCardElevated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.2.dp, ActiveNavRedTop)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextWhite,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.clearBannerMessage() },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Schließen",
                                        tint = TextSilver,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                when (state.currentTab) {
                    AppTab.SHOPS -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("shops_lazy_column"),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // 1. Upgraded 3-Row Dark-Glass Radar Matrix Card + 5-Shop Signal Bar
                            item {
                                Ps5UnifiedGlassShowcaseCard(
                                    recommendation = state.recommendation,
                                    secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
                                    isRefreshingLive = state.isRefreshingLive,
                                    autoSyncCycleCount = state.autoSyncCycleCount,
                                    onClickDiagramm = { viewModel.selectTab(AppTab.DIAGRAMM) },
                                    onClickInfo = { viewModel.setQuickInfoDialogVisible(true) },
                                    onClickScraper = { viewModel.selectTab(AppTab.SCALPER_SCRAPER) }
                                )
                            }

                            // 2. Section Header "Offizielle Märkte & Direktlinks"
                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Offizielle Märkte & Links",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = TextWhite,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Auto-Refresh ${state.secondsUntilNextAutoRefresh}s",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = PsCyanGlow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // 3. Official Retailer Dark-Glass Pill Cards (MediaMarkt, Saturn, OTTO, Amazon, PlayStation.com)
                            items(
                                items = state.officialShopOffers,
                                key = { it.id }
                            ) { offer ->
                                ShopOfferCard(
                                    offer = offer,
                                    onOpenShopUrl = ::openExternalUrl
                                )
                            }

                            // 4. Interactive Radar Action Pills
                            item {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Radar-Funktionen",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextWhite,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            item {
                                QuickGlassActionPill(
                                    title = "Kurz-Info & Kauf-Ratgeber (?)",
                                    subtitle = "Wann lohnt sich der Kauf? → Kurz-Info öffnen",
                                    leadingStarColor = PsCyanGlow,
                                    rightCheckCount = 3,
                                    onClick = { viewModel.setQuickInfoDialogVisible(true) }
                                )
                            }

                            item {
                                QuickGlassActionPill(
                                    title = "Live-Preisdiagramm & Kurve",
                                    subtitle = "Uhrzeit-Verlauf & Scalper-Warnung → Öffnen",
                                    leadingIcon = Icons.Default.PlayArrow,
                                    leadingIconTint = Color(0xFFFF6B00),
                                    rightCheckCount = 5,
                                    onClick = { viewModel.selectTab(AppTab.DIAGRAMM) }
                                )
                            }

                            item {
                                QuickGlassActionPill(
                                    title = "Handy-Alarm & 5-Märkte-Filter",
                                    subtitle = "+5 Offizielle Händler im Hintergrund-Check",
                                    leadingIcon = Icons.Default.Group,
                                    leadingIconTint = StockEmerald,
                                    rightCounterText = "5/5",
                                    onClick = { viewModel.selectTab(AppTab.ALARM_SETTINGS) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }

                    AppTab.DIAGRAMM -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("diagramm_lazy_column"),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                HeroPs5PriceChartCard(
                                    history = state.filteredHistory,
                                    selectedRange = state.selectedRange,
                                    selectedPointIndex = state.selectedPointIndex,
                                    recommendation = state.recommendation,
                                    isRefreshingLive = state.isRefreshingLive,
                                    secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
                                    onSelectRange = viewModel::selectChartRange,
                                    onSelectPoint = viewModel::selectChartPoint
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }

                    AppTab.INFO_GUIDE -> {
                        InfoGuideScreen(
                            recommendation = state.recommendation,
                            secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
                            autoSyncCycleCount = state.autoSyncCycleCount,
                            onNavigateToShops = { viewModel.selectTab(AppTab.SHOPS) },
                            onNavigateToDiagramm = { viewModel.selectTab(AppTab.DIAGRAMM) },
                            onTriggerRefresh = { viewModel.triggerManualRefresh() }
                        )
                    }

                    AppTab.SCALPER_SCRAPER -> {
                        PriceAlertScreen(
                            ebayOffers = state.ebayScraperOffers,
                            alert = state.priceAlert,
                            secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
                            autoSyncCycleCount = state.autoSyncCycleCount,
                            onOpenShopUrl = ::openExternalUrl,
                            onSaveAlert = viewModel::savePriceAlert,
                            onShowMessage = viewModel::showStatusMessage,
                            focusSettingsFirst = false
                        )
                    }

                    AppTab.ALARM_SETTINGS -> {
                        PriceAlertScreen(
                            ebayOffers = state.ebayScraperOffers,
                            alert = state.priceAlert,
                            secondsUntilNextAutoRefresh = state.secondsUntilNextAutoRefresh,
                            autoSyncCycleCount = state.autoSyncCycleCount,
                            onOpenShopUrl = ::openExternalUrl,
                            onSaveAlert = viewModel::savePriceAlert,
                            onShowMessage = viewModel::showStatusMessage,
                            focusSettingsFirst = true
                        )
                    }
                }
            }
        }
    }
}

/**
 * Upgraded Top Header Bar:
 * Left: Live UVP & Scalper/Gebraucht telemetry with glowing 4-point stars (✦)
 * Right: 3 interactive circular dark-glass buttons:
 *  - ? : Opens the Kurz-Info & Kaufempfehlung Glass Overlay Dialog
 *  - 🔔 : Sends/tests the Android phone push notification immediately
 *  - ↻ : Triggers an immediate live sync across all 5 stores & eBay
 */
@Composable
private fun Ps5TopHeaderBar(
    scalperPrice: Double,
    usedPrice: Double,
    shouldBuyNow: Boolean,
    onQuestionMarkClick: () -> Unit,
    onBellClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Offizielle UVP:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSilver,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "799 €",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (shouldBuyNow) StockEmerald else TextWhite,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                FourPointSparkleStar(size = 16.dp, color = TextWhite)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Scalper / Gebr.:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSilver,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${scalperPrice.toInt()} € / ${usedPrice.toInt()} €",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                FourPointSparkleStar(size = 15.dp, color = PsCyanGlow)
            }
        }

        // Right 3 Circular Frosted Glass Buttons (?, 🔔, ↻)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularGlassTopButton(
                icon = Icons.Default.HelpOutline,
                contentDescription = "Kurz-Info & Kaufempfehlung öffnen",
                onClick = onQuestionMarkClick,
                testTag = "top_question_info_button"
            )
            CircularGlassTopButton(
                icon = Icons.Default.NotificationsNone,
                contentDescription = "Handy-Alarm sofort testen",
                onClick = onBellClick,
                testTag = "top_bell_alarm_button"
            )
            CircularGlassTopButton(
                icon = Icons.Default.Autorenew,
                contentDescription = "Alle Märkte sofort live prüfen",
                onClick = onRefreshClick,
                testTag = "top_refresh_live_button"
            )
        }
    }
}

@Composable
private fun CircularGlassTopButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CrimsonButtonGlass,
                        Color(0xFF1A1217)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.28f),
                        CrimsonButtonBorder
                    )
                ),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = TextWhite,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Upgraded 3-Row Dark-Glass Radar Matrix Card with Neon Specular Rim,
 * Vertical Right-Column Telemetry Split, and Tailored 5-Shop Signal Bar at the bottom.
 */
@Composable
private fun Ps5UnifiedGlassShowcaseCard(
    recommendation: MarketRecommendation,
    secondsUntilNextAutoRefresh: Int,
    isRefreshingLive: Boolean,
    autoSyncCycleCount: Int,
    onClickDiagramm: () -> Unit,
    onClickInfo: () -> Unit,
    onClickScraper: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        border = BorderStroke(
            width = 1.4.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.28f),
                    ActiveNavRedTop.copy(alpha = 0.45f),
                    GlassBorder,
                    Color.White.copy(alpha = 0.08f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MidnightCardElevated.copy(alpha = 0.92f),
                            MidnightCard,
                            Color(0xFF121217)
                        )
                    )
                )
        ) {
            // Row 1: Offizielle Elektronikmärkte (MediaMarkt & Saturn)
            UnifiedGlassShowcaseRow(
                overline = "ELEKTRONIKMÄRKTE • AUTO-RADAR",
                title = "MediaMarkt & Saturn",
                statusIcon = Icons.Default.Check,
                statusText = if (recommendation.shouldBuyNow) {
                    "Auf Lager • Jetzt zum UVP kaufen!"
                } else {
                    "Live-Überwachung aktiv (#$autoSyncCycleCount)"
                },
                statusColor = StockEmerald,
                rightLabel = "Auto-Check in",
                rightValue = if (isRefreshingLive) {
                    "Prüfe..."
                } else {
                    "00:${String.format(Locale.GERMANY, "%02d", secondsUntilNextAutoRefresh)}s Live"
                },
                onClick = onClickDiagramm
            )

            HorizontalDivider(color = GlassDivider, thickness = 1.dp)

            // Row 2: Online-Versand & Direktverkauf (OTTO, Amazon & PlayStation.com)
            UnifiedGlassShowcaseRow(
                overline = "DIREKTVERKAUF • PREISGARANTIE",
                title = "OTTO, Amazon & PS Direct",
                statusIcon = Icons.Default.Storefront,
                statusText = if (recommendation.shouldBuyNow) {
                    "Verfügbar für 799,99 €"
                } else {
                    "5/5 Märkte geprüft • Nicht verfügbar"
                },
                statusColor = ScalperAmber,
                rightLabel = "Offizieller UVP",
                rightValue = "799,99 €",
                onClick = onClickInfo
            )

            HorizontalDivider(color = GlassDivider, thickness = 1.dp)

            // Row 3: Live eBay-Scraper & Scalper-Schutz
            UnifiedGlassShowcaseRow(
                overline = "EBAY SCRAPER • KAUFEMPFEHLUNG",
                title = "Scalper vs. Gebraucht-Deal",
                statusIcon = Icons.Default.Shield,
                statusText = "Gebraucht ab ${recommendation.ebayNormalUsedPrice.toInt()} € • Scalper meiden!",
                statusColor = ScalperAmber,
                rightLabel = "Scalper-Warnung",
                rightValue = "${recommendation.ebayLiveScalperPrice.toInt()} € (+${recommendation.scalperMarkupEuro.toInt()}€)",
                onClick = onClickScraper
            )

            HorizontalDivider(color = GlassDivider, thickness = 1.dp)

            // Bottom Tailored 5-Shop Live Radar Signal Bar (Upgraded from the screenshot's bar)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val radarNodes = listOf("MM", "SAT", "OTTO", "AMZ", "PS5", "EBAY", "15s")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    radarNodes.forEachIndexed { index, node ->
                        Text(
                            text = node,
                            style = MaterialTheme.typography.labelLarge,
                            color = when {
                                index <= 4 -> TextWhite
                                index == 5 -> ScalperAmber
                                else -> PsCyanGlow
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(ProgressTrackDark)
                ) {
                    val progressFraction = ((16 - secondsUntilNextAutoRefresh).coerceIn(3, 15)) / 15f
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction.coerceAtLeast(0.62f))
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(StockEmerald, PsCyanGlow, PsElectricBlue)
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun UnifiedGlassShowcaseRow(
    overline: String,
    title: String,
    statusIcon: ImageVector,
    statusText: String,
    statusColor: Color,
    rightLabel: String,
    rightValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left: Product Image (PS5 Pro PNG) with subtle neon rim
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color(0xFF101016))
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.22f), GlassBorder)
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ps5_console),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Center Column: Overline + Bold White Title + Colored Status
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = overline,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Vertical Divider Line
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(48.dp)
                .background(GlassDivider)
        )

        // Right Column: Muted Label + Glowing Cyan/Periwinkle Value
        Column(
            modifier = Modifier.width(106.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = rightLabel,
                style = MaterialTheme.typography.labelSmall,
                color = TextSilver,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = rightValue,
                style = TextStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PsCyanGlow, PsElectricBlue, PsPeriwinkleGlow)
                    ),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickGlassActionPill(
    title: String,
    subtitle: String,
    leadingStarColor: Color? = null,
    leadingIcon: ImageVector? = null,
    leadingIconTint: Color = TextWhite,
    rightCheckCount: Int = 0,
    rightCounterText: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        border = BorderStroke(
            width = 1.2.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.20f),
                    GlassBorder,
                    Color.White.copy(alpha = 0.05f)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MidnightCardElevated.copy(alpha = 0.85f),
                            MidnightCard
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (leadingStarColor != null) {
                    FourPointSparkleStar(size = 26.dp, color = leadingStarColor)
                } else if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = leadingIconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSilver,
                        fontSize = 13.sp
                    )
                }
            }

            if (rightCounterText != null) {
                Text(
                    text = rightCounterText,
                    style = MaterialTheme.typography.titleMedium,
                    color = PsCyanGlow,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            } else if (rightCheckCount > 0) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(rightCheckCount) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (rightCheckCount == 3) StockEmerald else PsCyanGlow.copy(alpha = 0.75f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Floating Pill Bottom Navigation Bar where EVERY SINGLE ONE of the 5 buttons
 * has its own real tab function and displays the glowing crimson-red active circle when selected!
 */
@Composable
private fun Ps5FloatingBottomBar(
    currentTab: AppTab,
    onSelectTab: (AppTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            color = Color(0xFF18181F),
            shape = RoundedCornerShape(36.dp),
            border = BorderStroke(
                width = 1.3.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.24f),
                        GlassBorder,
                        Color.White.copy(alpha = 0.06f)
                    )
                )
            ),
            shadowElevation = 18.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Button 1: ✦ Shops & Radar-Übersicht (MediaMarkt, Saturn, OTTO, Amazon, PlayStation.com)
                FloatingNavSlot(
                    selected = currentTab == AppTab.SHOPS,
                    onClick = { onSelectTab(AppTab.SHOPS) },
                    testTag = "nav_tab_shops",
                    isSparkleStar = true,
                    icon = Icons.Default.EmojiEvents,
                    contentDescription = "Shops & Radar-Übersicht"
                )

                // Button 2: 🏆 Live-Preisdiagramm (Heute / 7 Tage / 30 Tage)
                FloatingNavSlot(
                    selected = currentTab == AppTab.DIAGRAMM,
                    onClick = { onSelectTab(AppTab.DIAGRAMM) },
                    testTag = "nav_tab_diagramm",
                    icon = Icons.Default.EmojiEvents,
                    contentDescription = "Live-Preisdiagramm"
                )

                // Button 3: ? Kurz-Info & Kauf-Ratgeber (Dedicated Info Guide Screen)
                FloatingNavSlot(
                    selected = currentTab == AppTab.INFO_GUIDE,
                    onClick = { onSelectTab(AppTab.INFO_GUIDE) },
                    testTag = "nav_tab_info_center",
                    icon = Icons.Default.HelpOutline,
                    contentDescription = "Kurz-Info & Kauf-Ratgeber"
                )

                // Button 4: 👥 eBay-Scraper & Scalper-Radar
                FloatingNavSlot(
                    selected = currentTab == AppTab.SCALPER_SCRAPER,
                    onClick = { onSelectTab(AppTab.SCALPER_SCRAPER) },
                    testTag = "nav_tab_scraper",
                    icon = Icons.Default.Group,
                    contentDescription = "eBay-Scraper & Scalper"
                )

                // Button 5: ⚙ Handy-Alarm & 5-Märkte Einstellungen
                FloatingNavSlot(
                    selected = currentTab == AppTab.ALARM_SETTINGS,
                    onClick = { onSelectTab(AppTab.ALARM_SETTINGS) },
                    testTag = "nav_tab_settings_alarm",
                    icon = Icons.Default.Settings,
                    contentDescription = "Handy-Alarm & Einstellungen"
                )
            }
        }
    }
}

@Composable
private fun FloatingNavSlot(
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    icon: ImageVector,
    contentDescription: String,
    isSparkleStar: Boolean = false
) {
    if (selected) {
        // Glowing Crimson-Red Capsule Circle with soft pink/white specular rim
        Box(
            modifier = Modifier
                .size(height = 54.dp, width = 66.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(ActiveNavRedTop, ActiveNavRedBottom)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ActiveNavRedRim.copy(alpha = 0.95f),
                            ActiveNavRedTop.copy(alpha = 0.45f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .clickable(onClick = onClick)
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            if (isSparkleStar) {
                FourPointSparkleStar(size = 24.dp, color = TextWhite)
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick)
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            if (isSparkleStar) {
                FourPointSparkleStar(size = 22.dp, color = TextMuted)
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = TextMuted,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun FourPointSparkleStar(
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val innerOffset = w * 0.16f

        val starPath = Path().apply {
            moveTo(cx, 0f)
            quadraticTo(cx + innerOffset * 0.4f, cy - innerOffset * 0.4f, w, cy)
            quadraticTo(cx + innerOffset * 0.4f, cy + innerOffset * 0.4f, cx, h)
            quadraticTo(cx - innerOffset * 0.4f, cy + innerOffset * 0.4f, 0f, cy)
            quadraticTo(cx - innerOffset * 0.4f, cy - innerOffset * 0.4f, cx, 0f)
            close()
        }
        drawPath(path = starPath, color = color)
    }
}

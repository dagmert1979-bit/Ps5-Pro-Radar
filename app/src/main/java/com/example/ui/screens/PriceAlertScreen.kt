package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PriceAlertEntity
import com.example.data.ShopOfferEntity
import com.example.ui.Ps5NotificationHelper
import com.example.ui.components.ShopOfferCard
import com.example.ui.theme.ActiveNavRedTop
import com.example.ui.theme.CrimsonGlowTop
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCardElevated
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.PsCyanGlow
import com.example.ui.theme.StockEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite

@Composable
fun PriceAlertScreen(
    ebayOffers: List<ShopOfferEntity>,
    alert: PriceAlertEntity,
    secondsUntilNextAutoRefresh: Int,
    autoSyncCycleCount: Int,
    onOpenShopUrl: (String) -> Unit,
    onSaveAlert: (PriceAlertEntity) -> Unit,
    onShowMessage: (String) -> Unit,
    focusSettingsFirst: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isEnabled by remember(alert.isEnabled) { mutableStateOf(alert.isEnabled) }
    var alertOnAnyRestock by remember(alert.alertOnAnyRestock) { mutableStateOf(alert.alertOnAnyRestock) }
    var watchMediaMarkt by remember(alert.watchMediaMarkt) { mutableStateOf(alert.watchMediaMarkt) }
    var watchSaturn by remember(alert.watchSaturn) { mutableStateOf(alert.watchSaturn) }
    var watchOtto by remember(alert.watchOtto) { mutableStateOf(alert.watchOtto) }
    var watchAmazon by remember(alert.watchAmazon) { mutableStateOf(alert.watchAmazon) }
    var watchPsDirect by remember(alert.watchPsDirect) { mutableStateOf(alert.watchPsDirect) }
    var hasPermission by remember {
        mutableStateOf(Ps5NotificationHelper.hasNotificationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            Ps5NotificationHelper.sendStockNotification(
                context = context,
                title = "PS5 Pro Radar • Handy-Alarm aktiviert!",
                message = "Du erhältst ab sofort eine Benachrichtigung auf dein Handy, sobald die PS5 Pro bei MediaMarkt, Saturn, OTTO, Amazon oder PlayStation.com wieder verfügbar ist!"
            )
            onShowMessage("Handy-Benachrichtigung erfolgreich aktiviert und gesendet!")
        } else {
            onShowMessage("Bitte Benachrichtigungs-Berechtigung in den Android-Einstellungen erlauben.")
        }
    }

    val activeWatchCount = listOf(
        watchMediaMarkt,
        watchSaturn,
        watchOtto,
        watchAmazon,
        watchPsDirect
    ).count { it }

    fun triggerPhoneNotificationTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !Ps5NotificationHelper.hasNotificationPermission(context)
        ) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val sent = Ps5NotificationHelper.sendStockNotification(
                context = context,
                title = "PS5 Pro Radar • Live-Überwachung aktiv",
                message = "Dein Handy benachrichtigt dich sofort automatisch, sobald MediaMarkt, Saturn, OTTO, Amazon oder PlayStation.com wieder PS5 Pro Lagerbestand (799,99 €) hat!"
            )
            if (sent) {
                onShowMessage("Handy-Benachrichtigung oben in der Statusleiste gesendet!")
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("scalper_scraper_lazy_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (!focusSettingsFirst) {
            item {
                Text(
                    text = "Live eBay-Scraper & Preisvergleich",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }

            items(
                items = ebayOffers,
                key = { it.id }
            ) { ebayOffer ->
                ShopOfferCard(
                    offer = ebayOffer,
                    onOpenShopUrl = onOpenShopUrl
                )
            }
        }

        // Section Header for Handy-Alarm & Einstellungen
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (focusSettingsFirst) "Handy-Alarm & Radar-Einstellungen" else "Schnell-Check & Push-Alarm",
                style = MaterialTheme.typography.headlineSmall,
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Pill Card 1: Auto-Scraper Status with glowing cyan star & emerald checkmarks
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(
                    width = 1.3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            GlassBorder
                        )
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(MidnightCardElevated, MidnightCard)
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "✦",
                            color = PsCyanGlow,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "15s Hintergrund-Radar aktiv",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Check #$autoSyncCycleCount • Nächstes Update in ${secondsUntilNextAutoRefresh}s",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(3) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = StockEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Pill Card 2: Test / Trigger Phone Notification
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .clickable { triggerPhoneNotificationTest() }
                    .testTag("test_phone_notification_button"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(
                    width = 1.3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF6B00).copy(alpha = 0.55f),
                            GlassBorder
                        )
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(MidnightCardElevated, MidnightCard)
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFFFF6B00),
                            modifier = Modifier.size(30.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (hasPermission) "Push-Alarm jetzt aufs Handy senden" else "Handy-Alarm Berechtigung aktivieren",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Antippen → Testet sofort den Verfügbarkeits-Ton",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSilver,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = if (hasPermission) StockEmerald else TextMuted.copy(alpha = 0.6f),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // Pill Card 3: Shop Watch Settings & Save
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(
                    width = 1.3.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            StockEmerald.copy(alpha = 0.45f),
                            GlassBorder
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
                                    CrimsonGlowTop.copy(alpha = 0.25f),
                                    MidnightCardElevated,
                                    MidnightCard
                                )
                            )
                        )
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = StockEmerald,
                                modifier = Modifier.size(28.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "Überwachte Märkte",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextWhite,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Automatischer Alarm bei 799,99 € UVP",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSilver,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "$activeWatchCount/5",
                                style = MaterialTheme.typography.titleMedium,
                                color = PsCyanGlow,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { isEnabled = it },
                                modifier = Modifier.testTag("price_alert_enable_switch"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ObsidianBg,
                                    checkedTrackColor = StockEmerald
                                )
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        ShopWatchRow("MediaMarkt (UVP 799,99 € Verfügbarkeits-Check)", watchMediaMarkt) { watchMediaMarkt = it }
                        ShopWatchRow("Saturn (UVP 799,99 € Verfügbarkeits-Check)", watchSaturn) { watchSaturn = it }
                        ShopWatchRow("OTTO (UVP 799,99 € Verfügbarkeits-Check)", watchOtto) { watchOtto = it }
                        ShopWatchRow("Amazon.de (UVP 799,99 € Verfügbarkeits-Check)", watchAmazon) { watchAmazon = it }
                        ShopWatchRow("PlayStation.com (Offizieller Direktverkauf)", watchPsDirect) { watchPsDirect = it }
                        ShopWatchRow("eBay Scalper-Preise (> 1.000 €) als Warnung markieren", alertOnAnyRestock) { alertOnAnyRestock = it }
                    }

                    Button(
                        onClick = {
                            onSaveAlert(
                                alert.copy(
                                    targetPrice = 799.99,
                                    isEnabled = isEnabled,
                                    alertOnAnyRestock = alertOnAnyRestock,
                                    watchMediaMarkt = watchMediaMarkt,
                                    watchSaturn = watchSaturn,
                                    watchOtto = watchOtto,
                                    watchAmazon = watchAmazon,
                                    watchPsDirect = watchPsDirect,
                                    lastTriggeredBanner = "Handy-Benachrichtigungen & Scraper aktiv."
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_price_alert_button"),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActiveNavRedTop,
                            contentColor = TextWhite
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Einstellungen speichern",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (focusSettingsFirst) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Live eBay-Scraper Status",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextWhite,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            items(
                items = ebayOffers,
                key = { it.id }
            ) { ebayOffer ->
                ShopOfferCard(
                    offer = ebayOffer,
                    onOpenShopUrl = onOpenShopUrl
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShopWatchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = StockEmerald,
                checkmarkColor = ObsidianBg,
                uncheckedColor = TextMuted
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextWhite,
            fontWeight = FontWeight.Medium
        )
    }
}

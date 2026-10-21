package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.MarketRecommendation
import com.example.ui.theme.ActiveNavRedBottom
import com.example.ui.theme.ActiveNavRedTop
import com.example.ui.theme.CrimsonGlowTop
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassDivider
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCardElevated
import com.example.ui.theme.PsCyanGlow
import com.example.ui.theme.PsElectricBlue
import com.example.ui.theme.PsPeriwinkleGlow
import com.example.ui.theme.ScalperAmber
import com.example.ui.theme.ScalperCrimson
import com.example.ui.theme.StockEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import java.util.Locale

/**
 * Full Screen for the bottom "?" Navigation Button (AppTab.INFO_GUIDE):
 * Gives a clear, high-craft overview of the PS5 Pro Radar, current buy/wait recommendation,
 * Scalper vs. UVP rules, and how the automatic 15s stock check works.
 */
@Composable
fun InfoGuideScreen(
    recommendation: MarketRecommendation,
    secondsUntilNextAutoRefresh: Int,
    autoSyncCycleCount: Int,
    onNavigateToShops: () -> Unit,
    onNavigateToDiagramm: () -> Unit,
    onTriggerRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("info_guide_lazy_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Kurz-Info Glass Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(
                    width = 1.4.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.28f),
                            ActiveNavRedTop.copy(alpha = 0.55f),
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
                                    CrimsonGlowTop.copy(alpha = 0.55f),
                                    MidnightCardElevated,
                                    MidnightCard
                                )
                            )
                        )
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF101016))
                                .border(1.dp, ActiveNavRedTop, RoundedCornerShape(16.dp))
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ps5_console),
                                contentDescription = "PS5 Pro",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "KURZ-INFO & KAUF-RATGEBER",
                                style = MaterialTheme.typography.labelSmall,
                                color = PsCyanGlow,
                                fontSize = 10.sp,
                                letterSpacing = 0.9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Wie funktioniert PS5 Pro Radar?",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextWhite,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Live-Überwachung #${autoSyncCycleCount} • Nächster Check in ${secondsUntilNextAutoRefresh}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSilver
                            )
                        }
                    }

                    HorizontalDivider(color = GlassDivider, thickness = 1.dp)

                    Text(
                        text = recommendation.headline,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (recommendation.shouldBuyNow) StockEmerald else ScalperAmber,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = recommendation.detailedAdvice,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSilver
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onNavigateToShops,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ActiveNavRedTop,
                                contentColor = TextWhite
                            )
                        ) {
                            Text("Zu den 5 Shops", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onNavigateToDiagramm,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MidnightCardElevated,
                                contentColor = PsCyanGlow
                            )
                        ) {
                            Text("Live-Diagramm", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Info Card 1: Preis-Ampel Erklärung
        item {
            InfoFeatureGlassCard(
                icon = Icons.Default.Shield,
                iconTint = StockEmerald,
                overline = "1. OFFIZIELLER UVP-PREIS (799,99 €)",
                title = "Wann lohnt sich der Kauf sofort?",
                description = "Sobald MediaMarkt, Saturn, OTTO, Amazon.de oder PlayStation.com neue Lagerware für 799,99 € freischalten, schaltet die App automatisch auf GRÜN ('Jetzt kaufen!') und sendet dir auf Wunsch eine Handy-Benachrichtigung.",
                rightMetricLabel = "Zielpreis",
                rightMetricValue = "799,99 €"
            )
        }

        // Info Card 2: Scalper-Warnung Erklärung
        item {
            InfoFeatureGlassCard(
                icon = Icons.Default.WarningAmber,
                iconTint = ScalperCrimson,
                overline = "2. SCALPER-SCHUTZ (> 1.000 €)",
                title = "Warum vor Scalpern gewarnt wird",
                description = "Wenn die offiziellen Märkte ausverkauft sind, kaufen Weiterverkäufer (Scalper) Bestände auf und bieten sie auf eBay für über 1.000 € an. Die App warnt dich automatisch vor diesen Wucherpreisen (+${String.format(Locale.GERMANY, "%.0f €", recommendation.scalperMarkupEuro)} Aufpreis).",
                rightMetricLabel = "Scalper aktuell",
                rightMetricValue = "${recommendation.ebayLiveScalperPrice.toInt()} €"
            )
        }

        // Info Card 3: Gebraucht-Alternative & Auto-Refresh
        item {
            InfoFeatureGlassCard(
                icon = Icons.Default.TrendingDown,
                iconTint = ScalperAmber,
                overline = "3. FAIRE ALTERNATIVE & AUTO-UPDATE",
                title = "Gebraucht-Deals & 15s Live-Radar",
                description = "Parallel prüft unser eBay-Scraper auch normale gebrauchte PS5 Pro Konsolen (aktuell ab ca. ${String.format(Locale.GERMANY, "%.2f €", recommendation.ebayNormalUsedPrice)}). Alle Preise und das Uhrzeit-Diagramm bleiben auch nach einem App-Neustart gespeichert.",
                rightMetricLabel = "Gebraucht ab",
                rightMetricValue = "${recommendation.ebayNormalUsedPrice.toInt()} €"
            )
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun InfoFeatureGlassCard(
    icon: ImageVector,
    iconTint: Color,
    overline: String,
    title: String,
    description: String,
    rightMetricLabel: String,
    rightMetricValue: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        border = BorderStroke(1.2.dp, GlassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(MidnightCardElevated, MidnightCard)
                    )
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.16f))
                        .border(1.dp, iconTint.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = overline,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(38.dp)
                        .background(GlassDivider)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = rightMetricLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = rightMetricValue,
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PsCyanGlow, PsElectricBlue, PsPeriwinkleGlow)
                            ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSilver
            )
        }
    }
}

/**
 * Sleek Dark-Glass Popup Overlay Dialog triggered by the top-right "?" button
 * so the user gets an instant Kurz-Info & Kaufempfehlung overlay from anywhere in the app!
 */
@Composable
fun QuickInfoGlassDialog(
    recommendation: MarketRecommendation,
    secondsUntilNextAutoRefresh: Int,
    onDismiss: () -> Unit,
    onOpenFullGuide: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quick_info_glass_dialog"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightCard),
            border = BorderStroke(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.35f),
                        ActiveNavRedTop,
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
                                CrimsonGlowTop.copy(alpha = 0.65f),
                                MidnightCardElevated,
                                MidnightCard
                            )
                        )
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(ActiveNavRedTop, ActiveNavRedBottom)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = TextWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PS5 Pro Radar • Kurz-Info",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Auto-Update alle 15s (nächster Check: ${secondsUntilNextAutoRefresh}s)",
                                style = MaterialTheme.typography.labelSmall,
                                color = PsCyanGlow
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Schließen",
                            tint = TextSilver
                        )
                    }
                }

                HorizontalDivider(color = GlassDivider, thickness = 1.dp)

                // Short bullet points inside the glass popup
                QuickInfoBulletRow(
                    badgeColor = StockEmerald,
                    title = "Offizielle Märkte (UVP 799,99 €):",
                    text = "MediaMarkt, Saturn, OTTO, Amazon & PlayStation.com werden vollautomatisch auf Lagerbestand geprüft."
                )

                QuickInfoBulletRow(
                    badgeColor = ScalperCrimson,
                    title = "Scalper-Schutz (aktuell ${recommendation.ebayLiveScalperPrice.toInt()} €):",
                    text = "Überteuerte eBay-Scalper (> 1.000 €) werden automatisch erkannt und als 'Nicht empfohlen' markiert."
                )

                QuickInfoBulletRow(
                    badgeColor = ScalperAmber,
                    title = "Aktuelle Empfehlung:",
                    text = if (recommendation.shouldBuyNow) {
                        "Jetzt zum UVP (799,99 €) bei den offiziellen Shops zuschlagen!"
                    } else {
                        "Bei Scalpern NICHT kaufen! Auf offizielle Lagerware (799,99 €) warten oder Gebraucht-Deal (~${recommendation.ebayNormalUsedPrice.toInt()} €) prüfen."
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = {
                            onDismiss()
                            onOpenFullGuide()
                        },
                        modifier = Modifier.weight(1f),
                        color = MidnightCardElevated,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GlassBorder)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Ganzer Ratgeber",
                                style = MaterialTheme.typography.labelLarge,
                                color = PsCyanGlow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ActiveNavRedTop,
                            contentColor = TextWhite
                        )
                    ) {
                        Text(
                            text = "Verstanden",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickInfoBulletRow(
    badgeColor: Color,
    title: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(9.dp)
                .clip(CircleShape)
                .background(badgeColor)
        )
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = TextSilver
            )
        }
    }
}

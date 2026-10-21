package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AvailabilityStatus
import com.example.data.ShopOfferEntity
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassDivider
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCardElevated
import com.example.ui.theme.PsCyanGlow
import com.example.ui.theme.PsElectricBlue
import com.example.ui.theme.PsIndigoAccent
import com.example.ui.theme.PsPeriwinkleGlow
import com.example.ui.theme.ScalperAmber
import com.example.ui.theme.ScalperCrimson
import com.example.ui.theme.StockEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShopOfferCard(
    offer: ShopOfferEntity,
    onOpenShopUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEbayScalper = offer.id == "ebay_scalper"
    val isEbayUsed = offer.id == "ebay_used"
    val isAvailableOfficial = offer.isOfficialRetailer && offer.parsedStatus == AvailabilityStatus.IN_STOCK_MSRP

    val accentIconColor = when (offer.id) {
        "mediamarkt" -> Color(0xFFFF6B00) // Vibrant orange like the play triangle in screenshot
        "saturn" -> Color(0xFF38BDF8)     // Cyan sparkle
        "otto" -> StockEmerald            // Emerald green
        "amazon" -> ScalperAmber          // Warm gold
        "psdirect" -> PsIndigoAccent      // Periwinkle blue
        "ebay_scalper" -> ScalperCrimson
        "ebay_used" -> StockEmerald
        else -> PsElectricBlue
    }

    val statusColor = when {
        isAvailableOfficial -> StockEmerald
        isEbayUsed -> ScalperAmber
        isEbayScalper -> ScalperCrimson
        else -> ScalperCrimson
    }

    val priceDiff = (offer.currentPrice - offer.msrpPrice).coerceAtLeast(0.0)
    val timeFormatter = SimpleDateFormat("HH:mm 'Uhr'", Locale.GERMANY)
    val checkedTimeText = timeFormatter.format(Date(offer.lastCheckedTimestamp))

    // Sleek Dark-Glass Pill Card matching "Sammle Tickets" / Grouped Glass cards in screenshot
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable { onOpenShopUrl(offer.productUrl) }
            .testTag("shop_card_${offer.id}"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        border = BorderStroke(
            width = 1.2.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.16f),
                    GlassBorder.copy(alpha = 0.7f),
                    Color.White.copy(alpha = 0.05f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MidnightCardElevated.copy(alpha = 0.75f),
                            MidnightCard,
                            MidnightCardElevated.copy(alpha = 0.55f)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Icon + PS5 Pro Thumbnail Box
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF101015))
                        .border(1.dp, accentIconColor.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                        .padding(5.dp)
                        .testTag("shop_ps5_mini_box_${offer.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ps5_console),
                        contentDescription = "PS5 Pro Foto bei ${offer.shopName}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                // Center Info: Uppercase Tag + Bold White Title + Subtitle with Arrow
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = when {
                            isEbayScalper -> "EBAY SCALPER • WARNUNG"
                            isEbayUsed -> "EBAY GEBRAUCHT • NORMALPREIS"
                            else -> "OFFIZIELLER SHOP • LIVE $checkedTimeText"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = offer.shopName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                isAvailableOfficial -> Icons.Default.Check
                                isEbayScalper -> Icons.Default.WarningAmber
                                else -> Icons.Default.PlayArrow
                            },
                            contentDescription = null,
                            tint = if (isAvailableOfficial) StockEmerald else accentIconColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = when {
                                isAvailableOfficial -> "Verfügbar → Jetzt zum UVP sichern"
                                isEbayScalper -> "Nicht kaufen → +${String.format(Locale.GERMANY, "%.0f €", priceDiff)} über UVP"
                                isEbayUsed -> "Gebraucht-Deal → Unter UVP verfügbar"
                                else -> "Nicht verfügbar → Direktlink prüfen"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isAvailableOfficial) StockEmerald else TextSilver,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Vertical Subtle Divider (matching screenshot's right-column split)
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(44.dp)
                        .background(GlassDivider)
                )

                // Right Column: Status / Glowing Blue-Violet Metric + Checkmarks
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = when {
                            isEbayScalper -> "Scalper"
                            isEbayUsed -> "Ab Preis"
                            isAvailableOfficial -> "Auf Lager"
                            else -> "Nicht verfügbar"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = String.format(Locale.GERMANY, "%.2f €", offer.currentPrice),
                        style = TextStyle(
                            brush = Brush.horizontalGradient(
                                colors = if (isEbayScalper) {
                                    listOf(ScalperCrimson, Color(0xFFFF8FA3))
                                } else {
                                    listOf(PsCyanGlow, PsElectricBlue, PsPeriwinkleGlow)
                                }
                            ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )

                    // Checkmark row like the screenshot's ✓ ✓ ✓
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { idx ->
                            val checkColor = when {
                                isAvailableOfficial -> StockEmerald
                                isEbayUsed && idx < 2 -> StockEmerald
                                !isEbayScalper && idx == 0 -> StockEmerald.copy(alpha = 0.7f)
                                else -> TextMuted.copy(alpha = 0.45f)
                            }
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = checkColor,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }

            // Bottom subtle glass bar with status note & direct link button
            Surface(
                color = Color(0xFF121217),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GlassDivider)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Text(
                            text = offer.statusBadgeText,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenShopUrl(offer.productUrl) }
                            .testTag("open_shop_link_${offer.id}"),
                        color = MidnightCardElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, accentIconColor.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = when {
                                    isAvailableOfficial -> "Jetzt kaufen"
                                    isEbayUsed -> "eBay Deal"
                                    isEbayScalper -> "eBay Link"
                                    else -> "Zum Shop"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                contentDescription = "Shop öffnen",
                                tint = PsElectricBlue,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

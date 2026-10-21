package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PriceHistoryEntity
import com.example.ui.ChartRangeFilter
import com.example.ui.MarketRecommendation
import com.example.ui.theme.ActiveNavRedTop
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassDivider
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCardElevated
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.ProgressTrackDark
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
import java.util.Locale

@Composable
fun HeroPs5PriceChartCard(
    history: List<PriceHistoryEntity>,
    selectedRange: ChartRangeFilter,
    selectedPointIndex: Int,
    recommendation: MarketRecommendation,
    isRefreshingLive: Boolean,
    secondsUntilNextAutoRefresh: Int,
    onSelectRange: (ChartRangeFilter) -> Unit,
    onSelectPoint: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activePoint = history.getOrNull(selectedPointIndex) ?: history.lastOrNull()
    val firstPointLabel = history.firstOrNull()?.dateLabel ?: ""
    val midPointLabel = history.getOrNull(history.size / 2)?.dateLabel ?: ""
    val lastPointLabel = history.lastOrNull()?.dateLabel ?: "Jetzt"

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Unified Frosted Dark-Glass Summary + Live Price Chart Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hero_ps5_chart_card"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightCard),
            border = BorderStroke(
                width = 1.2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.22f),
                        GlassBorder,
                        Color.White.copy(alpha = 0.05f)
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
                                MidnightCardElevated.copy(alpha = 0.85f),
                                MidnightCard,
                                Color(0xFF121217)
                            )
                        )
                    )
            ) {
                // Top Row inside Unified Glass Card (matching the screenshot's divided row design)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF101015))
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
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

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "LIVE PREISDIAGRAMM • UHRZEIT-TRACKER",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "PlayStation 5 Pro Kurs",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = if (recommendation.shouldBuyNow) Icons.Default.Check else Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = if (recommendation.shouldBuyNow) StockEmerald else ScalperAmber,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (recommendation.shouldBuyNow) {
                                    "Jetzt zum UVP (799,99 €) kaufen"
                                } else {
                                    "Scalper hoch • Auf UVP warten"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (recommendation.shouldBuyNow) StockEmerald else ScalperAmber,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(48.dp)
                            .background(GlassDivider)
                    )

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Nächster Check",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = if (isRefreshingLive) "Lädt..." else "00:${String.format(Locale.GERMANY, "%02d", secondsUntilNextAutoRefresh)}s",
                            style = TextStyle(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(PsCyanGlow, PsElectricBlue, PsPeriwinkleGlow)
                                ),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }

                HorizontalDivider(color = GlassDivider, thickness = 1.dp)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Filter Pills (Heute / 7 Tage / 30 Tage)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ChartRangeFilter.entries.forEach { filter ->
                            val selected = filter == selectedRange
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onSelectRange(filter) }
                                    .testTag("range_filter_${filter.name.lowercase()}"),
                                color = if (selected) ActiveNavRedTop.copy(alpha = 0.25f) else Color(0xFF111116),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    1.2.dp,
                                    if (selected) ActiveNavRedTop else GlassBorder
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 9.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = filter.label,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (selected) TextWhite else TextSilver,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Legend Strip
                    Surface(
                        color = Color(0xFF111116),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, GlassDivider)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendDot(color = ScalperCrimson, text = "Scalper (>1.000€)")
                            LegendDot(color = ScalperAmber, text = "Gebraucht (~748€)")
                            LegendDot(color = StockEmerald, text = "UVP (799,99€)")
                        }
                    }

                    // SIDE-BY-SIDE: PS5 Photo on the Left + Real Time-Based Chart on the Right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(235.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left Box: User's PlayStation 5 PNG image
                        Box(
                            modifier = Modifier
                                .width(108.dp)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF101015))
                                .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                                .padding(8.dp)
                                .testTag("ps5_hero_photo_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Surface(
                                    color = if (recommendation.shouldBuyNow) StockEmerald.copy(alpha = 0.18f) else ScalperCrimson.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (recommendation.shouldBuyNow) StockEmerald else ScalperCrimson
                                    )
                                ) {
                                    Text(
                                        text = if (recommendation.shouldBuyNow) "AUF LAGER" else "SCALPER",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        color = if (recommendation.shouldBuyNow) StockEmerald else ScalperCrimson,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Image(
                                    painter = painterResource(id = R.drawable.ps5_console),
                                    contentDescription = stringResource(id = R.string.ps5_image_desc),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    contentScale = ContentScale.Fit
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "PS5 Pro",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "799,99 €",
                                        style = TextStyle(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(PsCyanGlow, PsPeriwinkleGlow)
                                            ),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                }
                            }
                        }

                        // Right Box: Interactive Time-Based Price Chart
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(18.dp))
                                .background(ObsidianBg)
                                .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                                .padding(10.dp)
                                .testTag("ps5_price_chart_canvas")
                        ) {
                            if (history.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Lade Preisverlauf ...", color = TextWhite)
                                }
                            } else {
                                val minPrice = 680.0
                                val maxPrice = 1160.0
                                val safeIndex = selectedPointIndex.coerceIn(0, (history.size - 1).coerceAtLeast(0))

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (activePoint != null) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Schedule,
                                                    contentDescription = null,
                                                    tint = PsCyanGlow,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Text(
                                                    text = activePoint.dateLabel,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = TextSilver,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                text = formatPrice(activePoint.scalperPeakPrice),
                                                style = TextStyle(
                                                    brush = Brush.horizontalGradient(
                                                        colors = listOf(PsCyanGlow, PsIndigoAccent)
                                                    ),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(end = 6.dp),
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            horizontalAlignment = Alignment.End
                                        ) {
                                            Text("1.100€", color = ScalperCrimson, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            Text("950€", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            Text("800€", color = StockEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            Text("700€", color = ScalperAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Canvas(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .pointerInput(history) {
                                                    detectTapGestures { tapOffset ->
                                                        if (history.size > 1 && size.width > 0) {
                                                            val stepX = size.width.toFloat() / (history.size - 1).toFloat()
                                                            val tappedIdx = ((tapOffset.x / stepX) + 0.5f).toInt()
                                                                .coerceIn(0, history.lastIndex)
                                                            onSelectPoint(tappedIdx)
                                                        }
                                                    }
                                                }
                                        ) {
                                            val w = size.width
                                            val h = size.height

                                            fun priceToY(price: Double): Float {
                                                val normalized = ((price - minPrice) / (maxPrice - minPrice)).coerceIn(0.0, 1.0)
                                                return (h - (normalized * (h - 14f)) - 7f).toFloat()
                                            }

                                            listOf(1100.0, 950.0, 799.99, 700.0).forEach { gp ->
                                                val y = priceToY(gp)
                                                val isUvp = gp in 799.0..801.0
                                                drawLine(
                                                    color = if (isUvp) StockEmerald else GlassDivider,
                                                    start = Offset(0f, y),
                                                    end = Offset(w, y),
                                                    strokeWidth = if (isUvp) 3f else 1.2f,
                                                    pathEffect = if (isUvp) PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f) else null
                                                )
                                            }

                                            val count = history.size
                                            val stepX = if (count > 1) w / (count - 1).toFloat() else w

                                            val scalperPath = Path()
                                            val scalperFill = Path()
                                            history.forEachIndexed { idx, item ->
                                                val x = idx * stepX
                                                val y = priceToY(item.scalperPeakPrice)
                                                if (idx == 0) {
                                                    scalperPath.moveTo(x, y)
                                                    scalperFill.moveTo(x, h)
                                                    scalperFill.lineTo(x, y)
                                                } else {
                                                    scalperPath.lineTo(x, y)
                                                    scalperFill.lineTo(x, y)
                                                }
                                                if (idx == history.lastIndex) {
                                                    scalperFill.lineTo(x, h)
                                                    scalperFill.close()
                                                }
                                            }

                                            drawPath(
                                                path = scalperFill,
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        ScalperCrimson.copy(alpha = 0.32f),
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                            drawPath(
                                                path = scalperPath,
                                                color = ScalperCrimson,
                                                style = Stroke(width = 4.5f, cap = StrokeCap.Round)
                                            )

                                            val usedPath = Path()
                                            history.forEachIndexed { idx, item ->
                                                val x = idx * stepX
                                                val y = priceToY(item.lowestAvailablePrice)
                                                if (idx == 0) usedPath.moveTo(x, y) else usedPath.lineTo(x, y)
                                            }
                                            drawPath(
                                                path = usedPath,
                                                color = ScalperAmber,
                                                style = Stroke(width = 4f, cap = StrokeCap.Round)
                                            )

                                            history.forEachIndexed { idx, item ->
                                                val x = idx * stepX
                                                val yScalper = priceToY(item.scalperPeakPrice)
                                                val yUsed = priceToY(item.lowestAvailablePrice)
                                                val isSelected = idx == safeIndex

                                                if (isSelected) {
                                                    drawLine(
                                                        color = PsCyanGlow,
                                                        start = Offset(x, 0f),
                                                        end = Offset(x, h),
                                                        strokeWidth = 2f
                                                    )
                                                    drawCircle(
                                                        color = TextWhite,
                                                        radius = 7f,
                                                        center = Offset(x, yScalper)
                                                    )
                                                    drawCircle(
                                                        color = TextWhite,
                                                        radius = 7f,
                                                        center = Offset(x, yUsed)
                                                    )
                                                }
                                                drawCircle(
                                                    color = ScalperCrimson,
                                                    radius = if (isSelected) 4.5f else 3f,
                                                    center = Offset(x, yScalper)
                                                )
                                                drawCircle(
                                                    color = ScalperAmber,
                                                    radius = if (isSelected) 4.5f else 3f,
                                                    center = Offset(x, yUsed)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 34.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(firstPointLabel, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(midPointLabel, color = TextSilver, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text(lastPointLabel, color = PsCyanGlow, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (activePoint != null) {
                        Text(
                            text = activePoint.eventNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                HorizontalDivider(color = GlassDivider, thickness = 1.dp)

                // Bottom 5-Shop Live Radar Signal Strip (Tailored upgrade over the reference bar)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val shopSignals = listOf("MM", "SAT", "OTTO", "AMZ", "PSD", "EBAY", "LIVE")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        shopSignals.forEachIndexed { index, code ->
                            Text(
                                text = code,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (index <= 4) TextWhite else PsCyanGlow,
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.74f)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(StockEmerald, PsCyanGlow, PsIndigoAccent)
                                    )
                                )
                        )
                    }
                }
            }
        }

        // Recommendation Dark Glass Pill Card (matching screenshot's bottom pill cards)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("scalper_recommendation_banner"),
            color = MidnightCard,
            shape = RoundedCornerShape(26.dp),
            border = BorderStroke(
                1.2.dp,
                if (recommendation.shouldBuyNow) StockEmerald.copy(alpha = 0.6f) else GlassBorder
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = if (recommendation.shouldBuyNow) Icons.Default.Check else Icons.Default.EmojiEvents,
                        contentDescription = "Kaufempfehlung",
                        tint = if (recommendation.shouldBuyNow) StockEmerald else ScalperAmber,
                        modifier = Modifier.size(26.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = recommendation.headline,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = recommendation.subheadline,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (recommendation.shouldBuyNow) StockEmerald else ScalperAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Text(
                    text = recommendation.detailedAdvice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSilver
                )
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = TextSilver,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatPrice(price: Double): String {
    return String.format(Locale.GERMANY, "%.2f €", price)
}

package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Ps5DarkColorScheme = darkColorScheme(
    primary = PsElectricBlue,
    onPrimary = ObsidianBg,
    primaryContainer = PsCobaltPrimary,
    onPrimaryContainer = TextWhite,
    secondary = StockEmerald,
    onSecondary = ObsidianBg,
    secondaryContainer = StockEmeraldBg,
    onSecondaryContainer = StockEmerald,
    tertiary = ScalperAmber,
    error = ScalperCrimson,
    errorContainer = ScalperCrimsonBg,
    onErrorContainer = ScalperCrimson,
    background = ObsidianBg,
    onBackground = TextWhite,
    surface = MidnightSurface,
    onSurface = TextWhite,
    surfaceVariant = MidnightCard,
    onSurfaceVariant = TextSilver,
    outline = BorderSubtle
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = Ps5DarkColorScheme,
        typography = Typography,
        content = content
    )
}

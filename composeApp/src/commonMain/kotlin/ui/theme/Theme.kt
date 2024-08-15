package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColors = Colors(
    primary = Pink50,
    primaryVariant = Color.White,
    secondary = Blue60,
    secondaryVariant = Color.White,
    background = Gray90,
    onBackground = Gray10,
    surface = Color.White,
    onSurface = Gray90,
    error = Pink60,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onError = Color.White,
    isLight = true,
)

// TODO: Add dark color scheme
private val DarkColors = LightColors.copy(isLight = false)

@Composable
fun WouldYouRatherGameTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = when {
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colors = colors,
        typography = WouldYouRatherGameTypography(),
        content = content,
    )
}

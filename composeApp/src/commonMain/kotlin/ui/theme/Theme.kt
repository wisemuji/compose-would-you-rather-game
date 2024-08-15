package ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WouldYouRatherGameTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
//        colors = MaterialTheme.colors,
        typography = WouldYouRatherGameTypography(),
        content = content,
    )
}

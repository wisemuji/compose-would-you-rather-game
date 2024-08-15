package ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.dohyeon_regular
import org.jetbrains.compose.resources.Font

@Composable
fun DoHyeonFontFamily() = FontFamily(
    Font(Res.font.dohyeon_regular, weight = FontWeight.Normal),
)

@Composable
fun WouldYouRatherGameTypography() = Typography().run {
    val fontFamily = DoHyeonFontFamily()
    copy(
        h1 = h1.copy(fontFamily = fontFamily),
        h2 = h2.copy(fontFamily = fontFamily),
        h3 = h3.copy(fontFamily = fontFamily),
        h4 = h4.copy(fontFamily = fontFamily),
        h5 = h5.copy(
            fontFamily = fontFamily,
            letterSpacing = -(0.4).sp,
        ),
        h6 = h6.copy(
            fontFamily = fontFamily,
            letterSpacing = -(0.4).sp,
        ),
        subtitle1 = subtitle1.copy(fontFamily = fontFamily),
        subtitle2 = subtitle2.copy(fontFamily = fontFamily),
        body1 = body1.copy(
            fontFamily = fontFamily,
            letterSpacing = -(0.5).sp,
        ),
        body2 = body2.copy(fontFamily = fontFamily),
        button = button.copy(fontFamily = fontFamily),
        caption = caption.copy(fontFamily = fontFamily),
    )
}

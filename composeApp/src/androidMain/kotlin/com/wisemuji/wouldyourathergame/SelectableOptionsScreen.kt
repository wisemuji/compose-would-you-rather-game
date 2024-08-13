//package com.wisemuji.wouldyourathergame
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.ExperimentalSharedTransitionApi
//import androidx.compose.animation.SharedTransitionLayout
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import ui.game.GameUiState
//import ui.game.GameScreen
//
////<palette>
////<color name="Rose" hex="f72585" r="247" g="37" b="133" />
////<color name="Grape" hex="7209b7" r="114" g="9" b="183" />
////<color name="Zaffre" hex="3a0ca3" r="58" g="12" b="163" />
////<color name="Neon blue" hex="4361ee" r="67" g="97" b="238" />
////<color name="Vivid sky blue" hex="4cc9f0" r="76" g="201" b="240" />
////</palette>
//
//
//@OptIn(ExperimentalSharedTransitionApi::class)
//@Preview(showBackground = true)
//@Composable
//fun SelectableOptionsScreenPreview(modifier: Modifier = Modifier) {
//    SharedTransitionLayout {
//        AnimatedVisibility(visible = true) {
//            GameScreen(
//                uiState = GameUiState.SelectableOptions(
//                    question = "Okay, developers, here's a tough choice! Which one would you pick?",
//                    optionA = "\uD83D\uDC7B\n\nMake 1 billion won with an app I created",
//                    optionB = "Get praised by developers all over the world for my open source code\n\n\uD83D\uDC4E",
//                    remainingTurns = 3,
//                ),
//                onOptionSelected = {},
//                sharedTransitionScope = this@SharedTransitionLayout,
//                animatedVisibilityScope = this@AnimatedVisibility,
//                modifier = modifier
//                    .fillMaxSize(),
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalSharedTransitionApi::class)
//@Preview(showBackground = true)
//@Composable
//fun SelectableOptionsScreenPreview_Korean(modifier: Modifier = Modifier) {
//    SharedTransitionLayout {
//        AnimatedVisibility(visible = true) {
//            GameScreen(
//                uiState = GameUiState.SelectableOptions(
//                    question = "자, 개발자라면 누구나 고민될 만한 선택지! 어떤 걸 선택할래?",
//                    optionA = "내가 만든 앱으로 10억 벌기",
//                    optionB = "내가 만든 오픈소스로 전 세계 개발자들에게 칭찬받기",
//                    remainingTurns = 3,
//                ),
//                onOptionSelected = {},
//                sharedTransitionScope = this@SharedTransitionLayout,
//                animatedVisibilityScope = this@AnimatedVisibility,
//                modifier = modifier
//                    .fillMaxSize(),
//            )
//        }
//    }
//}

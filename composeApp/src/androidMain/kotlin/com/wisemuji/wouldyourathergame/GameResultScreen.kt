//package com.wisemuji.wouldyourathergame
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.ExperimentalSharedTransitionApi
//import androidx.compose.animation.SharedTransitionLayout
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import model.Option
//import ui.game.GameUiState
//import ui.gameresult.GameResultScreen
//
//
//@OptIn(ExperimentalSharedTransitionApi::class)
//@Preview(showBackground = true)
//@Composable
//private fun GameResultScreenPreview() {
//    SharedTransitionLayout {
//        AnimatedVisibility(visible = true) {
//            GameResultScreen(
//                uiState = GameUiState.GameResult(
//                    selectedOption = GameUiState.GameResult.SelectedOption(
//                        option = Option.A,
//                        comment = "\uD83E\uDD11\n\nMake \uD83D\uDC861 billion won with an app I created, work 20 hours a day to maintain it, and deal with angry users' comments every day",
//                    ),
//                    lesson = "You chose to make 1 billion won! \uD83D\uDD25 Dealing with angry users and working 20 hours a day... you're truly dedicated! \uD83D\uDC4F\nThis balance game made us realize! ✨\nThere's no right or wrong answer in life! \uD83D\uDCAF  The important thing is to find what you truly \uD83D\uDC96want\uD83D\uDC96 and have the \uD83D\uDCAA\uD83D\uDD25 courage and passion\uD83D\uDD25\uD83D\uDCAA to see it through!  \nAll you developers, believe in yourselves and chase your dreams! \uD83D\uDE80 You are the ones who will change the world! \uD83D\uDE4C\uD83C\uDF89",
//                ),
//                onRestartClick = {},
//                sharedTransitionScope = this@SharedTransitionLayout,
//                animatedVisibilityScope = this@AnimatedVisibility,
//            )
//        }
//    }
//}

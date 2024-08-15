/*
 * Copyright 2024 Suhyeon Kim(wisemuji), Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ui.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose_would_you_rather_game.composeapp.generated.resources.Res
import compose_would_you_rather_game.composeapp.generated.resources.restart
import compose_would_you_rather_game.composeapp.generated.resources.restart_caption
import org.jetbrains.compose.resources.stringResource

@Composable
fun RestartColumn(
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(Res.string.restart_caption),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        RestartButton(onRestartClick)
    }
}

@Composable
private fun RestartButton(
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onRestartClick,
        colors = ButtonDefaults
            .buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(Res.string.restart),
            color = Color.White,
            style = MaterialTheme.typography.h6,
        )
    }
}

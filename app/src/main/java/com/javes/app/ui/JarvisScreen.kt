package com.javes.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javes.app.ui.theme.JarvisBlack
import com.javes.app.ui.theme.JarvisBlue
import com.javes.app.ui.theme.JarvisCyan

@Composable
fun JarvisScreen(viewModel: JarvisViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(JarvisBlack)
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "J.A.V.E.S.",
            color = JarvisCyan,
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 8.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = state.label,
            color = JarvisBlue,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 4.sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            JarvisCore(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }

        JarvisWaveform(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        Spacer(Modifier.height(24.dp))
        Text(
            text = "MODO DE TESTE · ETAPA 1",
            color = JarvisCyan.copy(alpha = 0.6f),
            fontSize = 11.sp,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StateButton(JarvisState.IDLE, state) { viewModel.setState(it) }
            StateButton(JarvisState.LISTENING, state) { viewModel.setState(it) }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StateButton(JarvisState.PROCESSING, state) { viewModel.setState(it) }
            StateButton(JarvisState.SPEAKING, state) { viewModel.setState(it) }
        }
    }
}

@Composable
private fun RowScope.StateButton(
    target: JarvisState,
    current: JarvisState,
    onClick: (JarvisState) -> Unit
) {
    val selected = target == current
    OutlinedButton(
        onClick = { onClick(target) },
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) JarvisBlue.copy(alpha = 0.35f) else Color.Transparent,
            contentColor = JarvisCyan
        ),
        border = BorderStroke(1.dp, JarvisCyan.copy(alpha = if (selected) 1f else 0.4f))
    ) {
        Text(target.label, fontSize = 12.sp, maxLines = 1)
    }
}

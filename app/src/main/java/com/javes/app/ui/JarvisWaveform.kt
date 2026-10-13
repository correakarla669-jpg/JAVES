package com.javes.app.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import com.javes.app.ui.theme.JarvisCyan
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

/** Ondas animadas que reagem ao estado (escuta e fala). */
@Composable
fun JarvisWaveform(state: JarvisState, modifier: Modifier = Modifier) {
    val time = rememberAnimationTime()
    val amplitude by animateFloatAsState(
        targetValue = when (state) {
            JarvisState.IDLE -> 0.05f
            JarvisState.LISTENING -> 0.55f
            JarvisState.PROCESSING -> 0.15f
            JarvisState.SPEAKING -> 0.9f
        },
        animationSpec = tween(400),
        label = "amplitude"
    )

    Canvas(modifier = modifier) {
        val bars = 41
        val gap = size.width / bars
        val mid = size.height / 2f
        val t = time.value
        for (i in 0 until bars) {
            val x = gap * (i + 0.5f)
            val pos = i / (bars - 1f)
            val envelope = sin(pos * PI).toFloat()
            val wave = (sin(t * 6f + i * 0.55f) + sin(t * 3.3f - i * 0.9f)) / 2f
            val h = (size.height * 0.08f + size.height * 0.9f * amplitude * envelope * abs(wave))
                .coerceAtMost(size.height)
            drawLine(
                color = JarvisCyan.copy(alpha = 0.4f + 0.6f * envelope),
                start = Offset(x, mid - h / 2f),
                end = Offset(x, mid + h / 2f),
                strokeWidth = gap * 0.5f,
                cap = StrokeCap.Round
            )
        }
    }
}

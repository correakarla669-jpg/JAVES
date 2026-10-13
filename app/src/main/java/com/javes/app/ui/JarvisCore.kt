package com.javes.app.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.javes.app.ui.theme.JarvisBlue
import com.javes.app.ui.theme.JarvisCyan
import kotlin.math.cos
import kotlin.math.sin

private class Ring(
    val radiusFactor: Float,
    val sweep: Float,
    val width: Float,
    val speedFactor: Float,
    val color: Color
)

private val rings = listOf(
    Ring(0.50f, 70f, 5f, 1.0f, JarvisCyan),
    Ring(0.64f, 100f, 3f, -0.7f, JarvisBlue),
    Ring(0.78f, 55f, 4f, 0.45f, JarvisCyan)
)

/** Núcleo circular animado que representa a IA. */
@Composable
fun JarvisCore(state: JarvisState, modifier: Modifier = Modifier) {
    val intensity by animateFloatAsState(
        targetValue = when (state) {
            JarvisState.IDLE -> 0.25f
            JarvisState.LISTENING -> 0.7f
            JarvisState.PROCESSING -> 0.9f
            JarvisState.SPEAKING -> 1f
        },
        animationSpec = tween(600),
        label = "intensity"
    )
    val spinSpeed by animateFloatAsState(
        targetValue = when (state) {
            JarvisState.IDLE -> 20f
            JarvisState.LISTENING -> 45f
            JarvisState.PROCESSING -> 220f
            JarvisState.SPEAKING -> 70f
        },
        animationSpec = tween(800),
        label = "spin"
    )
    val ripple by animateFloatAsState(
        targetValue = if (state == JarvisState.LISTENING || state == JarvisState.SPEAKING) 1f else 0f,
        animationSpec = tween(500),
        label = "ripple"
    )

    val currentSpin by rememberUpdatedState(spinSpeed)
    var time by remember { mutableFloatStateOf(0f) }
    var angle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var last = withFrameNanos { it }
        while (true) {
            val now = withFrameNanos { it }
            val dt = (now - last) / 1_000_000_000f
            last = now
            time += dt
            angle = (angle + currentSpin * dt) % 3600f
        }
    }

    Canvas(modifier = modifier) {
        val t = time
        val a = angle
        val c = center
        val base = size.minDimension / 2f

        // Brilho de fundo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    JarvisCyan.copy(alpha = 0.08f + 0.30f * intensity),
                    Color.Transparent
                ),
                center = c,
                radius = base
            ),
            radius = base,
            center = c
        )

        // Marcas na borda externa
        for (i in 0 until 60) {
            val rad = Math.toRadians((a * 0.3f + i * 6f).toDouble())
            val outer = base * if (i % 5 == 0) 0.99f else 0.96f
            val inner = base * 0.92f
            drawLine(
                color = JarvisCyan.copy(alpha = if (i % 5 == 0) 0.7f else 0.3f),
                start = Offset(c.x + cos(rad).toFloat() * inner, c.y + sin(rad).toFloat() * inner),
                end = Offset(c.x + cos(rad).toFloat() * outer, c.y + sin(rad).toFloat() * outer),
                strokeWidth = if (i % 5 == 0) 3f else 2f
            )
        }

        // Anéis em arco girando
        rings.forEach { ring ->
            val r = base * ring.radiusFactor
            for (k in 0 until 3) {
                drawArc(
                    color = ring.color.copy(alpha = 0.35f + 0.55f * intensity),
                    startAngle = a * ring.speedFactor + k * 120f,
                    sweepAngle = ring.sweep,
                    useCenter = false,
                    topLeft = Offset(c.x - r, c.y - r),
                    size = Size(r * 2, r * 2),
                    style = Stroke(width = ring.width, cap = StrokeCap.Round)
                )
            }
        }

        // Ondas expandindo (ouvindo / falando)
        val pulse = 1f + 0.05f * sin(t * (2f + 4f * intensity))
        val coreRadius = base * 0.30f * pulse
        if (ripple > 0.01f) {
            for (i in 0 until 3) {
                val phase = (t * 0.6f + i / 3f) % 1f
                val r = coreRadius + (base * 0.5f - coreRadius) * phase
                drawCircle(
                    color = JarvisCyan.copy(alpha = (1f - phase) * 0.55f * ripple),
                    radius = r,
                    center = c,
                    style = Stroke(width = 3f)
                )
            }
        }

        // Núcleo
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, JarvisCyan, JarvisBlue),
                center = c,
                radius = coreRadius
            ),
            radius = coreRadius,
            center = c
        )
    }
}

package com.example.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MinerStatus
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MiningReactorCore(
    status: MinerStatus,
    onToggleMining: () -> Unit,
    onTapBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "reactor_anim")

    // Rotation angle for circuit ring
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (status.isMining) 8000 else 40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Reverse inner ring
    val innerRingRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (status.isMining) 12000 else 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "inner_rotation"
    )

    // Pulse scale for energy aura
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Tap boost visual feedback
    var isTapped by remember { mutableStateOf(false) }
    val tapScale by animateFloatAsState(
        targetValue = if (isTapped) 0.93f else 1.0f,
        animationSpec = tween(120),
        label = "tap_scale"
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(CyberCardBg, Color(0xFF0F172A))
                )
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top status row: Algorithm & Ping
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (status.isMining) Color(0xFF064E3B) else Color(0xFF334155),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (status.isMining) NeonEmerald else AlertRed)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (status.isMining) "NÚT KHAI THÁC ĐANG CHẠY" else "MÁY ĐÀO TẠM DỪNG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (status.isMining) NeonEmerald else TextMuted
                    )
                }
            }

            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${status.pingMs}ms • Stratum V2",
                    fontSize = 11.sp,
                    color = NeonCyan,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Center Reactor Disc
        Box(
            modifier = Modifier
                .size(230.dp)
                .scale(if (status.isMining) pulseGlow * tapScale else tapScale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if (status.isMining) {
                        isTapped = true
                        onTapBoost()
                        coroutineScope.launch {
                            delay(120)
                            isTapped = false
                        }
                    }
                }
                .testTag("mining_reactor_core"),
            contentAlignment = Alignment.Center
        ) {
            // Draw holographic glowing rings & orbit particles
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radiusOuter = size.width / 2f - 12.dp.toPx()
                val radiusInner = radiusOuter - 26.dp.toPx()

                // Ambient glow circle
                if (status.isMining) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(NeonCyanGlow, Color.Transparent),
                            center = center,
                            radius = size.width / 2f
                        ),
                        radius = size.width / 2f
                    )
                }

                // Outer dashed ring
                drawCircle(
                    color = if (status.isMining) CyberBorder else Color(0xFF1F2937),
                    radius = radiusOuter,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Rotating cyan segment arcs
                if (status.isMining) {
                    for (i in 0 until 4) {
                        val startAngle = ringRotation + (i * 90f)
                        drawArc(
                            color = NeonCyan,
                            startAngle = startAngle,
                            sweepAngle = 45f,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Rotating inner golden arcs
                    for (i in 0 until 3) {
                        val startAngle = innerRingRotation + (i * 120f)
                        drawArc(
                            color = CryptoGold,
                            startAngle = startAngle,
                            sweepAngle = 35f,
                            useCenter = false,
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Floating orbiting particle nodes
                    val angleRad = Math.toRadians(ringRotation.toDouble())
                    val nodeX = center.x + radiusOuter * cos(angleRad).toFloat()
                    val nodeY = center.y + radiusOuter * sin(angleRad).toFloat()
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(nodeX, nodeY)
                    )
                }
            }

            // Central core circular button
            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                color = Color(0xFF0F172A),
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (status.isMining) NeonCyan else Color(0xFF334155)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (status.isMining) Icons.Default.Bolt else Icons.Default.PowerSettingsNew,
                        contentDescription = "Mining Power",
                        tint = if (status.isMining) CryptoGold else TextMuted,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (status.isMining) "${status.hashrateMhs}" else "0.0",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "MH/s (x${status.boostMultiplier})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (status.isMining) NeonCyan else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Tap to boost hint
        if (status.isMining) {
            Surface(
                color = Color(0x2200E5FF),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "Boost",
                        tint = CryptoGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Chạm vào tâm để tăng tốc độ đào tức thì!",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Power Toggle Button
        FilledTonalButton(
            onClick = onToggleMining,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("toggle_mining_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (status.isMining) Color(0xFF1E293B) else NeonCyan,
                contentColor = if (status.isMining) AlertRed else Color(0xFF041E28)
            )
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (status.isMining) "TẠM DỪNG MÁY ĐÀO" else "BẮT ĐẦU ĐÀO COIN",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HashratePoint
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HashrateHistoryChart(
    historyPoints: List<HashratePoint>,
    currentHashrateMhs: Double,
    modifier: Modifier = Modifier
) {
    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
    var selectedTimeRange by remember { mutableStateOf("24H") }

    val points = if (historyPoints.isEmpty()) {
        listOf(HashratePoint("00:00", 42.0), HashratePoint("Bây giờ", 48.0))
    } else historyPoints

    val maxHashrate = (points.maxOfOrNull { it.hashrateMhs } ?: 70.0).coerceAtLeast(60.0) * 1.15
    val minHashrate = ((points.minOfOrNull { it.hashrateMhs } ?: 30.0) * 0.75).coerceAtLeast(0.0)
    val avgHashrate = points.map { it.hashrateMhs }.average()
    val peakHashrate = points.maxOfOrNull { it.hashrateMhs } ?: currentHashrateMhs

    val activePoint = selectedPointIndex?.let { points.getOrNull(it) } ?: points.lastOrNull()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, CyberBorder, RoundedCornerShape(22.dp)),
        color = CyberCardBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Title & Time Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F2633)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "BIỂU ĐỒ HIỆU SUẤT HASHRATE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NeonEmerald)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Thời gian thực 24h qua",
                                fontSize = 10.sp,
                                color = NeonEmerald
                            )
                        }
                    }
                }

                // Range pills (24H, 7D, 30D)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1E293B))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf("24H", "7D", "30D").forEach { range ->
                        val isSelected = selectedTimeRange == range
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) NeonCyan else Color.Transparent,
                            modifier = Modifier.clickable { selectedTimeRange = range }
                        ) {
                            Text(
                                text = range,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF041E28) else TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Highlight Metric Stats (Recharts KPI bar style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn(
                    label = "Hiện tại",
                    value = "${"%.1f".format(currentHashrateMhs)} MH/s",
                    valueColor = NeonCyan
                )
                MetricColumn(
                    label = "Trung bình 24h",
                    value = "${"%.1f".format(avgHashrate)} MH/s",
                    valueColor = CryptoGold
                )
                MetricColumn(
                    label = "Đỉnh (Peak)",
                    value = "${"%.1f".format(peakHashrate)} MH/s",
                    valueColor = NeonEmerald
                )
                MetricColumn(
                    label = "Chia sẻ hợp lệ",
                    value = "99.8%",
                    valueColor = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tooltip Banner for Inspected / Active Point
            if (activePoint != null) {
                Surface(
                    color = Color(0xFF0F1E2E),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mốc ${activePoint.timeLabel}:",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${activePoint.hashrateMhs} MH/s",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${activePoint.validShares} shares • ${activePoint.efficiency * 100}% hiệu suất)",
                                fontSize = 10.sp,
                                color = NeonEmerald
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Interactive D3 / Recharts-style Canvas Spline Area Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .pointerInput(points) {
                        detectTapGestures { offset ->
                            val step = size.width / (points.size - 1).coerceAtLeast(1)
                            val index = ((offset.x + step / 2) / step).toInt().coerceIn(0, points.lastIndex)
                            selectedPointIndex = index
                        }
                    }
                    .pointerInput(points) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            val step = size.width / (points.size - 1).coerceAtLeast(1)
                            val index = ((change.position.x + step / 2) / step).toInt().coerceIn(0, points.lastIndex)
                            selectedPointIndex = index
                        }
                    }
                    .testTag("hashrate_history_chart_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val n = points.size
                    if (n < 2) return@Canvas

                    val range = (maxHashrate - minHashrate).coerceAtLeast(1.0)
                    fun getY(mhs: Double): Float {
                        val norm = (mhs - minHashrate) / range
                        return (h - (norm * (h - 24.dp.toPx())) - 12.dp.toPx()).toFloat()
                    }

                    // 1. Horizontal Reference Grid Lines
                    val gridLevels = listOf(0.25f, 0.5f, 0.75f)
                    val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    for (gl in gridLevels) {
                        val y = h * gl
                        drawLine(
                            color = Color(0xFF1E293B),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dashedEffect
                        )
                    }

                    // 2. Average Reference Line (Recharts ReferenceLine style in gold)
                    val avgY = getY(avgHashrate)
                    drawLine(
                        color = CryptoGold.copy(alpha = 0.6f),
                        start = Offset(0f, avgY),
                        end = Offset(w, avgY),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                    )

                    // 3. Build Smooth Bezier Line & Gradient Fill Path
                    val stepX = w / (n - 1)
                    val strokePath = Path()
                    val fillPath = Path()

                    val coordinates = points.mapIndexed { i, p ->
                        Offset(i * stepX, getY(p.hashrateMhs))
                    }

                    strokePath.moveTo(coordinates[0].x, coordinates[0].y)
                    fillPath.moveTo(coordinates[0].x, h)
                    fillPath.lineTo(coordinates[0].x, coordinates[0].y)

                    for (i in 0 until coordinates.size - 1) {
                        val p0 = coordinates[i]
                        val p1 = coordinates[i + 1]
                        val cx1 = p0.x + (p1.x - p0.x) / 2f
                        val cy1 = p0.y
                        val cx2 = p0.x + (p1.x - p0.x) / 2f
                        val cy2 = p1.y

                        strokePath.cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
                        fillPath.cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
                    }

                    fillPath.lineTo(coordinates.last().x, h)
                    fillPath.close()

                    // Draw Gradient Area (Cyan to transparent dark)
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.35f),
                                NeonCyan.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = h
                        )
                    )

                    // Draw Line Stroke
                    drawPath(
                        path = strokePath,
                        color = NeonCyan,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // 4. Draw Selected Indicator & Cursor
                    val inspectedIdx = selectedPointIndex ?: (n - 1)
                    val selectedPos = coordinates[inspectedIdx]

                    // Vertical guide cursor
                    drawLine(
                        color = NeonCyan.copy(alpha = 0.6f),
                        start = Offset(selectedPos.x, 0f),
                        end = Offset(selectedPos.x, h),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    // Outer glowing halo
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.3f),
                        radius = 10.dp.toPx(),
                        center = selectedPos
                    )
                    // Inner dot
                    drawCircle(
                        color = NeonCyan,
                        radius = 5.dp.toPx(),
                        center = selectedPos
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = selectedPos
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // X-Axis Time Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labelIndices = listOf(0, 3, 6, 9, points.lastIndex)
                labelIndices.forEach { idx ->
                    val label = points.getOrNull(idx)?.timeLabel ?: ""
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = if (idx == points.lastIndex) NeonCyan else TextMuted,
                        fontWeight = if (idx == points.lastIndex) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricColumn(
    label: String,
    value: String,
    valueColor: Color
) {
    Column {
        Text(text = label, fontSize = 10.sp, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            fontFamily = FontFamily.Monospace
        )
    }
}

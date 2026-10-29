package com.example.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CryptoAsset
import com.example.model.CryptoTransaction
import com.example.model.TransactionType
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.CurrencyMode

@Composable
fun WalletScreen(
    assets: List<CryptoAsset>,
    transactions: List<CryptoTransaction>,
    currencyMode: CurrencyMode,
    onOpenWithdraw: () -> Unit,
    onOpenTransferInApp: () -> Unit,
    onOpenReceive: () -> Unit,
    onToggleCurrency: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val totalUsd = assets.sumOf { it.balanceValueUsd }
    val totalVnd = totalUsd * 25450.0

    val filteredTransactions = when (selectedFilter) {
        "WITHDRAW" -> transactions.filter { it.type == TransactionType.SENT && it.address.contains("Rút ví ngoài") }
        "TRANSFER" -> transactions.filter { it.type == TransactionType.SENT && it.address.contains("nội bộ") }
        "RECEIVE" -> transactions.filter { it.type == TransactionType.RECEIVED }
        "MINED" -> transactions.filter { it.type == TransactionType.MINED_REWARD || it.type == TransactionType.AIRDROP_BONUS }
        else -> transactions
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Portfolio Total Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF131D31), Color(0xFF0F172A))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("VÍ MÃ HÓA BẢO MẬT NOVA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonEmerald)
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier.clickable(onClick = onToggleCurrency)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.CurrencyExchange, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (currencyMode == CurrencyMode.VND) "VND (₫)" else "USD ($)",
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Tổng giá trị tài sản ròng", fontSize = 12.sp, color = TextMuted)

                    Spacer(modifier = Modifier.height(4.dp))

                    if (currencyMode == CurrencyMode.VND) {
                        Text(
                            text = "${"%,d".format(totalVnd.toLong())} ₫",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Text(
                            text = "$${"%.2f".format(totalUsd)} USD",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+$142.80 (+8.45%) trong 24h qua",
                            fontSize = 12.sp,
                            color = NeonEmerald,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated 7-day profit wave canvas
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        val points = listOf(0.3f, 0.45f, 0.4f, 0.65f, 0.58f, 0.82f, 0.95f)
                        val stepX = size.width / (points.size - 1)
                        val path = Path()

                        points.forEachIndexed { index, ratio ->
                            val x = index * stepX
                            val y = size.height - (ratio * size.height)
                            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }

                        drawPath(
                            path = path,
                            color = NeonCyan,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Triple Action Row: Rút ví ngoài, Chuyển nội bộ, Nhận coin
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Rút ví ngoài (External Withdrawal)
                        Button(
                            onClick = onOpenWithdraw,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("wallet_withdraw_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF041E28), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rút Ví", color = Color(0xFF041E28), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        // 2. Chuyển nội bộ (In-app internal transfer)
                        Button(
                            onClick = onOpenTransferInApp,
                            modifier = Modifier
                                .weight(1.1f)
                                .height(44.dp)
                                .testTag("wallet_transfer_inapp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald)
                        ) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null, tint = Color(0xFF022C22), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nội Bộ (0đ)", color = Color(0xFF022C22), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }

                        // 3. Nhận coin (Receive & QR)
                        OutlinedButton(
                            onClick = onOpenReceive,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("wallet_receive_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CryptoGold)
                        ) {
                            Icon(imageVector = Icons.Default.QrCode2, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nạp/QR", color = CryptoGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Crypto Assets Section Header
        item {
            Text("DANH MỤC TIỀN MÃ HOÁ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        // Crypto Assets List
        items(assets) { asset ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(asset.hexColor).copy(alpha = 0.15f))
                                .border(1.5.dp, Color(asset.hexColor), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = asset.symbol.take(2),
                                fontWeight = FontWeight.Bold,
                                color = Color(asset.hexColor),
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = asset.symbol,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = asset.name,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = "${asset.network} • $${"%,.2f".format(asset.priceUsd)}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "%.4f".format(asset.balance),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace
                        )

                        val fiatStr = if (currencyMode == CurrencyMode.VND) {
                            "≈ ${"%,d".format(asset.balanceValueVnd.toLong())} ₫"
                        } else {
                            "≈ $${"%.2f".format(asset.balanceValueUsd)}"
                        }

                        Text(
                            text = fiatStr,
                            fontSize = 11.sp,
                            color = if (asset.change24hPercent >= 0) NeonEmerald else AlertRed
                        )
                    }
                }
            }
        }

        // Transactions Ledger Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("LỊCH SỬ GIAO DỊCH", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("${transactions.size} bản ghi", fontSize = 11.sp, color = TextMuted)
            }
        }

        // Filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filters = listOf(
                    "ALL" to "Tất cả",
                    "WITHDRAW" to "Rút ví ngoài",
                    "TRANSFER" to "Chuyển nội bộ",
                    "RECEIVE" to "Đã nhận",
                    "MINED" to "Thưởng đào"
                )
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) NeonCyan else Color(0xFF1E293B),
                        modifier = Modifier.clickable { selectedFilter = key }
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF041E28) else TextSecondary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Transactions List
        items(filteredTransactions) { tx ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val iconColor = when (tx.type) {
                            TransactionType.SENT -> AlertRed
                            TransactionType.RECEIVED -> NeonEmerald
                            TransactionType.MINED_REWARD -> CryptoGold
                            TransactionType.AIRDROP_BONUS -> NeonCyan
                            TransactionType.RIG_PURCHASE -> Color(0xFFA855F7)
                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(iconColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (tx.type) {
                                    TransactionType.SENT -> Icons.Default.ArrowOutward
                                    TransactionType.RECEIVED -> Icons.Default.ArrowDownward
                                    else -> Icons.Default.Shield
                                },
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = tx.address,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "${tx.dateString} • Tx: ${tx.txHash.take(12)}...",
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        val prefix = when (tx.type) {
                            TransactionType.SENT, TransactionType.RIG_PURCHASE -> "-"
                            else -> "+"
                        }
                        val amountColor = when (tx.type) {
                            TransactionType.SENT, TransactionType.RIG_PURCHASE -> AlertRed
                            else -> NeonEmerald
                        }

                        Text(
                            text = "$prefix${"%.4f".format(tx.amount)} ${tx.symbol}",
                            fontWeight = FontWeight.Bold,
                            color = amountColor,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Đã xác nhận",
                            fontSize = 9.sp,
                            color = NeonEmerald
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

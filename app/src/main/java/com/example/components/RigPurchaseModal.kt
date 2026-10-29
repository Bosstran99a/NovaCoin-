package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MiningRig
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RigPurchaseModal(
    rig: MiningRig,
    userUsdtBalance: Double,
    onConfirmPurchase: (pin: String) -> Unit,
    onQuickDepositUsdt: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var pinInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val hasEnoughUsdt = userUsdtBalance >= rig.costUsdt

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0F2633)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = CryptoGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Mở Khóa Dàn Đào Bằng USDT",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Thanh toán an toàn • Kích hoạt ngay",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Rig Details Card
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rig.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            Surface(
                                color = Color(0x33FFB800),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${rig.costUsdt.toInt()}$ USDT",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = CryptoGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tốc độ: +${rig.hashrateMhs} MH/s",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonEmerald
                            )
                            Text(
                                text = "${rig.coolingEfficiency}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Wallet Balance Status
                Surface(
                    color = if (hasEnoughUsdt) Color(0xFF0C2436) else Color(0xFF2D1515),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (hasEnoughUsdt) NeonCyan else AlertRed
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Số dư USDT hiện có:", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = "${"%.2f".format(userUsdtBalance)} USDT",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (hasEnoughUsdt) TextPrimary else AlertRed,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (!hasEnoughUsdt) {
                            val missing = rig.costUsdt - userUsdtBalance
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Còn thiếu:", fontSize = 10.sp, color = AlertRed)
                                Text(
                                    text = "${"%.2f".format(missing)} USDT",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AlertRed
                                )
                            }
                        } else {
                            Text(
                                text = "Đủ thanh toán ✓",
                                fontSize = 11.sp,
                                color = NeonEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // If insufficient balance, show quick deposit options
                if (!hasEnoughUsdt) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Nạp thêm USDT để thanh toán mốc ${rig.costUsdt.toInt()}$:", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val depositAmounts = listOf(20.0, 100.0, 500.0, 1000.0)
                        depositAmounts.forEach { amt ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (amt == rig.costUsdt) CryptoGold else Color(0xFF1E293B),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onQuickDepositUsdt(amt) }
                            ) {
                                Text(
                                    text = "+${amt.toInt()}$",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (amt == rig.costUsdt) Color(0xFF2E2000) else TextPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // PIN verification input
                Text("Xác minh mã PIN ví bảo mật (1234):", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4) pinInput = it },
                    label = { Text("Mã PIN 4 số") },
                    placeholder = { Text("1234") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("rig_purchase_pin_input")
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = error ?: "", color = AlertRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!hasEnoughUsdt) {
                        error = "Số dư USDT không đủ. Vui lòng nạp thêm USDT."
                    } else if (pinInput.length != 4) {
                        error = "Vui lòng nhập mã PIN bảo mật 4 số"
                    } else {
                        onConfirmPurchase(pinInput)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CryptoGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_rig_purchase_button")
            ) {
                Text(
                    text = "Xác Nhận Trừ ${rig.costUsdt.toInt()}$ USDT",
                    color = Color(0xFF2E2000),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Hủy", color = TextSecondary)
            }
        }
    )
}

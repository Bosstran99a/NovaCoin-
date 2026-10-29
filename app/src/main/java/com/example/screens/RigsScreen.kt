package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
fun RigsScreen(
    rigs: List<MiningRig>,
    currentBalanceNvc: Double,
    onEquipRig: (String) -> Unit,
    onPurchaseRig: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("THỊ TRƯỜNG DÀN ĐÀO CLOUD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CryptoGold)
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Số dư: ${"%.2f".format(currentBalanceNvc)} NVC",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Nâng Cấp Phần Cứng Đám Mây", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Thuê các cụm ASIC ảo hóa và siêu máy chủ Quantum để tăng tốc độ đào lên gấp nhiều lần.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        items(rigs) { rig ->
            val isCurrent = rig.isEquipped

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = if (isCurrent) Color(0xFF0F2633) else CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (isCurrent) NeonCyan else CyberBorder
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E293B))
                                    .border(1.5.dp, if (isCurrent) NeonCyan else CryptoGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = if (isCurrent) NeonCyan else CryptoGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = rig.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = NeonCyan,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "ĐANG CHẠY",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF041E28),
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${rig.tierTitle} • ${rig.algorithm}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${rig.hashrateMhs} MH/s",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NeonEmerald,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Tốc độ cơ bản",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = rig.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Specs badges: Cooling, Power
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.AcUnit, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = rig.coolingEfficiency, fontSize = 10.sp, color = TextPrimary)
                            }
                        }

                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "${rig.powerConsumptionWatts}W Tiết kiệm", fontSize = 10.sp, color = TextPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Button: Equip, In Use, or Purchase
                    if (rig.isUnlocked) {
                        if (isCurrent) {
                            OutlinedButton(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Dàn Đào Này Đang Hoạt Động", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = { onEquipRig(rig.id) },
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                            ) {
                                Text("Trang Bị Dàn Đào Này", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Button(
                            onClick = { onPurchaseRig(rig.id) },
                            modifier = Modifier.fillMaxWidth().height(42.dp).testTag("purchase_rig_${rig.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CryptoGold)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF2E2000), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Nâng Cấp Giá ${rig.costNvc} NVC",
                                color = Color(0xFF2E2000),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

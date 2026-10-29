package com.example.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.components.HashrateHistoryChart
import com.example.components.MiningReactorCore
import com.example.model.HashratePoint
import com.example.model.MinedBlock
import com.example.model.MinerStatus
import com.example.model.MiningPoolStats
import com.example.model.ReferralSystemState
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
fun MiningScreen(
    status: MinerStatus,
    poolStats: MiningPoolStats,
    referralState: ReferralSystemState,
    minedBlocks: List<MinedBlock>,
    hashrateHistory: List<HashratePoint>,
    currencyMode: CurrencyMode,
    onToggleMining: () -> Unit,
    onTapBoost: () -> Unit,
    onRenewSession: () -> Unit,
    onClaimRewards: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onNavigateToReferrals: () -> Unit,
    onNavigateToRigs: () -> Unit,
    onToggleCurrency: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Header Banner with Hero Asset
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, CyberBorder, RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_mining),
                    contentDescription = "Mining Rig Cloud",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xEE090D16),
                                    Color(0x88090D16),
                                    Color(0x44090D16)
                                )
                            )
                        )
                )

                // Info overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xCC064E3B),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(NeonEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("CLUSTERING STRATUM V2", fontSize = 10.sp, color = NeonEmerald, fontWeight = FontWeight.Bold)
                            }
                        }

                        IconButton(
                            onClick = onToggleCurrency,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x881E293B))
                        ) {
                            Text(
                                text = if (currencyMode == CurrencyMode.VND) "₫" else "$",
                                color = CryptoGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = "Hệ Thống Đào Cloud Sinh Thái",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Khai thác tối ưu • Không nóng máy • Không hao pin",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Live Coin Balance Card
        item {
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
                        Text("TỔNG SỐ DƯ NOVACOIN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Surface(
                            color = Color(0x22FFB800),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "+12.65% (24h)",
                                color = CryptoGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "%.6f".format(status.totalMinedNvc),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "NVC",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CryptoGold,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }

                    val fiatValue = if (currencyMode == CurrencyMode.VND) {
                        "≈ ${"%,d".format((status.totalMinedNvc * 1.48 * 25450).toLong())} ₫"
                    } else {
                        "≈ $${"%.2f".format(status.totalMinedNvc * 1.48)} USD"
                    }

                    Text(text = fiatValue, fontSize = 13.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenWithdraw,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("mining_quick_withdraw_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = Color(0xFF041E28), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rút Ví Ngoài", color = Color(0xFF041E28), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToReferrals,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CryptoGold)
                        ) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mời Bạn (+25%)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Active Interactive Reactor Core
        item {
            MiningReactorCore(
                status = status,
                onToggleMining = onToggleMining,
                onTapBoost = onTapBoost
            )
        }

        // 24-Hour Hashrate History Performance Chart (D3 / Recharts-style Native Visualization)
        item {
            HashrateHistoryChart(
                historyPoints = hashrateHistory,
                currentHashrateMhs = status.hashrateMhs
            )
        }

        // Limited-Time Referral Boost Active Banner
        if (referralState.activeBoostPercent > 0) {
            item {
                Surface(
                    color = Color(0xFF0C2436),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToReferrals)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Tăng tốc đào đội nhóm +${referralState.activeBoostPercent.toInt()}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NeonCyan
                                )
                                val hoursLeft = referralState.boostRemainingSeconds / 3600
                                val minsLeft = (referralState.boostRemainingSeconds % 3600) / 60
                                Text(
                                    text = "Còn hiệu lực: ${hoursLeft}h ${minsLeft}p • ${referralState.activeMembersCount} thợ đào hoạt động",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // 24-Hour Mining Session Cycle
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CHU KỲ ĐÀO 24 GIỜ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            val hours = status.sessionRemainingSeconds / 3600
                            val mins = (status.sessionRemainingSeconds % 3600) / 60
                            val secs = status.sessionRemainingSeconds % 60
                            Text(
                                text = String.format("%02d:%02d:%02d", hours, mins, secs),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = onRenewSession,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("renew_session_button")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Gia Hạn 24h", color = NeonCyan, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val progress = (status.totalSessionSeconds - status.sessionRemainingSeconds).toFloat() / status.totalSessionSeconds
                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = NeonCyan,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Uncollected reward claim
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Phần thưởng phiên tạm tính:", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = "+${"%.6f".format(status.uncollectedSessionNvc)} NVC",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonEmerald,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Button(
                            onClick = onClaimRewards,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("claim_session_rewards_button")
                        ) {
                            Text("Thu Hoạch", color = Color(0xFF022C22), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Live Mined Blocks Stream
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CÁC KHỐI VỪA KHAI THÁC",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Độ khó: ${poolStats.totalNetworkHashrateThs} TH/s",
                    fontSize = 11.sp,
                    color = NeonCyan
                )
            }
        }

        items(minedBlocks.take(4)) { block ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = if (block.isUserBlock) Color(0xFF0E2A24) else Color(0xFF111928),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (block.isUserBlock) NeonEmerald else CyberBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (block.isUserBlock) Color(0xFF064E3B) else Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#",
                                fontWeight = FontWeight.Bold,
                                color = if (block.isUserBlock) NeonEmerald else NeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Block #${block.blockHeight}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                if (block.isUserBlock) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = NeonEmerald,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "BẠN ĐÀO ĐƯỢC",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF022C22),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${block.minerNodeId} • ${block.timestampFormatted}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+${block.rewardNvc} NVC",
                            fontWeight = FontWeight.Bold,
                            color = CryptoGold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Nonce: ${block.nonce.toString().take(6)}...",
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

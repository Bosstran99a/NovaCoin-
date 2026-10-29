package com.example.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MemberMiningStatus
import com.example.model.ReferralSystemState
import com.example.model.ReferralTier
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

@Composable
fun ReferralScreen(
    referralState: ReferralSystemState,
    onOpenEnterCodeDialog: () -> Unit,
    onSimulateNewMember: () -> Unit,
    onShareLink: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Hero Referral Code Card
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
                            Icon(imageVector = Icons.Default.GroupAdd, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("LIÊN MINH THỢ ĐÀO NOVA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CryptoGold)
                        }

                        Surface(
                            color = Color(referralState.currentTier.badgeColorHex).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = referralState.currentTier.title,
                                color = Color(referralState.currentTier.badgeColorHex),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Mời Bạn Bè Cùng Đào Coin", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mỗi khi bạn bè đăng ký và bắt đầu đào, CẢ HAI đều nhận ngay +5.0 NVC và +25% tốc độ đào trong 48 giờ!",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // My Referral Code Box
                    Surface(
                        color = Color(0xFF0A0F1D),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("MÃ GIỚI THIỆU CỦA BẠN", fontSize = 10.sp, color = TextMuted)
                                Text(
                                    text = referralState.myReferralCode,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NeonCyan,
                                    letterSpacing = 2.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(referralState.myReferralCode))
                                        Toast.makeText(context, "Đã sao chép mã giới thiệu!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1E293B))
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Code", tint = NeonCyan, modifier = Modifier.size(18.dp))
                                }

                                IconButton(
                                    onClick = onShareLink,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan)
                                ) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF041E28), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons: Enter inviter code & Simulate new member
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!referralState.isInviterCodeApplied) {
                            Button(
                                onClick = onOpenEnterCodeDialog,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .testTag("open_enter_referral_code_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CryptoGold)
                            ) {
                                Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFF2E2000), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Nhập Mã Bạn", color = Color(0xFF2E2000), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        } else {
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF064E3B)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Đã nhập: ${referralState.appliedInviterCode}", color = NeonEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Simulation button to immediately see referral rewards in action
                        OutlinedButton(
                            onClick = onSimulateNewMember,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("simulate_new_member_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                        ) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mô Phỏng Mời", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Limited-Time Active Boost Status Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF0F1F30),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "+${referralState.activeBoostPercent.toInt()}% Tốc Độ Đào",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NeonCyan
                                )
                            }
                            val hoursLeft = referralState.boostRemainingSeconds / 3600
                            val minsLeft = (referralState.boostRemainingSeconds % 3600) / 60
                            val secsLeft = referralState.boostRemainingSeconds % 60
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Thời gian tăng tốc còn: ${hoursLeft}h ${minsLeft}p ${secsLeft}s",
                                    fontSize = 11.sp,
                                    color = CryptoGold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Tổng thưởng nhận", fontSize = 10.sp, color = TextMuted)
                        Text(
                            text = "+${referralState.totalBonusEarnedNvc} NVC",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonEmerald
                        )
                    }
                }
            }
        }

        // Tier Progression Bar
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
                        Text("CẤP BẬC LIÊN MINH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = "${referralState.totalMembersCount}/6 Bạn bè để lên Cấp Vàng",
                            fontSize = 11.sp,
                            color = CryptoGold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val progress = (referralState.totalMembersCount.toFloat() / 6f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = CryptoGold,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TierBadge("Tân thủ (0)", true)
                        TierBadge("Bạc (3)", referralState.totalMembersCount >= 3)
                        TierBadge("Vàng (6)", referralState.totalMembersCount >= 6)
                        TierBadge("Kim Cương (12)", referralState.totalMembersCount >= 12)
                    }
                }
            }
        }

        // Squad Members List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DANH SÁCH THỢ ĐÀO ĐÃ MỜI (${referralState.totalMembersCount})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${referralState.activeMembersCount} Đang đào",
                    fontSize = 11.sp,
                    color = NeonEmerald
                )
            }
        }

        items(referralState.members) { member ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .border(1.5.dp, if (member.status == MemberMiningStatus.ACTIVE_MINING) NeonEmerald else TextMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = member.avatarInitials,
                                fontWeight = FontWeight.Bold,
                                color = if (member.status == MemberMiningStatus.ACTIVE_MINING) NeonEmerald else TextMuted,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "@${member.username}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                if (member.status == MemberMiningStatus.ACTIVE_MINING) {
                                    Surface(
                                        color = Color(0xFF064E3B),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ĐANG ĐÀO",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonEmerald,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Tham gia: ${member.joinedDateString} • +${member.contributedHashrateMhs} MH/s",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+${member.totalBonusNvcGenerated} NVC",
                            fontWeight = FontWeight.Bold,
                            color = CryptoGold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "+25% hashrate",
                            fontSize = 10.sp,
                            color = NeonCyan
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun TierBadge(label: String, isAchieved: Boolean) {
    Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = if (isAchieved) FontWeight.Bold else FontWeight.Normal,
        color = if (isAchieved) CryptoGold else TextMuted
    )
}

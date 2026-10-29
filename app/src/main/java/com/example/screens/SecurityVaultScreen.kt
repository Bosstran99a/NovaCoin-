package com.example.screens

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AuditSeverity
import com.example.model.SecurityProfile
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
fun SecurityVaultScreen(
    securityProfile: SecurityProfile,
    onLockAppNow: () -> Unit,
    onToggleBiometrics: (Boolean) -> Unit,
    onOpenChangePin: () -> Unit,
    onOpenSeedBackup: () -> Unit,
    onOpenPrivateKey: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Hero Security Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, CyberBorder, RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_vault),
                    contentDescription = "Crypto Vault Security",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = Color(0xCC064E3B),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("MÃ HÓA PHẦN CỨNG AES-256", fontSize = 10.sp, color = NeonEmerald, fontWeight = FontWeight.Bold)
                        }
                    }

                    Column {
                        Text("Trung Tâm Bảo Mật Tối Cao", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                        Text("Khóa ví lưu an toàn trong Android TEE Keystore", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
        }

        // Security Health Score Card
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
                        Text("ĐIỂM SỐ BẢO MẬT HỆ THỐNG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = "${securityProfile.securityScore}/100",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonEmerald,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { securityProfile.securityScore / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonEmerald,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = securityProfile.securityStatusLabel,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Instant Lock App Button
                    Button(
                        onClick = onLockAppNow,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("lock_app_now_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = AlertRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Khóa Ứng Dụng Ngay (Yêu Cầu PIN / Vân Tay)", color = AlertRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Security Actions & Controls
        item {
            Text("CẤU HÌNH BẢO MẬT VÍ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        // 1. Biometric Authentication Toggle
        item {
            SecurityOptionCard(
                icon = Icons.Default.Fingerprint,
                iconTint = NeonCyan,
                title = "Mở khóa sinh trắc học vân tay",
                subtitle = "Xác thực tức thì khi mở app hoặc ký lệnh rút tiền",
                trailing = {
                    Switch(
                        checked = securityProfile.isBiometricEnabled,
                        onCheckedChange = onToggleBiometrics,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonEmerald,
                            checkedTrackColor = Color(0xFF064E3B),
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = Color(0xFF1E293B)
                        ),
                        modifier = Modifier.testTag("biometric_switch")
                    )
                }
            )
        }

        // 2. Change PIN Code
        item {
            SecurityOptionCard(
                icon = Icons.Default.Password,
                iconTint = CryptoGold,
                title = "Đổi mã PIN bảo mật 4 số",
                subtitle = "Mã PIN hiện tại đang bảo vệ các giao dịch nhạy cảm",
                onClick = onOpenChangePin
            )
        }

        // 3. 12-Word BIP-39 Seed Phrase Backup
        item {
            SecurityOptionCard(
                icon = Icons.Default.Shield,
                iconTint = if (securityProfile.isSeedPhraseBackedUp) NeonEmerald else AlertRed,
                title = "12 Cụm từ hạt giống (BIP-39)",
                subtitle = if (securityProfile.isSeedPhraseBackedUp) "Đã sao lưu an toàn • Nhấn để xem lại" else "Chưa sao lưu! Nhấn để ghi chép lại ngay (+3 NVC)",
                onClick = onOpenSeedBackup
            )
        }

        // 4. Export Private Key (Encrypted with PIN)
        item {
            SecurityOptionCard(
                icon = Icons.Default.Key,
                iconTint = AlertRed,
                title = "Xuất khóa riêng tư (Private Key)",
                subtitle = "Yêu cầu mã PIN để giải mã chuỗi Hex Ed25519",
                onClick = onOpenPrivateKey
            )
        }

        // 5. Anti-Phishing Security Code
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("MÃ CHỐNG LỪA ĐẢO (ANTI-PHISHING)", fontSize = 10.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = securityProfile.antiPhishingPhrase,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CryptoGold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Đã xác thực",
                            color = NeonEmerald,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Security Audit Log Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("NHẬT KÝ KIỂM TOÁN AN NINH (AUDIT LOG)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Bảo mật thực", fontSize = 11.sp, color = NeonEmerald)
            }
        }

        // Audit Logs
        items(securityProfile.auditHistory) { log ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = CyberCardBg,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                when (log.severity) {
                                    AuditSeverity.SECURE -> NeonEmerald
                                    AuditSeverity.WARNING -> AlertRed
                                    else -> NeonCyan
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = log.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                        Text(text = log.detail, fontSize = 11.sp, color = TextSecondary)
                        Text(text = "${log.timeAgo} • ${log.ipAddress}", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun SecurityOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = CyberCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                    Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
                }
            }

            if (trailing != null) {
                trailing()
            } else if (onClick != null) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
            }
        }
    }
}

package com.example.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PinSecurityLockScreen(
    pinLength: Int = 4,
    enteredPin: String,
    errorMessage: String?,
    isScanningBiometric: Boolean,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onBiometricClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(CyberDarkBg, Color(0xFF030712))
                )
            )
            .padding(24.dp)
            .testTag("pin_security_lock_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Shield Icon with glow
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F172A))
                    .border(2.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Security Vault",
                    tint = NeonCyan,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "NOVA SECURE VAULT",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Xác thực mã PIN bảo mật để mở khóa ví và máy đào",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = Color(0x2200E5FF),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Mã PIN mặc định: 1234",
                    fontSize = 11.sp,
                    color = NeonCyan,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // PIN Dots Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until pinLength) {
                    val isFilled = i < enteredPin.length
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilled) NeonCyan else Color(0xFF1E293B)
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (isFilled) NeonCyan else Color(0xFF475569),
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error or biometric scanning state
            AnimatedVisibility(
                visible = errorMessage != null || isScanningBiometric,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (isScanningBiometric) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = NeonEmerald,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đang quét sinh trắc học vân tay an toàn...",
                            color = NeonEmerald,
                            fontSize = 12.sp
                        )
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = AlertRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Numeric Keypad (1 to 9, Biometric, 0, Backspace)
            val keypad = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("BIO", "0", "DEL")
            )

            for (row in keypad) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (key in row) {
                        KeypadButton(
                            label = key,
                            onClick = {
                                when (key) {
                                    "BIO" -> onBiometricClick()
                                    "DEL" -> onDeleteClick()
                                    else -> onDigitClick(key)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(
                when (label) {
                    "BIO" -> Color(0xFF0F2633)
                    "DEL" -> Color(0xFF1F1E2E)
                    else -> Color(0xFF131D31)
                }
            )
            .border(
                1.dp,
                when (label) {
                    "BIO" -> NeonCyan
                    "DEL" -> Color(0xFF334155)
                    else -> Color(0xFF1E293B)
                },
                CircleShape
            )
            .clickable(onClick = onClick)
            .testTag("keypad_$label"),
        contentAlignment = Alignment.Center
    ) {
        when (label) {
            "BIO" -> {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Sinh trắc học",
                    tint = NeonCyan,
                    modifier = Modifier.size(32.dp)
                )
            }
            "DEL" -> {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Xóa",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            else -> {
                Text(
                    text = label,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}

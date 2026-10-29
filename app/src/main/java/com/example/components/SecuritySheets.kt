package com.example.components

import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun SeedBackupModal(
    seedWords: List<String>,
    isBackedUp: Boolean,
    onConfirmBackup: () -> Unit,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var isRevealed by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = CryptoGold,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "12 Cụm Từ Hạt Giống (BIP-39)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = Color(0x22EF4444),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = AlertRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tuyệt đối không chia sẻ cụm từ này với bất kỳ ai. Bất kỳ ai có cụm từ này đều có thể chiếm đoạt coin của bạn!",
                            fontSize = 11.sp,
                            color = AlertRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Toggle reveal button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isRevealed) "Đang hiển thị từ khoá" else "Đang che giấu an toàn",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    IconButton(onClick = { isRevealed = !isRevealed }) {
                        Icon(
                            imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle Reveal",
                            tint = NeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 12 Words Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0A0F1D))
                        .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    val rows = seedWords.chunked(3)
                    for ((rowIndex, rowWords) in rows.withIndex()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for ((colIndex, word) in rowWords.withIndex()) {
                                val index = rowIndex * 3 + colIndex + 1
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF131D31),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E2B47))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "$index.",
                                            fontSize = 10.sp,
                                            color = TextMuted,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isRevealed) word else "••••",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isRevealed) NeonCyan else TextMuted,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Copy Seed Phrase Button
                OutlinedButton(
                    onClick = {
                        val allWords = seedWords.joinToString(" ")
                        clipboardManager.setText(AnnotatedString(allWords))
                        Toast.makeText(context, "Đã sao chép 12 từ vào bộ nhớ tạm an toàn!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sao chép 12 từ", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmBackup,
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_seed_backup_button")
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Tôi đã ghi lại an toàn", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Đóng", color = TextSecondary)
            }
        }
    )
}

@Composable
fun PrivateKeyExportModal(
    maskedKey: String,
    onDismiss: () -> Unit
) {
    var pinInput by remember { mutableStateOf("") }
    var isUnlocked by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val fullKey = "0x4a9b8f1c8e9d2a34bb1c82098d6174a88f01b5c2d3e4f5a6b7c8d9e0f1a2b3c4"
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = AlertRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Xuất Khóa Riêng Tư (Private Key)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!isUnlocked) {
                    Text(
                        text = "Vui lòng nhập mã PIN bảo mật (1234) để giải mã khóa riêng tư AES-256:",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { if (it.length <= 4) pinInput = it },
                        label = { Text("Mã PIN (4 số)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (error != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = error ?: "", color = AlertRed, fontSize = 11.sp)
                    }
                } else {
                    Surface(
                        color = Color(0xFF0F172A),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Khóa bí mật cấp độ Hex Ed25519:", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = fullKey,
                                fontSize = 12.sp,
                                color = CryptoGold,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(fullKey))
                            Toast.makeText(context, "Đã sao chép Private Key!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sao chép Private Key")
                    }
                }
            }
        },
        confirmButton = {
            if (!isUnlocked) {
                Button(
                    onClick = {
                        if (pinInput == "1234") {
                            isUnlocked = true
                            error = null
                        } else {
                            error = "Mã PIN không chính xác"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Giải mã", color = Color(0xFF041E28), fontWeight = FontWeight.Bold)
                }
            } else {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))) {
                    Text("Xong", color = TextPrimary)
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Hủy", color = TextSecondary)
            }
        }
    )
}

@Composable
fun ChangePinModal(
    onPinChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đổi Mã PIN Bảo Mật", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Thiết lập mã PIN 4 số mới để bảo vệ ví và máy đào:", fontSize = 13.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 4) newPin = it },
                    label = { Text("Mã PIN mới (4 số)") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = confirmPin,
                    onValueChange = { if (it.length <= 4) confirmPin = it },
                    label = { Text("Xác nhận mã PIN") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
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
                    if (newPin.length != 4) {
                        error = "Mã PIN phải đủ 4 chữ số"
                    } else if (newPin != confirmPin) {
                        error = "Mã PIN xác nhận không khớp"
                    } else {
                        onPinChanged(newPin)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Lưu mã PIN", color = Color(0xFF041E28), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Hủy", color = TextSecondary)
            }
        }
    )
}

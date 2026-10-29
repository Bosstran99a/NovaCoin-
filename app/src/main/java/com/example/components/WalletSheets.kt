package com.example.components

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CryptoAsset
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Modal rút tiền an toàn về ví bên ngoài (Binance, MetaMask, Trust Wallet...)
 * Có các lớp bảo mật: Kiểm tra địa chỉ ví, Phí Gas, Mã PIN, Chữ ký sinh trắc học
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawExternalModal(
    assets: List<CryptoAsset>,
    onWithdraw: (symbol: String, address: String, network: String, amount: Double, pin: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSymbol by remember { mutableStateOf("NVC") }
    var selectedNetwork by remember { mutableStateOf("NovaChain Mainnet") }
    var addressInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var isBiometricVerified by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expandedAssetMenu by remember { mutableStateOf(false) }
    var expandedNetworkMenu by remember { mutableStateOf(false) }

    val currentAsset = assets.find { it.symbol == selectedSymbol } ?: assets.first()
    val networks = when (selectedSymbol) {
        "NVC" -> listOf("NovaChain Mainnet (Khuyên dùng)", "BNB Smart Chain (BEP-20)")
        "BTC" -> listOf("Bitcoin Native", "Lightning Network")
        "ETH" -> listOf("Ethereum ERC-20", "Arbitrum One")
        else -> listOf("Tron TRC-20", "BNB Smart Chain (BEP-20)", "Ethereum ERC-20")
    }

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
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Rút Tiền Về Ví Ngoài", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Bảo mật đa lớp 2FA & PIN", fontSize = 11.sp, color = NeonEmerald)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Asset selector
                Text("Tài sản rút:", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedAssetMenu,
                    onExpandedChange = { expandedAssetMenu = !expandedAssetMenu }
                ) {
                    OutlinedTextField(
                        value = "${currentAsset.symbol} (Khả dụng: ${"%.4f".format(currentAsset.balance)})",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAssetMenu) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAssetMenu,
                        onDismissRequest = { expandedAssetMenu = false },
                        modifier = Modifier.background(CyberCardBg)
                    ) {
                        assets.forEach { a ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(a.symbol, fontWeight = FontWeight.Bold, color = NeonCyan)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${"%.4f".format(a.balance)}", color = TextPrimary)
                                    }
                                },
                                onClick = {
                                    selectedSymbol = a.symbol
                                    selectedNetwork = if (a.symbol == "NVC") "NovaChain Mainnet (Khuyên dùng)" else "Mạng chuẩn"
                                    expandedAssetMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Network Selector
                Text("Mạng lưới blockchain:", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedNetworkMenu,
                    onExpandedChange = { expandedNetworkMenu = !expandedNetworkMenu }
                ) {
                    OutlinedTextField(
                        value = selectedNetwork,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNetworkMenu) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedNetworkMenu,
                        onDismissRequest = { expandedNetworkMenu = false },
                        modifier = Modifier.background(CyberCardBg)
                    ) {
                        networks.forEach { net ->
                            DropdownMenuItem(
                                text = { Text(net, color = TextPrimary, fontSize = 13.sp) },
                                onClick = {
                                    selectedNetwork = net
                                    expandedNetworkMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Destination Address
                OutlinedTextField(
                    value = addressInput,
                    onValueChange = { addressInput = it },
                    label = { Text("Địa chỉ ví ngoài nhận tiền") },
                    placeholder = { Text("Ví dụ: 0x7F2a... hoặc TQ3p...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_address_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Amount
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Số lượng rút") },
                    placeholder = { Text("0.00") },
                    trailingIcon = {
                        Text(
                            text = "TỐI ĐA",
                            color = CryptoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable {
                                    val maxVal = (currentAsset.balance - 0.001).coerceAtLeast(0.0)
                                    amountInput = "%.4f".format(maxVal)
                                }
                                .padding(8.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_amount_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fee breakdown
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Phí Gas mạng lưới:", fontSize = 11.sp, color = TextMuted)
                        Text("0.001 $selectedSymbol", fontSize = 11.sp, color = CryptoGold, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Security Verification Section
                Text("Xác minh bảo mật bắt buộc:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4) pinInput = it },
                    label = { Text("Mã PIN bảo mật 4 số (1234)") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonEmerald,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_pin_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Simulated Biometric verification button
                Surface(
                    color = if (isBiometricVerified) Color(0xFF064E3B) else Color(0xFF1E293B),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isBiometricVerified) NeonEmerald else CyberBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isBiometricVerified = !isBiometricVerified }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBiometricVerified) Icons.Default.CheckCircle else Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = if (isBiometricVerified) NeonEmerald else NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isBiometricVerified) "Đã xác thực sinh trắc học vân tay an toàn" else "Nhấn để xác minh sinh trắc học (Khuyên dùng)",
                            fontSize = 11.sp,
                            color = if (isBiometricVerified) NeonEmerald else TextSecondary
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage ?: "", color = AlertRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountInput.toDoubleOrNull()
                    if (addressInput.isBlank() || addressInput.length < 10) {
                        errorMessage = "Vui lòng nhập địa chỉ ví nhận hợp lệ"
                    } else if (amount == null || amount <= 0.0) {
                        errorMessage = "Vui lòng nhập số lượng muốn rút"
                    } else if (amount + 0.001 > currentAsset.balance) {
                        errorMessage = "Số dư không đủ chi trả số lượng và phí mạng"
                    } else if (pinInput.length != 4) {
                        errorMessage = "Vui lòng nhập mã PIN bảo mật 4 số"
                    } else {
                        onWithdraw(selectedSymbol, addressInput, selectedNetwork, amount, pinInput)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_withdraw_button")
            ) {
                Text("Xác Nhận Rút Tiền", color = Color(0xFF041E28), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Hủy", color = TextSecondary)
            }
        }
    )
}

/**
 * Modal chuyển coin nội bộ giữa các người dùng trong ứng dụng
 * Không tốn phí Gas, tốc độ tức thì qua hệ thống P2P nội bộ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferInAppModal(
    assets: List<CryptoAsset>,
    onTransfer: (symbol: String, username: String, amount: Double, pin: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSymbol by remember { mutableStateOf("NVC") }
    var usernameInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expandedAssetMenu by remember { mutableStateOf(false) }

    val currentAsset = assets.find { it.symbol == selectedSymbol } ?: assets.first()

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
                        .background(Color(0xFF064E3B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Chuyển Nội Bộ Trong Ứng Dụng", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text("Miễn phí 100% • Tức thì P2P", fontSize = 11.sp, color = NeonEmerald)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Asset selector
                Text("Loại coin chuyển:", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedAssetMenu,
                    onExpandedChange = { expandedAssetMenu = !expandedAssetMenu }
                ) {
                    OutlinedTextField(
                        value = "${currentAsset.symbol} - Khả dụng: ${"%.4f".format(currentAsset.balance)}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAssetMenu) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAssetMenu,
                        onDismissRequest = { expandedAssetMenu = false },
                        modifier = Modifier.background(CyberCardBg)
                    ) {
                        assets.forEach { a ->
                            DropdownMenuItem(
                                text = { Text("${a.symbol} (${"%.4f".format(a.balance)})", color = TextPrimary) },
                                onClick = {
                                    selectedSymbol = a.symbol
                                    expandedAssetMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Recipient username / Phone / ID
                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    label = { Text("Tên người dùng / ID người nhận") },
                    placeholder = { Text("@crypto_viet_vn") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("transfer_inapp_username_input")
                )

                // Quick suggestions
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("hoang_miner", "alex_99", "crypto_viet_vn").forEach { suggestion ->
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { usernameInput = suggestion }
                        ) {
                            Text(
                                text = "@$suggestion",
                                fontSize = 10.sp,
                                color = NeonCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Amount
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Số lượng chuyển") },
                    placeholder = { Text("0.00") },
                    trailingIcon = {
                        Text(
                            text = "TỐI ĐA",
                            color = CryptoGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable { amountInput = currentAsset.balance.toString() }
                                .padding(8.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("transfer_inapp_amount_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // PIN verification
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4) pinInput = it },
                    label = { Text("Mã PIN xác thực (1234)") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonEmerald,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("transfer_inapp_pin_input")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = errorMessage ?: "", color = AlertRed, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountInput.toDoubleOrNull()
                    if (usernameInput.isBlank()) {
                        errorMessage = "Vui lòng nhập tên người dùng người nhận"
                    } else if (amount == null || amount <= 0.0) {
                        errorMessage = "Vui lòng nhập số lượng hợp lệ"
                    } else if (amount > currentAsset.balance) {
                        errorMessage = "Số dư không đủ"
                    } else if (pinInput.length != 4) {
                        errorMessage = "Vui lòng nhập mã PIN bảo mật"
                    } else {
                        onTransfer(selectedSymbol, usernameInput, amount, pinInput)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_transfer_inapp_button")
            ) {
                Text("Chuyển Ngay (Miễn Phí)", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Hủy", color = TextSecondary)
            }
        }
    )
}

/**
 * Modal hiển thị mã QR & Địa chỉ ví để nhận coin
 */
@Composable
fun ReceiveCryptoModal(
    walletAddress: String,
    onSimulateReceive: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF064E3B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Nhận Coin Vào Ví", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Quét mã QR hoặc sao chép địa chỉ công khai để nhận NovaCoin (NVC), Bitcoin, hoặc USDT:",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // High-tech Stylized QR Code
                Box(
                    modifier = Modifier
                        .size(175.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(3.dp, NeonCyan, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val gridSize = 19
                        val cellSize = size.width / gridSize
                        val darkColor = Color(0xFF0F172A)

                        fun drawFinder(top: Float, left: Float) {
                            drawRect(
                                color = darkColor,
                                topLeft = Offset(left * cellSize, top * cellSize),
                                size = Size(7 * cellSize, 7 * cellSize)
                            )
                            drawRect(
                                color = Color.White,
                                topLeft = Offset((left + 1) * cellSize, (top + 1) * cellSize),
                                size = Size(5 * cellSize, 5 * cellSize)
                            )
                            drawRect(
                                color = darkColor,
                                topLeft = Offset((left + 2) * cellSize, (top + 2) * cellSize),
                                size = Size(3 * cellSize, 3 * cellSize)
                            )
                        }

                        drawFinder(0f, 0f)
                        drawFinder(0f, (gridSize - 7).toFloat())
                        drawFinder((gridSize - 7).toFloat(), 0f)

                        for (r in 0 until gridSize) {
                            for (c in 0 until gridSize) {
                                val isFinder = (r < 7 && c < 7) || (r < 7 && c >= gridSize - 7) || (r >= gridSize - 7 && c < 7)
                                if (!isFinder) {
                                    if ((r * 7 + c * 13 + (r * c)) % 3 == 0) {
                                        drawRect(
                                            color = darkColor,
                                            topLeft = Offset(c * cellSize + 1f, r * cellSize + 1f),
                                            size = Size(cellSize - 2f, cellSize - 2f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Public Address Card
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "Địa chỉ ví công khai:", fontSize = 10.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = walletAddress,
                            fontSize = 11.sp,
                            color = NeonCyan,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(walletAddress))
                        Toast.makeText(context, "Đã sao chép địa chỉ ví!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Sao chép địa chỉ ví", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Test simulation button
                Text(
                    text = "Nhấn để thử nhận nạp +10 NVC vào ví",
                    fontSize = 11.sp,
                    color = CryptoGold,
                    modifier = Modifier
                        .clickable {
                            onSimulateReceive(10.0)
                        }
                        .padding(4.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đóng", color = Color(0xFF022C22), fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Modal nhập mã giới thiệu của bạn bè để nhận thưởng chào mừng
 */
@Composable
fun EnterReferralCodeModal(
    onApplyCode: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberCardBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = CryptoGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nhập Mã Giới Thiệu", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = Color(0xFF064E3B),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CryptoGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Khi nhập mã, CẢ HAI sẽ nhận ngay +5.0 NVC thưởng chào mừng và +25% tốc độ đào trong 72 giờ!",
                            fontSize = 12.sp,
                            color = NeonEmerald
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it.uppercase() },
                    label = { Text("Mã giới thiệu (ví dụ: NOVA-7788)") },
                    placeholder = { Text("NOVA-7788") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("enter_referral_code_input")
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
                    if (codeInput.isBlank()) {
                        error = "Vui lòng nhập mã giới thiệu"
                    } else {
                        onApplyCode(codeInput)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CryptoGold),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("apply_referral_code_button")
            ) {
                Text("Nhận Thưởng Ngay", color = Color(0xFF2E2000), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp)) {
                Text("Đóng", color = TextSecondary)
            }
        }
    )
}

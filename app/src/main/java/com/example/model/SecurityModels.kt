package com.example.model

enum class AuditSeverity {
    INFO,
    SECURE,
    WARNING
}

data class SecurityAuditItem(
    val id: String,
    val title: String,
    val detail: String,
    val timeAgo: String,
    val ipAddress: String,
    val severity: AuditSeverity
)

data class SecurityProfile(
    val isPinConfigured: Boolean = true,
    val pinCode: String = "1234", // Default initial PIN for easy testing
    val isBiometricEnabled: Boolean = true,
    val isSeedPhraseBackedUp: Boolean = false,
    val autoLockDurationSeconds: Int = 300, // 5 mins
    val antiPhishingPhrase: String = "NOVA-SHIELD-99",
    val walletPublicKey: String = "0x7F2a91C840bE15993C19Fe4dAe542C128bC89D42",
    val walletPrivateKeyMasked: String = "0x4a9b...7f1c8e9d2a34bb1c82098...SECURE_KEY",
    val seedWords: List<String> = listOf(
        "orbit", "matrix", "crypto", "cipher",
        "nebula", "quantum", "secure", "shield",
        "pulse", "vault", "galaxy", "forge"
    ),
    val auditHistory: List<SecurityAuditItem> = listOf(
        SecurityAuditItem(
            id = "log_1",
            title = "Xác thực sinh trắc học thành công",
            detail = "Truy cập từ thiết bị an toàn (Knox & Keystore hợp lệ)",
            timeAgo = "2 phút trước",
            ipAddress = "113.161.42.18 (VN)",
            severity = AuditSeverity.SECURE
        ),
        SecurityAuditItem(
            id = "log_2",
            title = "Ký khối Block #842,109",
            detail = "Chữ ký mật mã Ed25519 cho phần thưởng khai thác",
            timeAgo = "18 phút trước",
            ipAddress = "Stratum Node VN-02",
            severity = AuditSeverity.INFO
        ),
        SecurityAuditItem(
            id = "log_3",
            title = "Kích hoạt mã hoá ví AES-256-GCM",
            detail = "Khóa ví lưu trữ an toàn trong Android Keystore phần cứng",
            timeAgo = "Hôm qua",
            ipAddress = "Thiết bị nội bộ",
            severity = AuditSeverity.SECURE
        )
    )
) {
    val securityScore: Int
        get() {
            var score = 40
            if (isPinConfigured) score += 20
            if (isBiometricEnabled) score += 20
            if (isSeedPhraseBackedUp) score += 20
            return score.coerceIn(0, 100)
        }

    val securityStatusLabel: String
        get() = when {
            securityScore >= 90 -> "Cực kỳ an toàn (Hạng AAA)"
            securityScore >= 70 -> "Bảo mật cao"
            securityScore >= 50 -> "Bảo mật trung bình"
            else -> "Cần hoàn thiện sao lưu"
        }
}

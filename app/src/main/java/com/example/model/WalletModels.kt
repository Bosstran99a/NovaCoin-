package com.example.model

enum class TransactionType {
    MINED_REWARD,
    RECEIVED,
    SENT,
    RIG_PURCHASE,
    AIRDROP_BONUS
}

enum class TransactionStatus {
    CONFIRMED,
    PENDING,
    FAILED
}

data class CryptoAsset(
    val symbol: String,
    val name: String,
    val balance: Double,
    val priceUsd: Double,
    val change24hPercent: Double,
    val network: String,
    val hexColor: Long
) {
    val balanceValueUsd: Double
        get() = balance * priceUsd

    val balanceValueVnd: Double
        get() = balanceValueUsd * 25450.0
}

data class CryptoTransaction(
    val id: String,
    val type: TransactionType,
    val symbol: String,
    val amount: Double,
    val timestamp: Long,
    val dateString: String,
    val status: TransactionStatus,
    val address: String,
    val txHash: String,
    val feeNvc: Double = 0.0005,
    val confirmations: Int = 12
)

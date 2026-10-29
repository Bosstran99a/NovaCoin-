package com.example.model

/**
 * Mining engine data structures and status models
 */
data class MinerStatus(
    val isMining: Boolean = true,
    val hashrateMhs: Double = 48.6,
    val baseHashrateMhs: Double = 42.0,
    val boostMultiplier: Double = 1.15,
    val totalMinedNvc: Double = 34.821950,
    val uncollectedSessionNvc: Double = 1.482015,
    val sessionRemainingSeconds: Long = 71240L, // 24h cycle
    val totalSessionSeconds: Long = 86400L,
    val algorithmName: String = "SHA-256 Quantum Node",
    val efficiencyPercent: Int = 99,
    val activeRigName: String = "Cyber ASIC Node v2",
    val activeRigLevel: Int = 2,
    val pingMs: Int = 24
)

data class MiningRig(
    val id: String,
    val name: String,
    val tierTitle: String,
    val hashrateMhs: Double,
    val costNvc: Double,
    val powerConsumptionWatts: Int,
    val coolingEfficiency: String,
    val isUnlocked: Boolean,
    val isEquipped: Boolean,
    val description: String,
    val algorithm: String
)

data class MinedBlock(
    val blockHeight: Long,
    val blockHash: String,
    val nonce: Long,
    val rewardNvc: Double,
    val minerNodeId: String,
    val timestampFormatted: String,
    val difficulty: String,
    val isUserBlock: Boolean = false
)

data class MiningPoolStats(
    val poolName: String = "Nova Global Stratum Pool",
    val totalNetworkHashrateThs: Double = 18.4,
    val activeNodesWorldwide: Int = 612840,
    val poolFeePercent: Double = 0.5,
    val halvingDaysRemaining: Int = 138,
    val currentEpochRewardNvc: Double = 2.50,
    val userRankInPool: Int = 1420
)

data class MiningQuest(
    val id: String,
    val title: String,
    val description: String,
    val rewardNvc: Double,
    val progress: Float,
    val isCompleted: Boolean,
    val isClaimed: Boolean
)

data class HashratePoint(
    val timeLabel: String,
    val hashrateMhs: Double,
    val validShares: Int = 120,
    val efficiency: Float = 0.99f
)


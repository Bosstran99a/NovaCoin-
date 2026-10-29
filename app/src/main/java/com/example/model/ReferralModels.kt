package com.example.model

enum class MemberMiningStatus {
    ACTIVE_MINING,
    IDLE
}

data class ReferredMember(
    val id: String,
    val username: String,
    val avatarInitials: String,
    val joinedDateString: String,
    val status: MemberMiningStatus,
    val contributedHashrateMhs: Double,
    val totalBonusNvcGenerated: Double
)

enum class ReferralTier(
    val title: String,
    val minReferrals: Int,
    val bonusPerReferralNvc: Double,
    val boostPercentPerMember: Double,
    val badgeColorHex: Long
) {
    BRONZE("Thợ Đào Tân Thủ", 0, 5.0, 15.0, 0xFFCD7F32),
    SILVER("Đội Trưởng Mỏ Đào", 3, 7.5, 20.0, 0xFFC0C0C0),
    GOLD("Chỉ Huy Mạng Lưới", 6, 10.0, 25.0, 0xFFFFD700),
    DIAMOND("Đại Sứ Kim Cương", 12, 15.0, 30.0, 0xFF00E5FF)
}

data class ReferralSystemState(
    val myReferralCode: String = "NOVA-8899",
    val referralLink: String = "https://novamine.network/join?ref=NOVA-8899",
    val isInviterCodeApplied: Boolean = false,
    val appliedInviterCode: String? = null,
    val activeBoostPercent: Double = 35.0, // e.g. +35% hashrate boost from team
    val boostRemainingSeconds: Long = 172800L, // 48 hours countdown
    val totalBonusEarnedNvc: Double = 15.0,
    val members: List<ReferredMember> = listOf(
        ReferredMember(
            id = "mem_1",
            username = "alex_miner_99",
            avatarInitials = "AM",
            joinedDateString = "2 ngày trước",
            status = MemberMiningStatus.ACTIVE_MINING,
            contributedHashrateMhs = 12.0,
            totalBonusNvcGenerated = 7.5
        ),
        ReferredMember(
            id = "mem_2",
            username = "crypto_viet_vn",
            avatarInitials = "CV",
            joinedDateString = "Hôm qua",
            status = MemberMiningStatus.ACTIVE_MINING,
            contributedHashrateMhs = 14.5,
            totalBonusNvcGenerated = 7.5
        ),
        ReferredMember(
            id = "mem_3",
            username = "blockchain_pro",
            avatarInitials = "BP",
            joinedDateString = "3 giờ trước",
            status = MemberMiningStatus.ACTIVE_MINING,
            contributedHashrateMhs = 10.0,
            totalBonusNvcGenerated = 5.0
        )
    )
) {
    val activeMembersCount: Int
        get() = members.count { it.status == MemberMiningStatus.ACTIVE_MINING }

    val totalMembersCount: Int
        get() = members.size

    val currentTier: ReferralTier
        get() = when {
            totalMembersCount >= ReferralTier.DIAMOND.minReferrals -> ReferralTier.DIAMOND
            totalMembersCount >= ReferralTier.GOLD.minReferrals -> ReferralTier.GOLD
            totalMembersCount >= ReferralTier.SILVER.minReferrals -> ReferralTier.SILVER
            else -> ReferralTier.BRONZE
        }
}

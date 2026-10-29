package com.example.data

import com.example.model.AuditSeverity
import com.example.model.CryptoAsset
import com.example.model.CryptoTransaction
import com.example.model.HashratePoint
import com.example.model.MemberMiningStatus
import com.example.model.MinedBlock
import com.example.model.MinerStatus
import com.example.model.MiningPoolStats
import com.example.model.MiningQuest
import com.example.model.MiningRig
import com.example.model.ReferralSystemState
import com.example.model.ReferredMember
import com.example.model.SecurityAuditItem
import com.example.model.SecurityProfile
import com.example.model.TransactionStatus
import com.example.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class CryptoRepository {

    private val _minerStatus = MutableStateFlow(MinerStatus())
    val minerStatus: StateFlow<MinerStatus> = _minerStatus.asStateFlow()

    private val _hashrateHistory = MutableStateFlow(
        listOf(
            HashratePoint("00:00", 42.5, 118, 0.99f),
            HashratePoint("02:00", 43.8, 122, 0.99f),
            HashratePoint("04:00", 41.2, 115, 0.98f),
            HashratePoint("06:00", 45.6, 128, 1.00f),
            HashratePoint("08:00", 48.0, 134, 0.99f),
            HashratePoint("10:00", 52.4, 145, 0.99f),
            HashratePoint("12:00", 56.8, 158, 1.00f),
            HashratePoint("14:00", 54.2, 151, 0.99f),
            HashratePoint("16:00", 63.5, 178, 1.00f),
            HashratePoint("18:00", 59.0, 165, 0.99f),
            HashratePoint("20:00", 64.8, 182, 1.00f),
            HashratePoint("22:00", 61.2, 171, 0.99f),
            HashratePoint("Bây giờ", 48.6, 136, 0.99f)
        )
    )
    val hashrateHistory: StateFlow<List<HashratePoint>> = _hashrateHistory.asStateFlow()

    private val _assets = MutableStateFlow(
        listOf(
            CryptoAsset(
                symbol = "NVC",
                name = "NovaCoin (Đang đào)",
                balance = 54.821950,
                priceUsd = 1.48,
                change24hPercent = +12.65,
                network = "NovaChain Mainnet",
                hexColor = 0xFF00E5FF
            ),
            CryptoAsset(
                symbol = "BTC",
                name = "Bitcoin",
                balance = 0.005200,
                priceUsd = 67850.00,
                change24hPercent = +3.42,
                network = "Bitcoin Native",
                hexColor = 0xFFFFB800
            ),
            CryptoAsset(
                symbol = "ETH",
                name = "Ethereum",
                balance = 0.098000,
                priceUsd = 3420.50,
                change24hPercent = -1.18,
                network = "Ethereum ERC-20",
                hexColor = 0xFF627EEA
            ),
            CryptoAsset(
                symbol = "USDT",
                name = "Tether USD",
                balance = 165.50,
                priceUsd = 1.00,
                change24hPercent = +0.02,
                network = "Tron TRC-20",
                hexColor = 0xFF26A17B
            )
        )
    )
    val assets: StateFlow<List<CryptoAsset>> = _assets.asStateFlow()

    private val _transactions = MutableStateFlow(
        listOf(
            CryptoTransaction(
                id = "tx_1",
                type = TransactionType.MINED_REWARD,
                symbol = "NVC",
                amount = 2.50,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 18,
                dateString = "Hôm nay, 18 phút trước",
                status = TransactionStatus.CONFIRMED,
                address = "NovaChain Pool Node #02",
                txHash = "0x8fa4e21b7a...b91c04f"
            ),
            CryptoTransaction(
                id = "tx_ref_bonus",
                type = TransactionType.AIRDROP_BONUS,
                symbol = "NVC",
                amount = 5.00,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                dateString = "Hôm nay, 2 giờ trước",
                status = TransactionStatus.CONFIRMED,
                address = "Thưởng giới thiệu bạn bè: @crypto_viet_vn",
                txHash = "0x4fe19a8b...77d29a"
            ),
            CryptoTransaction(
                id = "tx_2",
                type = TransactionType.AIRDROP_BONUS,
                symbol = "NVC",
                amount = 5.00,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 4,
                dateString = "Hôm nay, 4 giờ trước",
                status = TransactionStatus.CONFIRMED,
                address = "Nova Genesis Welcome Vault",
                txHash = "0x3e12918a...77a82b"
            ),
            CryptoTransaction(
                id = "tx_3",
                type = TransactionType.RECEIVED,
                symbol = "USDT",
                amount = 50.00,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24,
                dateString = "Hôm qua, 14:22",
                status = TransactionStatus.CONFIRMED,
                address = "TQ3pW...k72B (Ví Binance)",
                txHash = "0x77c91a0...2d41ef"
            ),
            CryptoTransaction(
                id = "tx_4",
                type = TransactionType.SENT,
                symbol = "NVC",
                amount = 10.00,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 28,
                dateString = "Hôm qua, 10:15",
                status = TransactionStatus.CONFIRMED,
                address = "Chuyển nội bộ đến: @hoang_miner",
                txHash = "0x9812bc...3a401c"
            )
        )
    )
    val transactions: StateFlow<List<CryptoTransaction>> = _transactions.asStateFlow()

    private val _minedBlocks = MutableStateFlow(
        listOf(
            MinedBlock(
                blockHeight = 842109L,
                blockHash = "00000000000000000004f981cae18f23",
                nonce = 2841951023L,
                rewardNvc = 2.50,
                minerNodeId = "Nova-Mobile-Node #42 (Bạn)",
                timestampFormatted = "Vừa xong",
                difficulty = "18.42 T",
                isUserBlock = true
            ),
            MinedBlock(
                blockHeight = 842108L,
                blockHash = "000000000000000000072b842dfa8820",
                nonce = 1948271109L,
                rewardNvc = 2.50,
                minerNodeId = "Stratum-Rig-Asia-19",
                timestampFormatted = "3 phút trước",
                difficulty = "18.41 T"
            ),
            MinedBlock(
                blockHeight = 842107L,
                blockHash = "000000000000000000021c778fa9b341",
                nonce = 3381042918L,
                rewardNvc = 2.50,
                minerNodeId = "Nova-EU-Central-05",
                timestampFormatted = "7 phút trước",
                difficulty = "18.40 T"
            ),
            MinedBlock(
                blockHeight = 842106L,
                blockHash = "00000000000000000009da238ef1092a",
                nonce = 4109283719L,
                rewardNvc = 2.50,
                minerNodeId = "Hydro-Cluster-US-08",
                timestampFormatted = "12 phút trước",
                difficulty = "18.40 T"
            )
        )
    )
    val minedBlocks: StateFlow<List<MinedBlock>> = _minedBlocks.asStateFlow()

    private val _miningRigs = MutableStateFlow(
        listOf(
            MiningRig(
                id = "rig_micro",
                name = "Micro Cloud Node v1",
                tierTitle = "Cấp khởi đầu (Dùng thử)",
                hashrateMhs = 15.0,
                costUsdt = 0.0,
                powerConsumptionWatts = 5,
                coolingEfficiency = "Thụ động (Eco-Safe)",
                isUnlocked = true,
                isEquipped = true,
                description = "Node đám mây tối ưu hoá di động, không tiêu hao pin hoặc nhiệt độ máy.",
                algorithm = "SHA-256 Cloud"
            ),
            MiningRig(
                id = "rig_cyber_v2",
                name = "Cyber ASIC Node v2",
                tierTitle = "Mốc 20$ USDT (Tiêu chuẩn)",
                hashrateMhs = 65.0,
                costUsdt = 20.0,
                powerConsumptionWatts = 22,
                coolingEfficiency = "Tản nhiệt buồng khí AI",
                isUnlocked = false,
                isEquipped = false,
                description = "ASIC ảo hoá đám mây với bộ xử lý lượng tử kép, tốc độ đào ổn định x4.3 lần.",
                algorithm = "SHA-256 Quantum"
            ),
            MiningRig(
                id = "rig_quantum_hydro",
                name = "Quantum Hydro Cluster",
                tierTitle = "Mốc 100$ USDT (Chuyên nghiệp)",
                hashrateMhs = 180.0,
                costUsdt = 100.0,
                powerConsumptionWatts = 55,
                coolingEfficiency = "Làm mát chất lỏng tuần hoàn",
                isUnlocked = false,
                isEquipped = false,
                description = "Cụm máy đào công nghệ cao nối mạng trực tiếp tới trung tâm dữ liệu Frankfurt, tốc độ x12 lần.",
                algorithm = "Equihash Quantum"
            ),
            MiningRig(
                id = "rig_solar_titan",
                name = "Nova Solar Titan Rig",
                tierTitle = "Mốc 500$ USDT (Đẳng cấp cao)",
                hashrateMhs = 520.0,
                costUsdt = 500.0,
                powerConsumptionWatts = 95,
                coolingEfficiency = "Khí động học buồng áp suất",
                isUnlocked = false,
                isEquipped = false,
                description = "Siêu trạm khai thác năng lượng mặt trời công suất cực lớn, tối đa hoá phần thưởng khối mỗi ngày.",
                algorithm = "GhostRider Eco-Max"
            ),
            MiningRig(
                id = "rig_genesis_quantum",
                name = "Dark Matter Genesis Array",
                tierTitle = "Mốc 1000$ USDT (Tối thượng VIP)",
                hashrateMhs = 1450.0,
                costUsdt = 1000.0,
                powerConsumptionWatts = 160,
                coolingEfficiency = "Siêu dẫn nhiệt độ âm Cryo",
                isUnlocked = false,
                isEquipped = false,
                description = "Mạng lưới siêu cụm máy chủ lượng tử tối thượng, tốc độ đào x96 lần, ưu tiên xác thực khối Genesis.",
                algorithm = "Nova-Genesis Quantum Matrix"
            )
        )
    )
    val miningRigs: StateFlow<List<MiningRig>> = _miningRigs.asStateFlow()

    private val _poolStats = MutableStateFlow(MiningPoolStats())
    val poolStats: StateFlow<MiningPoolStats> = _poolStats.asStateFlow()

    private val _quests = MutableStateFlow(
        listOf(
            MiningQuest(
                id = "quest_1",
                title = "Duy trì phiên đào 24h",
                description = "Kích hoạt đào liên tục không gián đoạn trong 24 giờ",
                rewardNvc = 1.50,
                progress = 0.82f,
                isCompleted = false,
                isClaimed = false
            ),
            MiningQuest(
                id = "quest_2",
                title = "Sao lưu 12 cụm từ hạt giống",
                description = "Bảo vệ ví an toàn tuyệt đối với mã khoá khôi phục",
                rewardNvc = 3.00,
                progress = 0.0f,
                isCompleted = false,
                isClaimed = false
            ),
            MiningQuest(
                id = "quest_3",
                title = "Chạm tăng tốc Hashrate",
                description = "Chạm kích hoạt lõi năng lượng tăng tốc độ đào 100 lần",
                rewardNvc = 0.80,
                progress = 1.0f,
                isCompleted = true,
                isClaimed = false
            ),
            MiningQuest(
                id = "quest_4",
                title = "Mời 1 bạn cùng tham gia đội đào",
                description = "Mở rộng liên minh thợ đào tăng 25% tốc độ nhóm",
                rewardNvc = 5.00,
                progress = 1.0f,
                isCompleted = true,
                isClaimed = true
            )
        )
    )
    val quests: StateFlow<List<MiningQuest>> = _quests.asStateFlow()

    private val _securityProfile = MutableStateFlow(SecurityProfile())
    val securityProfile: StateFlow<SecurityProfile> = _securityProfile.asStateFlow()

    private val _referralState = MutableStateFlow(ReferralSystemState())
    val referralState: StateFlow<ReferralSystemState> = _referralState.asStateFlow()

    // 1-second pulse
    fun tickMiningPulse() {
        val current = _minerStatus.value
        if (!current.isMining) return

        val refBoostMultiplier = 1.0 + (_referralState.value.activeBoostPercent / 100.0)
        val jitter = Random.nextDouble(-1.2, 1.4)
        val currentRig = _miningRigs.value.find { it.isEquipped }
        val baseSpeed = currentRig?.hashrateMhs ?: current.baseHashrateMhs
        val totalCombinedMultiplier = current.boostMultiplier * refBoostMultiplier
        val newHashrate = (baseSpeed * totalCombinedMultiplier + jitter).coerceAtLeast(10.0)

        // Increment coin
        val coinEarned = (newHashrate / 50.0) * 0.000042
        val updatedSessionNvc = current.uncollectedSessionNvc + coinEarned
        val updatedTotalNvc = current.totalMinedNvc + coinEarned
        val remainingSec = (current.sessionRemainingSeconds - 1).coerceAtLeast(0L)

        _minerStatus.value = current.copy(
            hashrateMhs = (newHashrate * 100).toInt() / 100.0,
            uncollectedSessionNvc = updatedSessionNvc,
            totalMinedNvc = updatedTotalNvc,
            sessionRemainingSeconds = remainingSec,
            pingMs = Random.nextInt(20, 32)
        )

        // Sync NVC balance
        _assets.update { list ->
            list.map { if (it.symbol == "NVC") it.copy(balance = updatedTotalNvc) else it }
        }

        // Sync live point in hashrate history
        _hashrateHistory.update { list ->
            list.mapIndexed { index, point ->
                if (index == list.lastIndex) point.copy(hashrateMhs = (newHashrate * 10).toInt() / 10.0)
                else point
            }
        }

        // Countdown limited-time referral boost
        if (_referralState.value.boostRemainingSeconds > 0) {
            _referralState.update {
                it.copy(boostRemainingSeconds = (it.boostRemainingSeconds - 1).coerceAtLeast(0L))
            }
        }

        // Random chance of block discovery
        if (Random.nextInt(180) == 77) {
            simulateBlockMined(isUser = true)
        }
    }

    fun toggleMiningState() {
        val current = _minerStatus.value
        val nextState = !current.isMining
        _minerStatus.update {
            it.copy(
                isMining = nextState,
                hashrateMhs = if (nextState) it.baseHashrateMhs * it.boostMultiplier else 0.0
            )
        }
        addSecurityLog(
            title = if (nextState) "Bắt đầu chu trình đào Cloud" else "Tạm dừng máy đào",
            detail = if (nextState) "Kết nối thành công Stratum Pool bảo mật" else "Ngắt kết nối an toàn",
            severity = AuditSeverity.INFO
        )
    }

    fun tapBoostHashrate() {
        val current = _minerStatus.value
        val boosted = (current.boostMultiplier + 0.05).coerceAtMost(2.5)
        val instantReward = 0.0005
        _minerStatus.update {
            it.copy(
                boostMultiplier = boosted,
                uncollectedSessionNvc = it.uncollectedSessionNvc + instantReward,
                totalMinedNvc = it.totalMinedNvc + instantReward
            )
        }
    }

    fun renewSession() {
        _minerStatus.update {
            it.copy(
                sessionRemainingSeconds = 86400L,
                boostMultiplier = 1.20
            )
        }
        addSecurityLog(
            title = "Gia hạn phiên đào 24h",
            detail = "Xác thực danh tính nút bảo mật thành công",
            severity = AuditSeverity.SECURE
        )
    }

    fun claimSessionRewards() {
        val current = _minerStatus.value
        val amount = current.uncollectedSessionNvc
        if (amount <= 0.0) return

        val newTx = CryptoTransaction(
            id = "tx_" + UUID.randomUUID().toString().take(8),
            type = TransactionType.MINED_REWARD,
            symbol = "NVC",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Ví cá nhân đã ký xác thực",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )

        _transactions.update { listOf(newTx) + it }
        _minerStatus.update { it.copy(uncollectedSessionNvc = 0.0) }
    }

    fun equipRig(rigId: String): Boolean {
        val currentRigs = _miningRigs.value
        val targetRig = currentRigs.find { it.id == rigId } ?: return false
        if (!targetRig.isUnlocked) return false

        _miningRigs.value = currentRigs.map { it.copy(isEquipped = it.id == rigId) }
        _minerStatus.update {
            it.copy(
                baseHashrateMhs = targetRig.hashrateMhs,
                activeRigName = targetRig.name,
                algorithmName = targetRig.algorithm
            )
        }
        return true
    }

    fun purchaseRigWithUsdt(rigId: String): Boolean {
        val currentRigs = _miningRigs.value
        val target = currentRigs.find { it.id == rigId } ?: return false
        val currentUsdtAsset = _assets.value.find { it.symbol == "USDT" } ?: return false

        if (currentUsdtAsset.balance < target.costUsdt) return false

        val remainingUsdt = currentUsdtAsset.balance - target.costUsdt
        _assets.update { list ->
            list.map { if (it.symbol == "USDT") it.copy(balance = remainingUsdt) else it }
        }

        _miningRigs.value = currentRigs.map {
            if (it.id == rigId) it.copy(isUnlocked = true, isEquipped = true)
            else it.copy(isEquipped = false)
        }

        _minerStatus.update {
            it.copy(
                baseHashrateMhs = target.hashrateMhs,
                activeRigName = target.name,
                algorithmName = target.algorithm
            )
        }

        val tx = CryptoTransaction(
            id = "tx_rig_" + UUID.randomUUID().toString().take(6),
            type = TransactionType.RIG_PURCHASE,
            symbol = "USDT",
            amount = target.costUsdt,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Mở khóa dàn đào: ${target.name} (${target.costUsdt}$ USDT)",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }

        addSecurityLog(
            title = "Thanh toán ${target.costUsdt}$ USDT mở khóa ${target.name}",
            detail = "Tốc độ đào tăng vọt lên ${target.hashrateMhs} MH/s. Trừ ${target.costUsdt} USDT",
            severity = AuditSeverity.SECURE
        )
        return true
    }

    fun depositTestUsdt(amount: Double) {
        _assets.update { list ->
            list.map { if (it.symbol == "USDT") it.copy(balance = it.balance + amount) else it }
        }
        val tx = CryptoTransaction(
            id = "tx_usdt_" + UUID.randomUUID().toString().take(6),
            type = TransactionType.RECEIVED,
            symbol = "USDT",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Nạp USDT vào ví (Mạng TRC-20/BEP-20)",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }
        addSecurityLog(
            title = "Nạp thành công +${amount} USDT vào ví",
            detail = "Mạng TRC-20 xác thực thành công",
            severity = AuditSeverity.INFO
        )
    }

    // ================= WALLET: WITHDRAW & INTERNAL TRANSFER =================

    /**
     * Rút tiền an toàn đến địa chỉ ví bên ngoài (External Wallet Withdrawal)
     */
    fun withdrawToExternalWallet(
        symbol: String,
        externalAddress: String,
        network: String,
        amount: Double,
        feeAmount: Double = 0.001
    ): Boolean {
        val currentAssets = _assets.value
        val asset = currentAssets.find { it.symbol == symbol } ?: return false
        val totalDeduction = amount + feeAmount
        if (asset.balance < totalDeduction || amount <= 0.0) return false

        val newBalance = asset.balance - totalDeduction
        _assets.update { list ->
            list.map { if (it.symbol == symbol) it.copy(balance = newBalance) else it }
        }

        if (symbol == "NVC") {
            _minerStatus.update { it.copy(totalMinedNvc = newBalance) }
        }

        val txHash = "0x" + UUID.randomUUID().toString().replace("-", "")
        val tx = CryptoTransaction(
            id = "wd_" + UUID.randomUUID().toString().take(8),
            type = TransactionType.SENT,
            symbol = symbol,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Rút ví ngoài ($network): ${externalAddress.take(8)}...${externalAddress.takeLast(6)}",
            txHash = txHash,
            feeNvc = feeAmount
        )
        _transactions.update { listOf(tx) + it }

        addSecurityLog(
            title = "Rút $amount $symbol về ví ngoài an toàn",
            detail = "Mạng $network | Tx: ${txHash.take(14)}... | Đã xác thực 2FA",
            severity = AuditSeverity.WARNING
        )
        return true
    }

    /**
     * Chuyển coin nội bộ không tốn phí đến người dùng khác trong ứng dụng (In-app transfer)
     */
    fun transferInAppToUser(
        symbol: String,
        recipientUsername: String,
        amount: Double
    ): Boolean {
        val currentAssets = _assets.value
        val asset = currentAssets.find { it.symbol == symbol } ?: return false
        if (asset.balance < amount || amount <= 0.0) return false

        val newBalance = asset.balance - amount
        _assets.update { list ->
            list.map { if (it.symbol == symbol) it.copy(balance = newBalance) else it }
        }

        if (symbol == "NVC") {
            _minerStatus.update { it.copy(totalMinedNvc = newBalance) }
        }

        val tx = CryptoTransaction(
            id = "p2p_" + UUID.randomUUID().toString().take(8),
            type = TransactionType.SENT,
            symbol = symbol,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Chuyển nội bộ tới: @${recipientUsername.removePrefix("@")}",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16),
            feeNvc = 0.0 // Free internal transfer!
        )
        _transactions.update { listOf(tx) + it }

        addSecurityLog(
            title = "Chuyển nội bộ $amount $symbol đến @${recipientUsername.removePrefix("@")}",
            detail = "Giao dịch P2P tức thì không mất phí mạng lưới",
            severity = AuditSeverity.INFO
        )
        return true
    }

    fun receiveSimulatedCrypto(symbol: String, amount: Double) {
        _assets.update { list ->
            list.map { if (it.symbol == symbol) it.copy(balance = it.balance + amount) else it }
        }
        val tx = CryptoTransaction(
            id = "tx_in_" + UUID.randomUUID().toString().take(6),
            type = TransactionType.RECEIVED,
            symbol = symbol,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Nhận từ: 0x" + UUID.randomUUID().toString().replace("-", "").take(12),
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }
    }

    // ================= REFERRAL REWARD SYSTEM =================

    /**
     * Nhập mã người giới thiệu (áp dụng cho người được mời):
     * Cả người nhập mã và người giới thiệu đều nhận thưởng:
     * + 5.0 NVC thưởng chào mừng vào ví
     * + 25% tăng tốc độ đào trong 72 giờ
     */
    fun applyInviterCode(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        if (trimmed.isEmpty() || _referralState.value.isInviterCodeApplied) return false
        if (trimmed == _referralState.value.myReferralCode) return false // cannot refer self

        val bonusNvc = 5.0
        val bonusBoost = 25.0
        val bonusSeconds = 72 * 3600L // 72 hours limited time

        _referralState.update {
            it.copy(
                isInviterCodeApplied = true,
                appliedInviterCode = trimmed,
                activeBoostPercent = it.activeBoostPercent + bonusBoost,
                boostRemainingSeconds = it.boostRemainingSeconds + bonusSeconds,
                totalBonusEarnedNvc = it.totalBonusEarnedNvc + bonusNvc
            )
        }

        // Add 5.0 NVC to wallet
        val updatedNvc = _minerStatus.value.totalMinedNvc + bonusNvc
        _minerStatus.update { it.copy(totalMinedNvc = updatedNvc) }
        _assets.update { list ->
            list.map { if (it.symbol == "NVC") it.copy(balance = updatedNvc) else it }
        }

        val tx = CryptoTransaction(
            id = "tx_welcome_ref",
            type = TransactionType.AIRDROP_BONUS,
            symbol = "NVC",
            amount = bonusNvc,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Thưởng chào mừng nhập mã giới thiệu: $trimmed",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }

        addSecurityLog(
            title = "Áp dụng mã giới thiệu thành công: $trimmed",
            detail = "Cả bạn và người giới thiệu nhận +5 NVC và +25% tốc độ đào (72h)",
            severity = AuditSeverity.SECURE
        )
        return true
    }

    /**
     * Mô phỏng khi giới thiệu thành công 1 người dùng mới bắt đầu đào:
     * Cả 2 người nhận ngay +5.0 NVC và +25% boost tốc độ đào trong 48h!
     */
    fun simulateReferralSuccess(invitedUsername: String) {
        val cleanName = invitedUsername.ifBlank { "miner_partner_" + Random.nextInt(100, 999) }
        val initials = cleanName.take(2).uppercase()
        val bonusNvc = 5.0
        val bonusBoostPercent = 25.0
        val limitedDurationSeconds = 48 * 3600L

        val newMember = ReferredMember(
            id = "mem_" + UUID.randomUUID().toString().take(6),
            username = cleanName,
            avatarInitials = initials,
            joinedDateString = "Vừa xong",
            status = MemberMiningStatus.ACTIVE_MINING,
            contributedHashrateMhs = 12.5,
            totalBonusNvcGenerated = bonusNvc
        )

        _referralState.update {
            it.copy(
                members = listOf(newMember) + it.members,
                activeBoostPercent = it.activeBoostPercent + bonusBoostPercent,
                boostRemainingSeconds = it.boostRemainingSeconds + limitedDurationSeconds,
                totalBonusEarnedNvc = it.totalBonusEarnedNvc + bonusNvc
            )
        }

        // Add 5.0 NVC to user's wallet
        val updatedNvc = _minerStatus.value.totalMinedNvc + bonusNvc
        _minerStatus.update { it.copy(totalMinedNvc = updatedNvc) }
        _assets.update { list ->
            list.map { if (it.symbol == "NVC") it.copy(balance = updatedNvc) else it }
        }

        val tx = CryptoTransaction(
            id = "tx_ref_" + UUID.randomUUID().toString().take(6),
            type = TransactionType.AIRDROP_BONUS,
            symbol = "NVC",
            amount = bonusNvc,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Thưởng giới thiệu thợ đào mới: @$cleanName",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }

        addSecurityLog(
            title = "Người dùng mới @$cleanName đã kích hoạt máy đào!",
            detail = "Cả hai nhận ngay +5.0 NVC và +25% tốc độ đào trong 48 giờ",
            severity = AuditSeverity.SECURE
        )
    }

    fun markSeedPhraseBackedUp() {
        _securityProfile.update { it.copy(isSeedPhraseBackedUp = true) }
        _quests.update { list ->
            list.map { if (it.id == "quest_2") it.copy(progress = 1.0f, isCompleted = true) else it }
        }
        addSecurityLog(
            title = "Sao lưu 12 cụm từ hạt giống hoàn tất",
            detail = "Xác nhận kiểm tra đối chiếu từ khóa bảo mật thành công",
            severity = AuditSeverity.SECURE
        )
    }

    fun updatePinCode(newPin: String) {
        _securityProfile.update { it.copy(pinCode = newPin, isPinConfigured = true) }
        addSecurityLog(
            title = "Thay đổi mã PIN bảo mật",
            detail = "Mã PIN 4 số được mã hóa và lưu trữ an toàn",
            severity = AuditSeverity.SECURE
        )
    }

    fun toggleBiometrics(enabled: Boolean) {
        _securityProfile.update { it.copy(isBiometricEnabled = enabled) }
        addSecurityLog(
            title = if (enabled) "Bật mở khóa sinh trắc học" else "Tắt mở khóa sinh trắc học",
            detail = if (enabled) "Sử dụng vân tay / khuôn mặt để xác thực nhanh" else "Chỉ sử dụng mã PIN",
            severity = AuditSeverity.INFO
        )
    }

    fun claimQuest(questId: String) {
        val quest = _quests.value.find { it.id == questId } ?: return
        if (!quest.isCompleted || quest.isClaimed) return

        _quests.update { list ->
            list.map { if (it.id == questId) it.copy(isClaimed = true) else it }
        }

        val reward = quest.rewardNvc
        _minerStatus.update { it.copy(totalMinedNvc = it.totalMinedNvc + reward) }
        _assets.update { list ->
            list.map { if (it.symbol == "NVC") it.copy(balance = it.balance + reward) else it }
        }

        val tx = CryptoTransaction(
            id = "tx_quest_" + UUID.randomUUID().toString().take(6),
            type = TransactionType.AIRDROP_BONUS,
            symbol = "NVC",
            amount = reward,
            timestamp = System.currentTimeMillis(),
            dateString = "Vừa xong",
            status = TransactionStatus.CONFIRMED,
            address = "Thưởng nhiệm vụ NovaMine",
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        )
        _transactions.update { listOf(tx) + it }
    }

    private fun simulateBlockMined(isUser: Boolean) {
        val currentBlocks = _minedBlocks.value
        val topHeight = (currentBlocks.firstOrNull()?.blockHeight ?: 842109L) + 1
        val newBlock = MinedBlock(
            blockHeight = topHeight,
            blockHash = "0000000000000000000" + UUID.randomUUID().toString().replace("-", "").take(13),
            nonce = Random.nextLong(1000000000L, 9999999999L),
            rewardNvc = 2.50,
            minerNodeId = if (isUser) "Nova-Mobile-Node #42 (Bạn)" else "Stratum-Pool-Rig #${Random.nextInt(10, 99)}",
            timestampFormatted = "Vừa xong",
            difficulty = "18.42 T",
            isUserBlock = isUser
        )

        _minedBlocks.value = (listOf(newBlock) + currentBlocks).take(20)

        if (isUser) {
            val bonus = 2.50
            _minerStatus.update { it.copy(totalMinedNvc = it.totalMinedNvc + bonus) }
            _assets.update { list ->
                list.map { if (it.symbol == "NVC") it.copy(balance = it.balance + bonus) else it }
            }
            val tx = CryptoTransaction(
                id = "tx_blk_" + topHeight,
                type = TransactionType.MINED_REWARD,
                symbol = "NVC",
                amount = bonus,
                timestamp = System.currentTimeMillis(),
                dateString = "Vừa xong",
                status = TransactionStatus.CONFIRMED,
                address = "Phần thưởng khối Block #$topHeight",
                txHash = newBlock.blockHash
            )
            _transactions.update { listOf(tx) + it }
            addSecurityLog(
                title = "Khai thác thành công Block #$topHeight!",
                detail = "Nhận thưởng +2.50 NVC được phân phối về ví",
                severity = AuditSeverity.SECURE
            )
        }
    }

    fun addSecurityLog(title: String, detail: String, severity: AuditSeverity) {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val newItem = SecurityAuditItem(
            id = "log_" + UUID.randomUUID().toString().take(6),
            title = title,
            detail = detail,
            timeAgo = sdf.format(Date()),
            ipAddress = "113.161.42.18 (VN-Secure)",
            severity = severity
        )
        _securityProfile.update {
            it.copy(auditHistory = listOf(newItem) + it.auditHistory.take(15))
        }
    }
}

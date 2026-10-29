package com.example.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CryptoRepository
import com.example.model.AuditSeverity
import com.example.model.CryptoAsset
import com.example.model.CryptoTransaction
import com.example.model.HashratePoint
import com.example.model.MinedBlock
import com.example.model.MinerStatus
import com.example.model.MiningPoolStats
import com.example.model.MiningQuest
import com.example.model.MiningRig
import com.example.model.ReferralSystemState
import com.example.model.SecurityProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String) {
    MINING("Đào Coin"),
    WALLET("Ví Tiền"),
    REFERRAL("Giới Thiệu"),
    RIGS("Dàn Đào"),
    SECURITY("Bảo Mật")
}

enum class CurrencyMode {
    VND,
    USD
}

class CryptoMinerViewModel(
    private val repository: CryptoRepository = CryptoRepository()
) : ViewModel() {

    val minerStatus: StateFlow<MinerStatus> = repository.minerStatus
    val assets: StateFlow<List<CryptoAsset>> = repository.assets
    val transactions: StateFlow<List<CryptoTransaction>> = repository.transactions
    val minedBlocks: StateFlow<List<MinedBlock>> = repository.minedBlocks
    val miningRigs: StateFlow<List<MiningRig>> = repository.miningRigs
    val poolStats: StateFlow<MiningPoolStats> = repository.poolStats
    val quests: StateFlow<List<MiningQuest>> = repository.quests
    val securityProfile: StateFlow<SecurityProfile> = repository.securityProfile
    val referralState: StateFlow<ReferralSystemState> = repository.referralState
    val hashrateHistory: StateFlow<List<HashratePoint>> = repository.hashrateHistory

    // UI Tab State
    private val _currentTab = MutableStateFlow(AppNavTab.MINING)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _currencyMode = MutableStateFlow(CurrencyMode.VND)
    val currencyMode: StateFlow<CurrencyMode> = _currencyMode.asStateFlow()

    // App Pin / Lock State
    private val _isAppLocked = MutableStateFlow(false)
    val isAppLocked: StateFlow<Boolean> = _isAppLocked.asStateFlow()

    private val _pinEntered = MutableStateFlow("")
    val pinEntered: StateFlow<String> = _pinEntered.asStateFlow()

    private val _pinError = MutableStateFlow<String?>(null)
    val pinError: StateFlow<String?> = _pinError.asStateFlow()

    private val _isBiometricScanning = MutableStateFlow(false)
    val isBiometricScanning: StateFlow<Boolean> = _isBiometricScanning.asStateFlow()

    // Modals
    private val _showWithdrawDialog = MutableStateFlow(false)
    val showWithdrawDialog: StateFlow<Boolean> = _showWithdrawDialog.asStateFlow()

    private val _showTransferInAppDialog = MutableStateFlow(false)
    val showTransferInAppDialog: StateFlow<Boolean> = _showTransferInAppDialog.asStateFlow()

    private val _showReceiveDialog = MutableStateFlow(false)
    val showReceiveDialog: StateFlow<Boolean> = _showReceiveDialog.asStateFlow()

    private val _showSeedBackupDialog = MutableStateFlow(false)
    val showSeedBackupDialog: StateFlow<Boolean> = _showSeedBackupDialog.asStateFlow()

    private val _showPrivateKeyDialog = MutableStateFlow(false)
    val showPrivateKeyDialog: StateFlow<Boolean> = _showPrivateKeyDialog.asStateFlow()

    private val _showChangePinDialog = MutableStateFlow(false)
    val showChangePinDialog: StateFlow<Boolean> = _showChangePinDialog.asStateFlow()

    private val _showEnterReferralCodeDialog = MutableStateFlow(false)
    val showEnterReferralCodeDialog: StateFlow<Boolean> = _showEnterReferralCodeDialog.asStateFlow()

    private val _selectedRigForPurchase = MutableStateFlow<MiningRig?>(null)
    val selectedRigForPurchase: StateFlow<MiningRig?> = _selectedRigForPurchase.asStateFlow()

    private val _showRigPurchaseModal = MutableStateFlow(false)
    val showRigPurchaseModal: StateFlow<Boolean> = _showRigPurchaseModal.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Continuous 1-second pulse
        viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                repository.tickMiningPulse()
            }
        }
    }

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun toggleCurrency() {
        _currencyMode.update {
            if (it == CurrencyMode.VND) CurrencyMode.USD else CurrencyMode.VND
        }
    }

    fun toggleMining(context: Context? = null) {
        triggerHaptic(context, 40)
        repository.toggleMiningState()
    }

    fun tapBoostHashrate(context: Context? = null) {
        triggerHaptic(context, 25)
        repository.tapBoostHashrate()
    }

    fun renewSession(context: Context? = null) {
        triggerHaptic(context, 60)
        repository.renewSession()
        showToast("Đã gia hạn chu trình đào 24h thành công!")
    }

    fun claimSessionRewards(context: Context? = null) {
        triggerHaptic(context, 50)
        repository.claimSessionRewards()
        showToast("Đã chuyển phần thưởng vào số dư ví an toàn!")
    }

    fun equipRig(rigId: String, context: Context? = null) {
        triggerHaptic(context, 35)
        if (repository.equipRig(rigId)) {
            showToast("Đã trang bị dàn đào thành công!")
        }
    }

    fun openRigPurchaseModal(rig: MiningRig) {
        _selectedRigForPurchase.value = rig
        _showRigPurchaseModal.value = true
    }

    fun closeRigPurchaseModal() {
        _selectedRigForPurchase.value = null
        _showRigPurchaseModal.value = false
    }

    fun purchaseRigWithUsdt(rigId: String, pin: String, context: Context? = null): Boolean {
        if (pin != securityProfile.value.pinCode) {
            showToast("Mã PIN bảo mật không chính xác!")
            return false
        }
        triggerHaptic(context, 70)
        val success = repository.purchaseRigWithUsdt(rigId)
        if (success) {
            showToast("Chúc mừng! Đã mở khóa dàn đào bằng USDT thành công!")
            closeRigPurchaseModal()
        } else {
            showToast("Số dư USDT trong ví không đủ để mở khóa!")
        }
        return success
    }

    fun depositUsdt(amount: Double, context: Context? = null) {
        triggerHaptic(context, 40)
        repository.depositTestUsdt(amount)
        showToast("Đã nạp thành công +${amount.toInt()}$ USDT vào ví!")
    }

    // ================= WALLET ACTIONS =================

    fun withdrawToExternal(
        symbol: String,
        address: String,
        network: String,
        amount: Double,
        pin: String,
        context: Context? = null
    ): Boolean {
        if (pin != securityProfile.value.pinCode) {
            showToast("Mã PIN xác minh không chính xác!")
            return false
        }
        val success = repository.withdrawToExternalWallet(symbol, address, network, amount)
        if (success) {
            triggerHaptic(context, 60)
            showToast("Lệnh rút $amount $symbol về ví ngoài đã gửi thành công!")
            _showWithdrawDialog.value = false
        } else {
            showToast("Rút tiền thất bại: Vui lòng kiểm tra lại số dư và phí mạng!")
        }
        return success
    }

    fun transferInApp(
        symbol: String,
        username: String,
        amount: Double,
        pin: String,
        context: Context? = null
    ): Boolean {
        if (pin != securityProfile.value.pinCode) {
            showToast("Mã PIN xác minh không chính xác!")
            return false
        }
        val success = repository.transferInAppToUser(symbol, username, amount)
        if (success) {
            triggerHaptic(context, 50)
            showToast("Đã chuyển $amount $symbol đến @${username.removePrefix("@")} thành công!")
            _showTransferInAppDialog.value = false
        } else {
            showToast("Chuyển tiền thất bại: Số dư không đủ!")
        }
        return success
    }

    fun simulateReceive(symbol: String, amount: Double) {
        repository.receiveSimulatedCrypto(symbol, amount)
        showToast("Nhận thành công +$amount $symbol vào ví!")
    }

    // ================= REFERRAL ACTIONS =================

    fun applyInviterCode(code: String, context: Context? = null): Boolean {
        val success = repository.applyInviterCode(code)
        if (success) {
            triggerHaptic(context, 70)
            showToast("Tuyệt vời! Nhận ngay +5 NVC và +25% tốc độ đào trong 72 giờ!")
            _showEnterReferralCodeDialog.value = false
        } else {
            showToast("Mã không hợp lệ hoặc bạn đã nhập mã trước đó.")
        }
        return success
    }

    fun simulateNewReferredMember(context: Context? = null) {
        val names = listOf("nguyen_miner_84", "crypto_shark_vn", "hacker_coin_99", "blockchain_queen", "saigon_node_01")
        val chosenName = names.random()
        triggerHaptic(context, 75)
        repository.simulateReferralSuccess(chosenName)
        showToast("Thành viên @$chosenName vừa kích hoạt đào! Cả 2 nhận +5 NVC và +25% hashrate (48h)!")
    }

    fun shareReferralLink(context: Context) {
        val link = referralState.value.referralLink
        val code = referralState.value.myReferralCode
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Tham gia mạng lưới đào coin NovaMine cùng tôi! Nhập mã giới thiệu $code để nhận ngay +5.0 NVC thưởng chào mừng và +25% tốc độ đào: $link"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ mã giới thiệu NovaMine")
        context.startActivity(shareIntent)
    }

    // ================= SECURITY & PIN ACTIONS =================

    fun lockApp() {
        _isAppLocked.value = true
        _pinEntered.value = ""
        _pinError.value = null
    }

    fun enterPinDigit(digit: String, context: Context? = null) {
        triggerHaptic(context, 15)
        if (_pinEntered.value.length < 4) {
            val updated = _pinEntered.value + digit
            _pinEntered.value = updated
            if (updated.length == 4) {
                verifyPin(updated, context)
            }
        }
    }

    fun deletePinDigit(context: Context? = null) {
        triggerHaptic(context, 15)
        if (_pinEntered.value.isNotEmpty()) {
            _pinEntered.value = _pinEntered.value.dropLast(1)
            _pinError.value = null
        }
    }

    private fun verifyPin(pin: String, context: Context? = null) {
        val correctPin = securityProfile.value.pinCode
        if (pin == correctPin) {
            triggerHaptic(context, 60)
            _isAppLocked.value = false
            _pinEntered.value = ""
            _pinError.value = null
            showToast("Xác thực bảo mật thành công!")
        } else {
            triggerHaptic(context, 120)
            _pinError.value = "Mã PIN không chính xác. Thử lại (Mặc định: 1234)"
            _pinEntered.value = ""
        }
    }

    fun triggerBiometricAuth(context: Context? = null) {
        viewModelScope.launch {
            _isBiometricScanning.value = true
            triggerHaptic(context, 30)
            delay(800L)
            triggerHaptic(context, 60)
            _isBiometricScanning.value = false
            _isAppLocked.value = false
            _pinEntered.value = ""
            _pinError.value = null
            showToast("Xác thực vân tay sinh trắc học thành công!")
        }
    }

    fun updatePin(newPin: String) {
        repository.updatePinCode(newPin)
        _showChangePinDialog.value = false
        showToast("Đã cập nhật mã PIN mới thành công!")
    }

    fun toggleBiometrics(enabled: Boolean) {
        repository.toggleBiometrics(enabled)
        showToast(if (enabled) "Đã bật sinh trắc học vân tay" else "Đã tắt sinh trắc học")
    }

    fun markSeedPhraseBackedUp() {
        repository.markSeedPhraseBackedUp()
        _showSeedBackupDialog.value = false
        showToast("Đã sao lưu 12 từ khóa hạt giống an toàn (+3 NVC)!")
    }

    fun claimQuest(questId: String, context: Context? = null) {
        triggerHaptic(context, 45)
        repository.claimQuest(questId)
        showToast("Đã nhận thưởng nhiệm vụ thành công!")
    }

    // Modal triggers
    fun openWithdrawDialog() { _showWithdrawDialog.value = true }
    fun closeWithdrawDialog() { _showWithdrawDialog.value = false }
    fun openTransferInAppDialog() { _showTransferInAppDialog.value = true }
    fun closeTransferInAppDialog() { _showTransferInAppDialog.value = false }
    fun openReceiveDialog() { _showReceiveDialog.value = true }
    fun closeReceiveDialog() { _showReceiveDialog.value = false }
    fun openSeedBackupDialog() { _showSeedBackupDialog.value = true }
    fun closeSeedBackupDialog() { _showSeedBackupDialog.value = false }
    fun openPrivateKeyDialog() { _showPrivateKeyDialog.value = true }
    fun closePrivateKeyDialog() { _showPrivateKeyDialog.value = false }
    fun openChangePinDialog() { _showChangePinDialog.value = true }
    fun closeChangePinDialog() { _showChangePinDialog.value = false }
    fun openEnterReferralCodeDialog() { _showEnterReferralCodeDialog.value = true }
    fun closeEnterReferralCodeDialog() { _showEnterReferralCodeDialog.value = false }

    fun showToast(msg: String) { _toastMessage.value = msg }
    fun clearToast() { _toastMessage.value = null }

    private fun triggerHaptic(context: Context?, durationMs: Long) {
        if (context == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }
}

package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.components.ChangePinModal
import com.example.components.EnterReferralCodeModal
import com.example.components.PinSecurityLockScreen
import com.example.components.PrivateKeyExportModal
import com.example.components.ReceiveCryptoModal
import com.example.components.RigPurchaseModal
import com.example.components.SeedBackupModal
import com.example.components.TransferInAppModal
import com.example.components.WithdrawExternalModal
import com.example.screens.MiningScreen
import com.example.screens.ReferralScreen
import com.example.screens.RigsScreen
import com.example.screens.SecurityVaultScreen
import com.example.screens.WalletScreen
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CryptoGold
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberDarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.CryptoMinerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CryptoMinerViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val isAppLocked by viewModel.isAppLocked.collectAsStateWithLifecycle()
                val enteredPin by viewModel.pinEntered.collectAsStateWithLifecycle()
                val pinError by viewModel.pinError.collectAsStateWithLifecycle()
                val isScanningBiometric by viewModel.isBiometricScanning.collectAsStateWithLifecycle()

                val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
                val minerStatus by viewModel.minerStatus.collectAsStateWithLifecycle()
                val assets by viewModel.assets.collectAsStateWithLifecycle()
                val transactions by viewModel.transactions.collectAsStateWithLifecycle()
                val minedBlocks by viewModel.minedBlocks.collectAsStateWithLifecycle()
                val miningRigs by viewModel.miningRigs.collectAsStateWithLifecycle()
                val poolStats by viewModel.poolStats.collectAsStateWithLifecycle()
                val referralState by viewModel.referralState.collectAsStateWithLifecycle()
                val hashrateHistory by viewModel.hashrateHistory.collectAsStateWithLifecycle()
                val securityProfile by viewModel.securityProfile.collectAsStateWithLifecycle()
                val currencyMode by viewModel.currencyMode.collectAsStateWithLifecycle()

                val showWithdrawDialog by viewModel.showWithdrawDialog.collectAsStateWithLifecycle()
                val showTransferInAppDialog by viewModel.showTransferInAppDialog.collectAsStateWithLifecycle()
                val showReceiveDialog by viewModel.showReceiveDialog.collectAsStateWithLifecycle()
                val showEnterReferralCodeDialog by viewModel.showEnterReferralCodeDialog.collectAsStateWithLifecycle()
                val showSeedBackupDialog by viewModel.showSeedBackupDialog.collectAsStateWithLifecycle()
                val showPrivateKeyDialog by viewModel.showPrivateKeyDialog.collectAsStateWithLifecycle()
                val showChangePinDialog by viewModel.showChangePinDialog.collectAsStateWithLifecycle()
                val selectedRigForPurchase by viewModel.selectedRigForPurchase.collectAsStateWithLifecycle()
                val showRigPurchaseModal by viewModel.showRigPurchaseModal.collectAsStateWithLifecycle()
                val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(toastMessage) {
                    toastMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                // If app is in locked security mode, display the PIN & biometric lock screen
                if (isAppLocked) {
                    PinSecurityLockScreen(
                        enteredPin = enteredPin,
                        errorMessage = pinError,
                        isScanningBiometric = isScanningBiometric,
                        onDigitClick = { viewModel.enterPinDigit(it, context) },
                        onDeleteClick = { viewModel.deletePinDigit(context) },
                        onBiometricClick = { viewModel.triggerBiometricAuth(context) }
                    )
                } else {
                    // Back handler for sub-tabs to return to Mining
                    BackHandler(enabled = currentTab != AppNavTab.MINING) {
                        viewModel.selectTab(AppNavTab.MINING)
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = CyberDarkBg,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.coin_logo),
                                            contentDescription = "NovaMine Logo",
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "NovaMine",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (minerStatus.isMining) NeonEmerald else AlertRed)
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(
                                        onClick = { viewModel.lockApp() },
                                        modifier = Modifier.testTag("app_bar_lock_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Khóa bảo mật",
                                            tint = NeonCyan,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = CyberDarkBg
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = CyberCardBg,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentTab == AppNavTab.MINING,
                                    onClick = { viewModel.selectTab(AppNavTab.MINING) },
                                    icon = { Icon(Icons.Default.Bolt, contentDescription = "Đào Coin") },
                                    label = { Text("Đào Coin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = navItemColors(),
                                    modifier = Modifier.testTag("nav_mining")
                                )
                                NavigationBarItem(
                                    selected = currentTab == AppNavTab.WALLET,
                                    onClick = { viewModel.selectTab(AppNavTab.WALLET) },
                                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Ví Tiền") },
                                    label = { Text("Ví Tiền", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = navItemColors(),
                                    modifier = Modifier.testTag("nav_wallet")
                                )
                                NavigationBarItem(
                                    selected = currentTab == AppNavTab.REFERRAL,
                                    onClick = { viewModel.selectTab(AppNavTab.REFERRAL) },
                                    icon = { Icon(Icons.Default.People, contentDescription = "Giới Thiệu") },
                                    label = { Text("Mời Bạn", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = navItemColors(),
                                    modifier = Modifier.testTag("nav_referral")
                                )
                                NavigationBarItem(
                                    selected = currentTab == AppNavTab.RIGS,
                                    onClick = { viewModel.selectTab(AppNavTab.RIGS) },
                                    icon = { Icon(Icons.Default.Memory, contentDescription = "Dàn Đào") },
                                    label = { Text("Dàn Đào", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = navItemColors(),
                                    modifier = Modifier.testTag("nav_rigs")
                                )
                                NavigationBarItem(
                                    selected = currentTab == AppNavTab.SECURITY,
                                    onClick = { viewModel.selectTab(AppNavTab.SECURITY) },
                                    icon = { Icon(Icons.Default.Security, contentDescription = "Bảo Mật") },
                                    label = { Text("Bảo Mật", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = navItemColors(),
                                    modifier = Modifier.testTag("nav_security")
                                )
                            }
                        }
                    ) { innerPadding ->
                        Crossfade(
                            targetState = currentTab,
                            label = "tab_fade",
                            modifier = Modifier.padding(innerPadding)
                        ) { tab ->
                            when (tab) {
                                AppNavTab.MINING -> {
                                    MiningScreen(
                                        status = minerStatus,
                                        poolStats = poolStats,
                                        referralState = referralState,
                                        minedBlocks = minedBlocks,
                                        hashrateHistory = hashrateHistory,
                                        currencyMode = currencyMode,
                                        onToggleMining = { viewModel.toggleMining(context) },
                                        onTapBoost = { viewModel.tapBoostHashrate(context) },
                                        onRenewSession = { viewModel.renewSession(context) },
                                        onClaimRewards = { viewModel.claimSessionRewards(context) },
                                        onOpenWithdraw = { viewModel.openWithdrawDialog() },
                                        onNavigateToReferrals = { viewModel.selectTab(AppNavTab.REFERRAL) },
                                        onNavigateToRigs = { viewModel.selectTab(AppNavTab.RIGS) },
                                        onToggleCurrency = { viewModel.toggleCurrency() }
                                    )
                                }
                                AppNavTab.WALLET -> {
                                    WalletScreen(
                                        assets = assets,
                                        transactions = transactions,
                                        currencyMode = currencyMode,
                                        onOpenWithdraw = { viewModel.openWithdrawDialog() },
                                        onOpenTransferInApp = { viewModel.openTransferInAppDialog() },
                                        onOpenReceive = { viewModel.openReceiveDialog() },
                                        onToggleCurrency = { viewModel.toggleCurrency() }
                                    )
                                }
                                AppNavTab.REFERRAL -> {
                                    ReferralScreen(
                                        referralState = referralState,
                                        onOpenEnterCodeDialog = { viewModel.openEnterReferralCodeDialog() },
                                        onSimulateNewMember = { viewModel.simulateNewReferredMember(context) },
                                        onShareLink = { viewModel.shareReferralLink(context) }
                                    )
                                }
                                AppNavTab.RIGS -> {
                                    val usdtBalance = assets.find { it.symbol == "USDT" }?.balance ?: 0.0
                                    RigsScreen(
                                        rigs = miningRigs,
                                        usdtBalance = usdtBalance,
                                        onEquipRig = { viewModel.equipRig(it, context) },
                                        onOpenPurchaseRig = { viewModel.openRigPurchaseModal(it) },
                                        onQuickDepositUsdt = { viewModel.depositUsdt(it, context) }
                                    )
                                }
                                AppNavTab.SECURITY -> {
                                    SecurityVaultScreen(
                                        securityProfile = securityProfile,
                                        onLockAppNow = { viewModel.lockApp() },
                                        onToggleBiometrics = { viewModel.toggleBiometrics(it) },
                                        onOpenChangePin = { viewModel.openChangePinDialog() },
                                        onOpenSeedBackup = { viewModel.openSeedBackupDialog() },
                                        onOpenPrivateKey = { viewModel.openPrivateKeyDialog() }
                                    )
                                }
                            }
                        }
                    }

                    // Modals
                    if (showWithdrawDialog) {
                        WithdrawExternalModal(
                            assets = assets,
                            onWithdraw = { symbol, address, network, amount, pin ->
                                viewModel.withdrawToExternal(symbol, address, network, amount, pin, context)
                            },
                            onDismiss = { viewModel.closeWithdrawDialog() }
                        )
                    }

                    if (showTransferInAppDialog) {
                        TransferInAppModal(
                            assets = assets,
                            onTransfer = { symbol, username, amount, pin ->
                                viewModel.transferInApp(symbol, username, amount, pin, context)
                            },
                            onDismiss = { viewModel.closeTransferInAppDialog() }
                        )
                    }

                    if (showReceiveDialog) {
                        ReceiveCryptoModal(
                            walletAddress = securityProfile.walletPublicKey,
                            onSimulateReceive = { amount ->
                                viewModel.simulateReceive("NVC", amount)
                            },
                            onDismiss = { viewModel.closeReceiveDialog() }
                        )
                    }

                    if (showEnterReferralCodeDialog) {
                        EnterReferralCodeModal(
                            onApplyCode = { code ->
                                viewModel.applyInviterCode(code, context)
                            },
                            onDismiss = { viewModel.closeEnterReferralCodeDialog() }
                        )
                    }

                    if (showSeedBackupDialog) {
                        SeedBackupModal(
                            seedWords = securityProfile.seedWords,
                            isBackedUp = securityProfile.isSeedPhraseBackedUp,
                            onConfirmBackup = { viewModel.markSeedPhraseBackedUp() },
                            onDismiss = { viewModel.closeSeedBackupDialog() }
                        )
                    }

                    if (showPrivateKeyDialog) {
                        PrivateKeyExportModal(
                            maskedKey = securityProfile.walletPrivateKeyMasked,
                            onDismiss = { viewModel.closePrivateKeyDialog() }
                        )
                    }

                    if (showChangePinDialog) {
                        ChangePinModal(
                            onPinChanged = { newPin ->
                                viewModel.updatePin(newPin)
                            },
                            onDismiss = { viewModel.closeChangePinDialog() }
                        )
                    }

                    if (showRigPurchaseModal && selectedRigForPurchase != null) {
                        RigPurchaseModal(
                            rig = selectedRigForPurchase!!,
                            userUsdtBalance = assets.find { it.symbol == "USDT" }?.balance ?: 0.0,
                            onConfirmPurchase = { pin ->
                                viewModel.purchaseRigWithUsdt(selectedRigForPurchase!!.id, pin, context)
                            },
                            onQuickDepositUsdt = { amt ->
                                viewModel.depositUsdt(amt, context)
                            },
                            onDismiss = { viewModel.closeRigPurchaseModal() }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun navItemColors() = NavigationBarItemDefaults.colors(
        selectedIconColor = NeonCyan,
        selectedTextColor = NeonCyan,
        unselectedIconColor = TextMuted,
        unselectedTextColor = TextMuted,
        indicatorColor = Color(0xFF0F2633)
    )
}

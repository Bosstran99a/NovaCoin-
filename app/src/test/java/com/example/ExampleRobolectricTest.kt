package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CryptoRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NovaMine", appName)
    }

    @Test
    fun `test referral system bonuses and mining`() {
        val repo = CryptoRepository()
        val initialNvc = repo.minerStatus.value.totalMinedNvc

        // Apply inviter code
        val success = repo.applyInviterCode("NOVA-TEST-CODE")
        assertTrue(success)

        // Verifies +5.0 NVC bonus added
        assertEquals(initialNvc + 5.0, repo.minerStatus.value.totalMinedNvc, 0.001)
        assertTrue(repo.referralState.value.isInviterCodeApplied)
    }

    @Test
    fun `test external withdrawal security verification`() {
        val repo = CryptoRepository()
        val initialBalance = repo.assets.value.first { it.symbol == "NVC" }.balance

        val withdrawn = repo.withdrawToExternalWallet(
            symbol = "NVC",
            externalAddress = "0x7F2a91C840bE15993C19Fe4dAe542C128bC89D42",
            network = "NovaChain Mainnet",
            amount = 10.0,
            feeAmount = 0.001
        )
        assertTrue(withdrawn)

        val newBalance = repo.assets.value.first { it.symbol == "NVC" }.balance
        assertEquals(initialBalance - 10.001, newBalance, 0.001)
    }

    @Test
    fun `test USDT rig unlock tiers 20$, 100$, 500$, 1000$`() {
        val repo = CryptoRepository()
        val rigs = repo.miningRigs.value

        // Verify tier prices
        assertEquals(20.0, rigs.first { it.id == "rig_cyber_v2" }.costUsdt, 0.01)
        assertEquals(100.0, rigs.first { it.id == "rig_quantum_hydro" }.costUsdt, 0.01)
        assertEquals(500.0, rigs.first { it.id == "rig_solar_titan" }.costUsdt, 0.01)
        assertEquals(1000.0, rigs.first { it.id == "rig_genesis_quantum" }.costUsdt, 0.01)

        val initialUsdt = repo.assets.value.first { it.symbol == "USDT" }.balance

        // Unlock 20$ rig
        val unlocked20 = repo.purchaseRigWithUsdt("rig_cyber_v2")
        assertTrue(unlocked20)
        val usdtAfter20 = repo.assets.value.first { it.symbol == "USDT" }.balance
        assertEquals(initialUsdt - 20.0, usdtAfter20, 0.01)
        assertTrue(repo.miningRigs.value.first { it.id == "rig_cyber_v2" }.isUnlocked)

        // Deposit 1000 USDT to test 1000$ tier
        repo.depositTestUsdt(1000.0)
        val unlocked1000 = repo.purchaseRigWithUsdt("rig_genesis_quantum")
        assertTrue(unlocked1000)
        assertTrue(repo.miningRigs.value.first { it.id == "rig_genesis_quantum" }.isUnlocked)
        assertEquals(1450.0, repo.minerStatus.value.baseHashrateMhs, 0.01)
    }
}

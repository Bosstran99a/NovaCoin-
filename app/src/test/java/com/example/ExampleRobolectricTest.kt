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
}

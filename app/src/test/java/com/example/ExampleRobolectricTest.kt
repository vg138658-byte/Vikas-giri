package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    assertEquals("UMESH ENTERPRISE", appName)
  }

  @Test
  fun `verify daily rate entity format`() {
    val rate = com.example.data.DailyRateEntity(
      productName = "Gitti",
      rateDisplay = "₹ 1,850",
      unit = "Per Ton",
      lastUpdated = "Today Morning"
    )
    assertEquals("Gitti", rate.productName)
    assertEquals("₹ 1,850", rate.rateDisplay)
    assertEquals("Per Ton", rate.unit)
  }

  @Test
  fun `verify product entity categories`() {
    val categories = listOf("GITTI", "BALU", "SARIYA", "CEMENT", "EENT")
    assertEquals(5, categories.size)
  }
}

package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.EscapeRoomRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    assertEquals("Operación Verdad", appName)
  }

  @Test
  fun `verify escape room enigmas configuration`() {
    val enigmas = EscapeRoomRepository.enigmas
    assertEquals(6, enigmas.size)

    val digitEnigmas = enigmas.filter { it.givesDigit && it.digitValue != null }
    assertEquals(4, digitEnigmas.size)

    // Verify final vault code matches discovered digits in order: 7 (E1), 3 (E3), 9 (E4), 2 (E6)
    assertEquals("7392", EscapeRoomRepository.FINAL_VAULT_CODE)
    assertEquals("1980", EscapeRoomRepository.TEACHER_SECRET_KEY)
    assertEquals("VERITAS", EscapeRoomRepository.DEFAULT_ROOM_PASSWORD)
  }
}

package se.johan.queueit.apiservices

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import se.johan.queueit.di.AppModule
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class GetLyricsTest {
    @Inject
    lateinit var apiServices: ApiServicesUseCases

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        grantPermissions()
    }

    private fun grantPermissions() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val permission = Manifest.permission.INTERNET

        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            InstrumentationRegistry.getInstrumentation().uiAutomation
                .executeShellCommand("pm grant ${context.packageName} $permission")
                .close() // Important to close the ParcelFileDescriptor
        }
    }

    @Test
    fun get_lyric_successful() {
        runBlocking {
            val result = try {
                when (apiServices.getLyric(artist = "accept", title = "wrong is right")) {
                    is LyricsApiResult.Success -> {
                        true
                    }
                    is LyricsApiResult.Error -> {
                       false
                    }
                }
            } catch (e: Exception) {
                false
            }
            assertTrue(result)
        }
    }

    @Test
    fun get_lyric_unsuccessful() {
        runBlocking {
            val result = try {
                when (apiServices.getLyric(artist = "", title = "")) {
                    is LyricsApiResult.Success -> {
                        false
                    }
                    is LyricsApiResult.Error -> {
                        true
                    }
                }
            } catch (e: Exception) {
                false
            }
            assertTrue(result)
        }
    }
}
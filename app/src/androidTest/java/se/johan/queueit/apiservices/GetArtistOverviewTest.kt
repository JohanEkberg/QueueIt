package se.johan.queueit.apiservices

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
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
import se.johan.queueit.TAG
import se.johan.queueit.di.AppModule
import se.johan.queueit.ui.UiEvent
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class GetArtistOverviewTest {
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
    fun get_artist_overview_successful() {
        runBlocking {
            val result = try {
                when (apiServices.getArtistOverview(artist = "accept")) {
                    is OverviewApiResult.Success -> {
                        true
                    }
                    is OverviewApiResult.Error -> {
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
    fun get_artist_overview_unsuccessful() {
        runBlocking {
            val result = try {
                when (apiServices.getArtistOverview(artist = "")) {
                    is OverviewApiResult.Success -> {
                        false
                    }
                    is OverviewApiResult.Error -> {
                       true
                    }
                }
            } catch (e: Exception) {
                Log.e("get_artist_overview_unsuccessful", "Exception: ${e.message}")
                false
            }
            assertTrue(result)
        }
    }
}
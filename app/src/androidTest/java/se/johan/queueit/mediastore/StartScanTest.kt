package se.johan.queueit.mediastore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import se.johan.queueit.MainActivity
import se.johan.queueit.di.AppModule
import se.johan.queueit.di.TestAppModule
import se.johan.queueit.mediastore.usecases.AudioScannerUseCases
import se.johan.queueit.mediastore.usecases.StartScan
import se.johan.queueit.model.usecases.AddAlbum
import se.johan.queueit.model.usecases.AddArtist
import se.johan.queueit.model.usecases.AddSong
import se.johan.queueit.model.usecases.AudioDataUseCases
import se.johan.queueit.model.usecases.GetSongs
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class StartScanTest {
    @Inject
    lateinit var audioDataUseCases: AudioDataUseCases

    @Inject
    lateinit var audioScannerUseCases: AudioScannerUseCases

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        grantPermissions()
    }

    private fun grantPermissions() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val permission = Manifest.permission.READ_MEDIA_AUDIO

        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            InstrumentationRegistry.getInstrumentation().uiAutomation
                .executeShellCommand("pm grant ${context.packageName} $permission")
                .close() // Important to close the ParcelFileDescriptor
        }
    }

    @Test
    fun scan_media_store() {
        runBlocking {
            val result = try {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                val success = audioScannerUseCases.startScan(context)
                assertTrue(success)
                val songs = audioDataUseCases.getSongs()
                assertTrue(songs.size > 1)
                true
            } catch (e: Exception) {
                false
            }
            assertTrue(result)
        }
    }
}
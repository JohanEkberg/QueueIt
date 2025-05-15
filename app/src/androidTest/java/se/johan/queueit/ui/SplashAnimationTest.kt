package se.johan.queueit.ui

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import se.johan.queueit.MainActivity
import se.johan.queueit.di.AppModule
import se.johan.queueit.ui.navigation.QueueItNavGraph
import se.johan.queueit.ui.screens.components.TopToolbar
import se.johan.queueit.ui.theme.QueueItTheme
import se.johan.queueit.viewmodel.SharedSearchViewModel

@HiltAndroidTest
@UninstallModules(AppModule::class)
//@RunWith(AndroidJUnit4::class)
class SplashAnimationTest {

    @get: Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val sharedSearchViewModel: SharedSearchViewModel = hiltViewModel()
            QueueItTheme {
               // Create an instance of the navigation controller,
               val navController = rememberNavController()
               Scaffold(
                   modifier = Modifier.fillMaxSize(),
                   // We want te be able to open the settings screen from the menu
                   topBar = {
                       TopToolbar(navController)
                   }
               ) { innerPadding ->
                   QueueItNavGraph(navController, innerPadding, sharedSearchViewModel)
               }
            }
        }
    }

    @Test
    fun splash_animation_visible_test() {
        composeRule
            .onNodeWithTag("SPLASH_ANIMATION")
            .assertExists()
    }
}
package se.johan.queueit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import se.johan.queueit.ui.handleUiEvent
import se.johan.queueit.ui.navigation.QueueItNavGraph
import se.johan.queueit.ui.navigation.resolveScreen
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.ui.screens.components.SearchToolbar
import se.johan.queueit.ui.screens.components.TopToolbar
import se.johan.queueit.ui.theme.QueueItTheme
import se.johan.queueit.viewmodel.BottomSheetViewModel
import se.johan.queueit.viewmodel.SharedSearchViewModel
import se.johan.queueit.viewmodel.dynamicBottomPadding

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QueueItTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                // Instantiate the BottomSheetViewModel i.e. bottom player.
                val bottomSheetViewModel: BottomSheetViewModel = hiltViewModel()
                val sheetContent = bottomSheetViewModel.content

                val sheetState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberStandardBottomSheetState(
                        initialValue = SheetValue.Hidden,
                        skipHiddenState = false // allow the sheet to animate to 'Hidden'
                    )
                )

                // Handle the bottom sheet state
                LaunchedEffect(sheetContent) {
                    if (sheetContent != null) {
                        sheetState.bottomSheetState.expand()
                    } else {
                        sheetState.bottomSheetState.hide()
                    }
                }

                // Handle the global UI events
                LaunchedEffect(Unit) {
                    bottomSheetViewModel.uiEvent.collect { event ->
                        handleUiEvent(event) {
                            snackbarHostState.showSnackbar(it)
                        }
                    }
                }

                // The search view model is shared between the searchToolbar and the searchScreen,
                // hence we instantiate it here.
                val sharedSearchViewModel: SharedSearchViewModel = hiltViewModel()

                // Create an instance of the navigation controller and get the current which we need
                // to check if it's the SearchToolbar that should be visible.
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val currentScreen = resolveScreen(currentRoute)
                Log.i(TAG, "Destination: ${currentRoute} ${currentScreen}")
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(
                        snackbarHostState,
                        modifier = Modifier.padding(bottom = bottomSheetViewModel.dynamicBottomPadding())
                    ) },
                            // We want te be able to open the settings screen from the menu
                    topBar = {
                        when (currentScreen) {
                            AppScreens.SearchScreenIdentifier -> SearchToolbar(navController, sharedSearchViewModel)
                            else -> TopToolbar(navController)
                        }
                    }
                ) { innerPadding ->
                    BottomSheetScaffold(
                        scaffoldState = sheetState,
                        sheetPeekHeight = if (sheetContent != null) 64.dp else 0.dp,
                        sheetDragHandle = null,
                        sheetContent = {
                            AnimatedVisibility(visible = sheetContent != null) {
                                sheetContent?.invoke()
                            }
                        }
                    ) {
                        QueueItNavGraph(navController, innerPadding, sharedSearchViewModel)
                    }
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    QueueItTheme {
//        Greeting("Android")
//    }
//}
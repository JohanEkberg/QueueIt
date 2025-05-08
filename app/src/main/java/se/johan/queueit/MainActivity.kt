package se.johan.queueit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import se.johan.queueit.ui.navigation.QueueItNavGraph
import se.johan.queueit.ui.screens.SettingsScreenIdentifier
import se.johan.queueit.ui.screens.components.Player
import se.johan.queueit.ui.screens.components.TopToolbar
import se.johan.queueit.ui.theme.QueueItTheme
import se.johan.queueit.viewmodel.BottomSheetViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QueueItTheme {

                val bottomSheetViewModel: BottomSheetViewModel = hiltViewModel()
                val sheetContent = bottomSheetViewModel.content


                val sheetState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberStandardBottomSheetState(
                        initialValue = SheetValue.Hidden,
                        skipHiddenState = false // allow the sheet to animate to 'Hidden'
                    )
                )
                //val isSongInQueue by remember { mutableStateOf(false) }

                LaunchedEffect(sheetContent) {
                    if (sheetContent != null) {
                        sheetState.bottomSheetState.expand()
                    } else {
                        sheetState.bottomSheetState.hide()
                    }
                }

                // Create an instance of the navigation controller,
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    // We want te be able to open the settings screen from the menu
                    topBar = {
                        TopToolbar(navController)
                    }
                ) { innerPadding ->

                    BottomSheetScaffold(
                        scaffoldState = sheetState,
                        sheetPeekHeight = if (sheetContent != null) 64.dp else 0.dp,
                        sheetDragHandle = null,
                        sheetContent = {
                            AnimatedVisibility(visible = sheetContent != null) {
                                sheetContent?.invoke()
                                //Player()
                            }
                            //Player()
                        }
                    ) {
                        QueueItNavGraph(navController, innerPadding)
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
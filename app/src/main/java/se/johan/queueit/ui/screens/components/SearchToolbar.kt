package se.johan.queueit.ui.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import se.johan.queueit.ui.screens.AppScreens
import se.johan.queueit.viewmodel.SharedSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolbar(
    navController: NavController,
    searchViewModel: SharedSearchViewModel
) {
    //val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(AppScreens.HomeScreenIdentifier)
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(text = "Search...", color = Color.White)
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f)
                )
            )
        },
        actions = {}
    )

    // Debounce effect
    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300) // delay in milliseconds
            .collectLatest { input ->
                if (input.isNotBlank()) {
                    searchViewModel.doSearch(input)
                }
            }
    }
}
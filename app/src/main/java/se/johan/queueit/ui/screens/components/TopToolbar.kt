package se.johan.queueit.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import se.johan.queueit.R
import se.johan.queueit.ui.screens.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolbar(
    navController: NavController
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(R.string.app_name), color = Color.White) },
        actions = {
            IconButton(onClick = {
                navController.navigate(AppScreens.SearchScreenIdentifier)
            }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }

            IconButton(onClick = { showMenu = true }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.menu_settings)) },
                    onClick = {
                        showMenu = false
                        navController.navigate(AppScreens.SettingsScreenIdentifier)
                    }
                )
            }
        }
    )
}
package se.johan.queueit.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.johan.queueit.TAG
import se.johan.queueit.mediastore.ScanHandler
import se.johan.queueit.model.usecases.AudioDataUseCases
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (
    private val audioDataUseCases: AudioDataUseCases,
    private val scanHandler: ScanHandler
) : ViewModel() {
    val artistsDetected: StateFlow<List<String>> = scanHandler.artistsDetected
    var successfulScan: StateFlow<Boolean?> = scanHandler.successfulScan

    private val _isPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean> = _isPermissionGranted

    fun updatePermissionStatus(granted: Boolean) {
        if (_isPermissionGranted.value != granted) {
            Log.i(TAG, "Permission granted: $granted")
            _isPermissionGranted.value = granted
        }
    }

    fun requireScan(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    scanHandler.startScan(
                        context = context,
                        skip = audioDataUseCases.databaseExist(context))
                } catch (e: Exception) {
                    Log.e(TAG, "Scan autio data failed, ${e.message}")
                }
            }
        }
    }
}
package se.johan.queueit.mediastore

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.johan.queueit.mediastore.usecases.AudioScannerUseCases
import javax.inject.Inject

class ScanHandler  @Inject constructor (
    private val audioScannerUseCases: AudioScannerUseCases
) {
    private val _artistsDetected = MutableStateFlow<List<String>>(emptyList())
    val artistsDetected: StateFlow<List<String>> = _artistsDetected

    private val _successfulScan: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var successfulScan: StateFlow<Boolean?> = _successfulScan

    fun startScan(context: Context, skip: Boolean = false) {
        try {
            if (skip) {
                _successfulScan.value = true
            } else {
                audioScannerUseCases.startScan(context) { artist ->
                    _artistsDetected.update { currentList ->
                        if (artist !in currentList) currentList + artist else currentList
                    }
                }.also {
                    _successfulScan.value = it
                }
            }
        } catch (e: Exception) {
            Log.e("ScanHandler", "Scan audio data failed, ${e.message}")
            _successfulScan.value = false
        }
    }
}
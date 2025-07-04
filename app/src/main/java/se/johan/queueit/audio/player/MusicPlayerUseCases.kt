package se.johan.queueit.audio.player

data class MusicPlayerUseCases(
    val play: Play,
    val pause: Pause,
    val skip: Skip,
    val stop: Stop,
    val setOnProgress: SetOnProgress,
    val setOnCompletion: SetOnCompletion,
    val getCurrentSong: GetCurrentSong
)

class MusicPlayerException(
    message: String = "",
    val errorCode: Int = 0
) : Exception(message)
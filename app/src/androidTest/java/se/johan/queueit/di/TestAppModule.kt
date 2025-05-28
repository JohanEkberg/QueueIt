package se.johan.queueit.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.johan.queueit.apiservices.ApiEndPoints.LYRICS_BASE_URL
import se.johan.queueit.apiservices.ApiServicesUseCases
import se.johan.queueit.apiservices.GetLyric
import se.johan.queueit.apiservices.LyricApiService
import se.johan.queueit.apiservices.LyricsApi
import se.johan.queueit.apiservices.LyricsRepository
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.audio.player.GetCurrentSong
import se.johan.queueit.audio.player.MusicPlayerRepository
import se.johan.queueit.audio.player.MusicPlayerUseCases
import se.johan.queueit.audio.player.Pause
import se.johan.queueit.audio.player.Play
import se.johan.queueit.audio.player.SetOnCompletion
import se.johan.queueit.audio.player.SetOnProgress
import se.johan.queueit.audio.player.Skip
import se.johan.queueit.audio.player.Stop
import se.johan.queueit.audio.queue.AddQueueItem
import se.johan.queueit.audio.queue.ClearQueue
import se.johan.queueit.audio.queue.GetQueueItem
import se.johan.queueit.audio.queue.GetQueueItems
import se.johan.queueit.audio.queue.IsEmpty
import se.johan.queueit.audio.queue.IsEqual
import se.johan.queueit.audio.queue.ObserveQueue
import se.johan.queueit.audio.queue.PeekQueue
import se.johan.queueit.audio.queue.QueueSize
import se.johan.queueit.audio.queue.RemoveQueueItem
import se.johan.queueit.audio.queue.SongQueueRepository
import se.johan.queueit.audio.queue.SongQueueUseCases
import se.johan.queueit.mediastore.usecases.AudioScannerUseCases
import se.johan.queueit.mediastore.usecases.StartScan
import se.johan.queueit.model.database.AudioDatabase
import se.johan.queueit.model.usecases.AddAlbum
import se.johan.queueit.model.usecases.AddArtist
import se.johan.queueit.model.usecases.AddSong
import se.johan.queueit.model.usecases.AudioDataUseCases
import se.johan.queueit.model.usecases.ClearAllDatabaseTables
import se.johan.queueit.model.usecases.DatabaseExist
import se.johan.queueit.model.usecases.DeleteAllData
import se.johan.queueit.model.usecases.GetAlbumById
import se.johan.queueit.model.usecases.GetAlbumByName
import se.johan.queueit.model.usecases.GetAlbumSongs
import se.johan.queueit.model.usecases.GetArtistById
import se.johan.queueit.model.usecases.GetArtistByName
import se.johan.queueit.model.usecases.GetArtistWithAlbums
import se.johan.queueit.model.usecases.GetArtistWithSongs
import se.johan.queueit.model.usecases.GetArtists
import se.johan.queueit.model.usecases.GetPagedAlbum
import se.johan.queueit.model.usecases.GetPagedAlbumWithSong
import se.johan.queueit.model.usecases.GetPagedArtistWithSongs
import se.johan.queueit.model.usecases.GetSongByName
import se.johan.queueit.model.usecases.GetSongs
import java.util.LinkedList
import java.util.Queue
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun provideBooksDatabase(context: Application) : AudioDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AudioDatabase::class.java
        ).build()
    }

    @Provides
    @Singleton
    fun provideBooksUseCases(db: AudioDatabase) : AudioDataUseCases {
        return AudioDataUseCases(
            databaseExist = DatabaseExist(),
            addArtist = AddArtist(db.audioDataDao()),
            addAlbum = AddAlbum(db.audioDataDao()),
            addSong = AddSong(db.audioDataDao()),
            getPagedAlbum = GetPagedAlbum(db.audioDataDao()),
            getPagedArtistWithSongs = GetPagedArtistWithSongs(db.audioDataDao()),
            getPagedAlbumWithSong = GetPagedAlbumWithSong(db.audioDataDao()),
            getArtists = GetArtists(db.audioDataDao()),
            getArtistById = GetArtistById(db.audioDataDao()),
            getArtistByName = GetArtistByName(db.audioDataDao()),
            getArtistWithSongs = GetArtistWithSongs(db.audioDataDao()),
            getArtistWithAlbums = GetArtistWithAlbums(db.audioDataDao()),
            getAlbumSongs = GetAlbumSongs(db.audioDataDao()),
            getAlbum = GetAlbumById(db.audioDataDao()),
            getAlbumByName = GetAlbumByName(db.audioDataDao()),
            getSongByName = GetSongByName(db.audioDataDao()),
            deleteAllData = DeleteAllData(db.audioDataDao()),
            clearAllDatabaseTables = ClearAllDatabaseTables(db),
            getSongs = GetSongs(db.audioDataDao())
        )
    }

    @Provides
    @Singleton
    fun provideAudioScannerRepository(audioDataUseCases: AudioDataUseCases) : AudioScannerUseCases {
        return AudioScannerUseCases(startScan = StartScan(audioDataUseCases)
        )
    }

    @Provides
    @Singleton
    fun provideSongQueueRepository(): SongQueueRepository {
        return SongQueueRepository()
    }

    @Provides
    @Singleton
    fun provideSongQueueUseCases(songQueueRepository: SongQueueRepository): SongQueueUseCases {
        return SongQueueUseCases(
            addQueueItem = AddQueueItem(songQueueRepository),
            clearQueue = ClearQueue(songQueueRepository),
            getQueueItem = GetQueueItem(songQueueRepository),
            getQueueItems = GetQueueItems(songQueueRepository),
            isEmpty = IsEmpty(songQueueRepository),
            isEqual = IsEqual(songQueueRepository),
            queueSize = QueueSize(songQueueRepository),
            removeQueueItem = RemoveQueueItem(songQueueRepository),
            peekQueue = PeekQueue(songQueueRepository),
            observeQueue = ObserveQueue(songQueueRepository)
        )
    }

    @Provides
    @Singleton
    fun provideMusicPlayerRepository(songQueueUseCases: SongQueueUseCases): MusicPlayerRepository {
        return MusicPlayerRepository(songQueueUseCases)
    }

    @Provides
    @Singleton
    fun provideMusicPlayerUseCases(repository: MusicPlayerRepository): MusicPlayerUseCases {
        return MusicPlayerUseCases(
            play = Play(repository),
            pause = Pause(repository),
            skip = Skip(repository),
            stop = Stop(repository),
            setOnProgress = SetOnProgress(repository),
            setOnCompletion = SetOnCompletion(repository),
            getCurrentSong = GetCurrentSong(repository)
        )
    }

    @LyricsApi
    @Provides
    fun provideLyricsBaseUrl() = LYRICS_BASE_URL

    @LyricsApi
    @Provides
    @Singleton
    fun provideRetrofit(@LyricsApi baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @LyricsApi
    @Provides
    @Singleton
    fun provideLyricsApi(@LyricsApi retrofit: Retrofit): LyricApiService =
        retrofit.create(LyricApiService::class.java)

    @Provides
    @Singleton
    fun provideLyricsRepository(@LyricsApi lyricApiService: LyricApiService): LyricsRepository {
        return LyricsRepository(lyricApiService)
    }

    @Provides
    @Singleton
    fun provideApiServicesUseCases(repository: LyricsRepository): ApiServicesUseCases {
        return ApiServicesUseCases(
            getLyric = GetLyric(repository)
        )
    }
}
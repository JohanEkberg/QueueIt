package se.johan.queueit.mediastore.usecases

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import se.johan.queueit.TAG
import se.johan.queueit.audio.data.AudioFileMetaData
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.SongEntity
import se.johan.queueit.model.usecases.AudioDataUseCases
import java.util.Date
import javax.inject.Inject

class StartScan@Inject constructor (
    private val audioDataUseCases: AudioDataUseCases
) {
    operator fun invoke(
        context: Context,
        newArtistDetectedCallback: ((artist: String) -> Unit)? = null
    ) : Boolean {
        return try {
            val collection =
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID
            )

            val query =  context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                null
            )

            query?.use { cursor ->
                if (!iterateMediaStore(
                        context = context,
                        cursor = cursor,
                        columnIndices = getCacheColumnIndices(cursor),
                        artistNotificationCallback = newArtistDetectedCallback
                    )) {
                    Log.e(TAG, "Scan media store failed")
                }
            }
            true
        } catch(e: Exception) {
            Log.e(TAG, "Scan media files failed, exception: ${e.message}, stacktrace: ${Log.getStackTraceString(e)}")
            false
        }
    }

    private fun getCacheColumnIndices(cursor: Cursor) : CacheColumnIndices? {
        return try {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val yearColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            CacheColumnIndices(
                idColumn = idColumn,
                artistColumn = artistColumn,
                albumColumn = albumColumn,
                titleColumn = titleColumn,
                yearColumn =  yearColumn,
                durationColumn = durationColumn,
                sizeColumn = sizeColumn,
                albumIdColumn = albumIdColumn
            )
        } catch(e: Exception) {
            Log.e(TAG, "Failed to get cache column indices, exception: ${e.message}")
            null
        }
    }

    private fun iterateMediaStore(
        context: Context,
        cursor: Cursor,
        columnIndices: CacheColumnIndices?,
        artistNotificationCallback: ((artist: String) -> Unit)?
        ) : Boolean {
        return try {
            if (columnIndices == null) {
                Log.e(TAG, "Failed to iterate media store, no column indices provided")
                return false
            }

            var latestAlbumId: Long = -1
            val albumList = mutableListOf<AlbumItem>()
            var latestArtistId: Long = -1
            val artistList = mutableListOf<ArtistItem>()

            loop@while (cursor.moveToNext()) {
                // Get values of columns for a given audio file.
                val songId = cursor.getLong(columnIndices.idColumn)
                val artist = cursor.getString(columnIndices.artistColumn)
                val album = cursor.getString(columnIndices.albumColumn)
                val title = cursor.getString(columnIndices.titleColumn)
                val year = cursor.getString(columnIndices.yearColumn) ?: ""
                val duration = cursor.getString(columnIndices.durationColumn) ?: ""
                val size = cursor.getInt(columnIndices.sizeColumn)
                val albumId = cursor.getLong(columnIndices.albumIdColumn)

                val albumUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )
                val artworkExist = checkURIResource(context.contentResolver, albumUri)

                val songUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    songId
                )

                Log.d(TAG,
                    "Artist=${artist} " +
                            "Album=${album} " +
                            "Title=${title} " +
                            "Year=${year} " +
                            "Duration=${duration} " +
                            "Size=${size} " +
                            "AlbumUri=${albumUri} " +
                            "SongUri=${songUri} " +
                            "Artwork exist=${artworkExist}")

                if (artist.isNullOrEmpty() ||
                    artist.equals("<unknown>") ||
                    album.isNullOrEmpty() ||
                    title.isNullOrEmpty() ||
                    size <= 0) {
                    Log.i(
                        TAG,
                        "Skip item: " +
                                "Artist=${artist} " +
                                "Album=${album} " +
                                "Title=${title} " +
                                "Size=${size}"
                    )
                    continue@loop
                }

                val audioFileMetaData = AudioFileMetaData(
                    songUri = songUri,
                    title = title,
                    album = album,
                    artist = artist,
                    genre = "",
                    year = year,
                    format = "",
                    duration = duration,
                    resolution = "",
                    size = size.toLong(),
                    bitmap = null,
                    albumUri = if (artworkExist) { albumUri } else { Uri.parse("") }
                )

                /**
                 * Store data in the album database table if it is a new album.
                 */
                val currentAlbum = albumList.find { item -> album.equals(item.value, ignoreCase = true) }
                if (currentAlbum == null) {
                    latestAlbumId = audioDataUseCases.addAlbum(
                        AlbumEntity(
                            albumName = audioFileMetaData.album.replace("\\s+".toRegex(), " "),
                            albumUri = audioFileMetaData.albumUri.toString(),
                            genre = audioFileMetaData.genre,
                            year = audioFileMetaData.year
                        ))
                    albumList.add(AlbumItem(value = album, id = latestAlbumId))
                } else {
                    latestAlbumId = currentAlbum.id
                }

                /**
                 * Store data in the artist database table if it is a new artist.
                 */
                val currentArtist = artistList.find { item -> artist.equals(item.value, ignoreCase = true) }
                if (currentArtist == null) {
                    latestArtistId = audioDataUseCases.addArtist(ArtistEntity(artistName = audioFileMetaData.artist))
                    artistList.add(ArtistItem(value = artist, id = latestArtistId))
                    artistNotificationCallback?.let { it((artist)) }
                } else {
                    latestArtistId = currentArtist.id
                }

                audioDataUseCases.addSong(SongEntity(
                    timestampDate = Date(),
                    songArtistId = latestArtistId,
                    songAlbumId = latestAlbumId,
                    songName = audioFileMetaData.title,
                    songUri = audioFileMetaData.songUri.toString(),
                    albumUri = audioFileMetaData.albumUri.toString(),
                    format = audioFileMetaData.format,
                    duration = audioFileMetaData.duration,
                    resolution = audioFileMetaData.resolution,
                    size = audioFileMetaData.size
                ))
            }
            Log.d(TAG, "Total processed songs: ${albumList.size}")
            cursor.close()
            true
        } catch(e: Exception) {
            Log.e(TAG, "Failed to iterate media store, exception: ${e.message}")
            false
        }
    }

    private fun checkURIResource(resolver: ContentResolver, contentUri: Uri?): Boolean {
        val cursor: Cursor? = resolver.query(contentUri!!, null, null, null, null)
        val doesExist = cursor != null && cursor.moveToFirst()
        cursor?.close()
        return doesExist
    }
}

data class AlbumItem(var value: String = "", var id: Long = -1)
data class ArtistItem(var value: String = "", var id: Long = -1)
data class CacheColumnIndices(
    val idColumn: Int,
    val artistColumn: Int,
    val albumColumn: Int,
    val titleColumn: Int,
    val yearColumn: Int,
    val durationColumn: Int,
    val sizeColumn: Int,
    val albumIdColumn: Int
)
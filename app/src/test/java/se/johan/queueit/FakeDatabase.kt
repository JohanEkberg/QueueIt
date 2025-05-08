package se.johan.queueit

import androidx.paging.PagingSource
import se.johan.queueit.model.database.AlbumEntity
import se.johan.queueit.model.database.AlbumWithSongs
import se.johan.queueit.model.database.ArtistEntity
import se.johan.queueit.model.database.ArtistWithAlbums
import se.johan.queueit.model.database.ArtistWithSongs
import se.johan.queueit.model.database.AudioDataDao
import se.johan.queueit.model.database.SongEntity

class FakeDatabase : AudioDataDao {

    val testArtists = mutableListOf<ArtistEntity>()
    val testAlbums = mutableListOf<AlbumEntity>()
    val testSongs = mutableListOf<SongEntity>()

    override fun getPagedAlbums(): PagingSource<Int, AlbumEntity> {
        TODO("Not yet implemented")
    }

    override fun getPagedArtists(): PagingSource<Int, ArtistEntity> {
        TODO("Not yet implemented")
    }

    override fun getPagedArtistsWithSongs(): PagingSource<Int, ArtistWithSongs> {
        TODO("Not yet implemented")
    }

    override fun getArtistById(id: Int): ArtistEntity {
        return testArtists.first { it.artistId.toInt() == id }
    }

    override fun getPagedAlbumWithSongs(): PagingSource<Int, AlbumWithSongs> {
        TODO("Not yet implemented")
    }

    override fun getArtists(): List<ArtistEntity> {
        return testArtists
    }

    override fun getArtistByName(name: String): List<ArtistEntity> {
        return testArtists.filter { it.artistName == name }
    }

    override fun getArtistWithSongs(id: Long): ArtistWithSongs {
        TODO("Not yet implemented")
    }

    override fun getAlbumWithSongs(id: Long): AlbumWithSongs {
        TODO("Not yet implemented")
    }

    override fun getArtistWithAlbums(id: Long): ArtistWithAlbums {
        TODO("Not yet implemented")
    }

    override fun getAlbumById(id: Int): AlbumEntity {
        return testAlbums.first { it.albumId.toInt() == id }
    }

    override fun getAlbumByName(name: String): List<AlbumEntity> {
        return testAlbums.filter { it.albumName == name }
    }

    override fun getSongByName(name: String): List<SongEntity> {
        return testSongs.filter { it.songName == name }
    }

    override fun getSongs(): List<SongEntity> {
        return testSongs
    }

    override fun insert(artistEntity: ArtistEntity): Long {
        testArtists.add(artistEntity)
        return testArtists.size.toLong()
    }

    override fun insert(albumEntity: AlbumEntity): Long {
        testAlbums.add(albumEntity)
        return testAlbums.size.toLong()
    }

    override fun insert(vararg songEntity: SongEntity) {
        testSongs.addAll(songEntity)
    }

    override fun deleteAlbums() {
        testAlbums.clear()
    }

    override fun deleteArtists() {
        testArtists.clear()
    }

    override fun deleteSongs() {
        testSongs.clear()
    }
}
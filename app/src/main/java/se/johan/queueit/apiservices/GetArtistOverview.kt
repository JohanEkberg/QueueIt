package se.johan.queueit.apiservices

class GetArtistOverview(private val repository: OverviewRepository) {
    @Throws(ApiServicesException::class)
    suspend operator fun invoke(artist: String) : OverviewApiResult {
        return repository.getArtistOverview(artist)
    }
}
package nl.thairosi.activityx.network.NearbySearchApiModel

data class NearbySearchResponse(
    val results: List<Result>,
    val status: String
)
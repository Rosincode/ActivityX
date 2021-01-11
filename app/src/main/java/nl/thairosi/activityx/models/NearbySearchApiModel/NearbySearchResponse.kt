package nl.thairosi.activityx.models.NearbySearchApiModel

data class NearbySearchResponse(
    val results: List<Result>,
    val status: String
)
package nl.thairosi.activityx.network.NearbySearchApiModel

data class Result(
    val geometry: Geometry,
    val name: String,
    val place_id: String,
    val types: List<String>
)
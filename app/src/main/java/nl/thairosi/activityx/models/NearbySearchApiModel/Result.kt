package nl.thairosi.activityx.models.NearbySearchApiModel

data class Result(
    val geometry: Geometry,
    val name: String,
    val place_id: String,
    val types: List<String>
)
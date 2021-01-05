package nl.thairosi.activityx.network.NearbySearchApiModel

data class Result(
    val business_status: String,
    val geometry: Geometry,
    val icon: String,
    val name: String,
    val permanently_closed: Boolean,
    val photos: List<Photo>,
    val place_id: String,
    val plus_code: PlusCode,
    val price_level: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val user_ratings_total: Int,
    val vicinity: String
)
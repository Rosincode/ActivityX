package nl.thairosi.activityx.network.data

data class PlaceResponse(
    val html_attributions: List<Any>,
    val result: Result,
    val status: String,
    val error_message: String = ""
)
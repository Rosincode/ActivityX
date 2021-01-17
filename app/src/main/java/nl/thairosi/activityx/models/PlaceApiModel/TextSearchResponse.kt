package nl.thairosi.activityx.models.PlaceApiModel

data class TextSearchResponse(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)
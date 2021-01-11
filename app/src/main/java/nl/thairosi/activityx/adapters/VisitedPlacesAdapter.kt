package nl.thairosi.activityx.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_visited_place.view.*
import nl.thairosi.activityx.Keys
import nl.thairosi.activityx.R
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.network.PlaceAPIService
import nl.thairosi.activityx.utils.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class VisitedPlacesAdapter : RecyclerView.Adapter<VisitedPlacesAdapter.PlaceViewHolder>() {

    // ViewHolder for RecyclerView
    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // DiffUtil only update changes instead of the whole list
    private val differCallback = object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_visited_place,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Place) -> Unit)? = null
    private var onToggleClickListener: ((Place) -> Unit)? = null

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(Utils.getImageUrl(place.photoReference)).into(image_view_visited_place)
            text_view_name_visited_place.text = place.name
            text_view_date_place_visited.text = Utils.getDateToView(place.date)
            switch_block_visited_place.isChecked = place.blocked

            // OnclickListener for Item clicks
            setOnClickListener {
                onItemClickListener?.let { it(place) }
            }

            // OnclickListener for toggle "blocked"
            switch_block_visited_place.setOnClickListener {
                place.blocked = switch_block_visited_place.isChecked
                onToggleClickListener?.let { it(place) }
            }

        }
    }

    fun setOnItemClickListener(listener: (Place) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnToggleClickListener(listener: (Place) -> Unit) {
        onToggleClickListener = listener
    }

//    // Helper method to build Image Url
//    fun getImageUrl(photo: String): String {
//        val baseUrl = PlaceAPIService.BASE_URL
//        val request = "photo?"
//        val maxwidth = "maxwidth=400"
//        val reference = "photoreference=$photo"
//        val key = "key=${Keys.apiKey()}"
//        return "$baseUrl$request$maxwidth&$reference&$key"
//    }

//    // Helper method to show date
//    fun getDateToView(date: LocalDateTime?) : String {
//        return if (date != null) {
//            date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
//        } else {
//            ""
//        }
//    }

}
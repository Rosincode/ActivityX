package nl.thairosi.activityx.ui.visitedPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_visited_place.view.*
import nl.thairosi.activityx.R
import nl.thairosi.activityx.models.Place
import nl.thairosi.activityx.utils.Utils

/**
 * VisitedPlacesAdapter fills the recycler views for the visited places UI
 */
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

    // Helper for computing the difference between two lists via DiffUtil on a background thread.
    val differ = AsyncListDiffer(this, differCallback)

    // Get the current list item count
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Inflates the fragment_visited_place layout
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

    // Binds the view holders to the current list
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(Utils.getImageUrl(place.photoReference))
                .into(image_view_visited_place)
            text_view_name_visited_place.text = place.name
            text_view_date_place_visited.text = Utils.getDateToView(place.date)
            switch_block_visited_place.isChecked = place.blocked

            image_view_visited_place.visibility = View.VISIBLE

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
}
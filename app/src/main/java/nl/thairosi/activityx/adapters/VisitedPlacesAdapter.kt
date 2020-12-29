package nl.thairosi.activityx.adapters

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

class VisitedPlacesAdapter : RecyclerView.Adapter<VisitedPlacesAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(place.photo).into(image_view_visited_place)
            text_view_name_visited_place.text = place.name
            text_view_date_place_visited.text = place.date?.toLocaleString()
            switch_block_visited_place.isChecked = place.blocked

            setOnClickListener {
                onItemClickListener?.let { it(place) }
            }

        }
    }

    // To do

    fun setOnItemClickListener(listener: (Place) -> Unit) {
        onItemClickListener = listener
    }

}
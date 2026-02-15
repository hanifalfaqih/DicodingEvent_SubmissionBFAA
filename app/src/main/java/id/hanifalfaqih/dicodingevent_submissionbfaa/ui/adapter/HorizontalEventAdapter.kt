package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.response.EventItem
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.ItemEventHorizontalBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.loadImage

class HorizontalEventAdapter(private val onItemClick: (EventItem) -> Unit) :
    ListAdapter<EventItem, HorizontalEventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(private val binding: ItemEventHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventItem) {
            binding.apply {
                tvEventName.text = event.name

                ivEventImage.loadImage(event.mediaCover)

                root.setOnClickListener {
                    onItemClick(event)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventItem>() {
            override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

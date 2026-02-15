package id.hanifalfaqih.dicodingevent_submissionbfaa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.hanifalfaqih.dicodingevent_submissionbfaa.R
import id.hanifalfaqih.dicodingevent_submissionbfaa.data.local.entity.FavoriteEventEntity
import id.hanifalfaqih.dicodingevent_submissionbfaa.databinding.ItemFavoriteEventBinding
import id.hanifalfaqih.dicodingevent_submissionbfaa.utils.loadImage

class FavoriteEventAdapter(
    private val onItemClick: (FavoriteEventEntity) -> Unit,
    private val onDeleteClick: (FavoriteEventEntity) -> Unit
) : ListAdapter<FavoriteEventEntity, FavoriteEventAdapter.FavoriteEventViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding =
            ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteEventViewHolder(private val binding: ItemFavoriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEventEntity) {
            binding.apply {
                tvEventName.text = event.name
                tvEventSummary.text = event.summary
                ivEventImage.loadImage(event.imageLogo)

                root.setOnClickListener {
                    onItemClick(event)
                }
                btnDelete.setOnClickListener {
                    onDeleteClick(event)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEventEntity,
                newItem: FavoriteEventEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEventEntity,
                newItem: FavoriteEventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

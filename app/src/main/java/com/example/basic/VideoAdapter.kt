package com.example.basic

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basic.databinding.VideoItemBinding
import com.example.basic.utils.CountProcess
import kotlin.math.min
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class VideoAdapter(
    var videos: List<Video>,
    private val clickListener: VideoClickListener
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videos[position], clickListener)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    class ViewHolder private constructor(private val binding: VideoItemBinding,
                                         private val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video, clickListener: VideoClickListener) {
            binding.titleTextView.text = item.title
            binding.channelNameTextView.text = item.channel_name
            binding.likesTextView.text = CountProcess.processCount(item.likes)
            binding.viewsTextView.text = CountProcess.processCount(item.views)

            Glide.with(context)
                .load(item.thumbnail)
                .into(binding.thumbnailImageView)

            binding.root.setOnClickListener {
                clickListener.onClick(item.id)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VideoItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }
}

class VideoClickListener(val clickListener : (video : Int) -> Unit) {
    fun onClick(video : Int) = clickListener(video)
}
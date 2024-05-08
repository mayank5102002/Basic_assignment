package com.example.basic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.basic.databinding.FragmentVideoBinding
import com.example.basic.utils.CountProcess
import kotlin.math.min

class VideoFragment : Fragment() {

    private lateinit var binding : FragmentVideoBinding

    private var videoId : Int = -1

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        initArguments()
        initListeners()
    }

    private fun initArguments(){
        val args = VideoFragmentArgs.fromBundle(requireArguments())
        videoId = args.videoId
        setVideo()
    }

    private fun initListeners(){
        binding.backButton.setOnClickListener{
            requireView().findNavController().navigateUp()
        }
        binding.readMoreTextView.setOnClickListener{
            binding.descriptionTextView.text = viewModel.getVideo(videoId)?.description
            binding.readMoreTextView.visibility = View.GONE
        }
    }

    private fun setVideo(){
        val video = viewModel.getVideo(videoId) ?: return
        binding.titleTextView.text = video.title
        binding.channelNameTextView.text = video.channel_name
        binding.likesTextView.text = CountProcess.processCount(video.likes)
        binding.viewsTextView.text = CountProcess.processCount(video.views)
        binding.descriptionTextView.text = video.description
        binding.readMoreTextView.visibility = View.GONE

        if(video.description.isEmpty()){
            binding.descriptionTextView.text = "No description available"
        }

        if(video.description.length > 200){
            val desc = video.description.substring(0, min(100, video.description.length)) + "..."
            binding.descriptionTextView.text = desc
            binding.readMoreTextView.visibility = View.VISIBLE
        }

        Glide.with(requireContext())
            .load(video.thumbnail)
            .into(binding.thumbnailImageView)
    }
}
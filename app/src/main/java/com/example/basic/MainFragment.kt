package com.example.basic

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.basic.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding : FragmentMainBinding

    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: VideoAdapter

    private var cancelButton = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        viewModel.generateSupabaseObject()
        enableProgressBar()
        initListeners()
        initObservers()
        recyclerView = binding.videoRecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        if(viewModel.videosData.isEmpty()){
            viewModel.generateData()
        } else {
            setAdapter()
        }
    }

    private fun initObservers() {
        viewModel.videosFetched.observe(viewLifecycleOwner) { fetched ->
            if (fetched) {
                setAdapter()
            }
            viewModel.videosFetched.postValue(false)
        }
    }

    private fun initListeners() {
        binding.videoSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cancelButton = true
                binding.searchButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_cancel_icon, null))
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                if(s.isNullOrBlank()){
                    cancelButton = false
                    binding.searchButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_icon, null))
                } else {
                    val filteredStationNames = getFilterNames(binding.videoSearch.text.toString())
                    adapter.videos = filteredStationNames
                    adapter.notifyDataSetChanged()
                }
            }
        })
        binding.searchButton.setOnClickListener{
            if(cancelButton){
                cancelButton = false
                binding.searchButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_search_icon, null))
                binding.videoSearch.text = null
                adapter.videos = viewModel.videosData
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun getFilterNames(query : String) : ArrayList<Video>{
        val filteredVideos = ArrayList<Video>()
        for(video in viewModel.videosData){
            if(video.title.contains(query, true) || video.channel_name.contains(query, true)){
                filteredVideos.add(video)
            }
        }
        return filteredVideos
    }

    private fun setAdapter() {
        adapter = VideoAdapter(viewModel.videosData, VideoClickListener {
            val action = MainFragmentDirections.actionMainFragmentToVideoFragment(it)
            requireView().findNavController().navigate(action)
        })
        recyclerView.adapter = adapter
        disableProgressBar()
    }

    private fun enableProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.videoRecyclerView.visibility = View.GONE
        binding.videoSearch.isEnabled = false
        binding.searchButton.isEnabled = false
    }

    private fun disableProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.videoRecyclerView.visibility = View.VISIBLE
        binding.videoSearch.isEnabled = true
        binding.searchButton.isEnabled = true
    }

}
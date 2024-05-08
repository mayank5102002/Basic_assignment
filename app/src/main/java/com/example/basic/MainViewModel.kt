package com.example.basic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    var videosData = mutableListOf<Video>()
    val videosFetched: MutableLiveData<Boolean> = MutableLiveData(false)

    lateinit var supabaseObject: SupabaseClient

    fun generateSupabaseObject() {
        viewModelScope.launch {
            supabaseObject = createSupabaseClient(
                "https://uzofyokrsxamnrsnlcxv.supabase.co",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InV6b2Z5b2tyc3hhbW5yc25sY3h2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMTY3NTQsImV4cCI6MjAzMDU5Mjc1NH0.PXFN_2cCYWmzz9SaubhaI1X7Ga2dKp-0-vw2Mkngxlo"){
                install(Postgrest)
            }
        }
    }

    fun generateData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for (i in supabaseObject.from("videos").select().decodeList<Video>()) {
                    videosData.add(i)
                }
                videosFetched.postValue(true)
            }
        }
    }

    fun getVideo(videoId: Int): Video? {
        for (video in videosData) {
            if (video.id == videoId) {
                return video
            }
        }
        return null
    }
}
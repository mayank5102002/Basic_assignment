package com.example.basic.utils

import kotlin.math.min

object CountProcess {

    fun processCount(count: Int): String {
        val second : String = when {
            count < 1000 -> ""
            count < 1000000 -> "${(count%1000).toDouble() / 1000}"
            count < 1000000000 -> "${(count%1000000).toDouble() / 1000000}"
            else -> "${(count%1000000000).toDouble() / 1000000000}"
        }

        if(second.length >= 3 && second[2] == '0'){
            return when {
                count < 1000000 -> "${count/1000}K"
                count < 1000000000 -> "${count/1000000}M"
                else -> "${count/1000000000}B"
            }
        }

        return when {
            count < 1000 -> "$count"
            count < 1000000 -> "${count/1000}.${second.subSequence(2, min(4, second.length))}K"
            count < 1000000000 -> "${count/1000000}.${second.subSequence(2, min(4, second.length))}M"
            else -> "${count/1000000000}.${second.subSequence(2, min(4, second.length))}B"
        }
    }

}
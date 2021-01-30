package com.example.podcastapp.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object DateUtils{
    fun JsonDateToShortDate(jsonDate: String?):String{
        if(jsonDate == null){
            return "-"
        }
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = inFormat.parse(jsonDate)
        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.ENGLISH)
        return outputFormat.format(date)
    }
}
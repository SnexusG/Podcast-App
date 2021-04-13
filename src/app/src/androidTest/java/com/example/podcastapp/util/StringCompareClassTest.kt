package com.example.podcastapp.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.podcastapp.R
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class StringCompareClassTest{

    private lateinit var stringCompareClass: StringCompareClass

    @Before
    fun setup(){
        stringCompareClass = StringCompareClass()
    }

    @Test
    fun testAppName_returnTrue(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = stringCompareClass.compareAppName(context, R.string.app_name, "PodcastApp")
        assertThat(result).isTrue()
    }
}
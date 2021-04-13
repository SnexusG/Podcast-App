package com.example.podcastapp.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SpeedUtilTest{

    @Test
    fun `speed above range`(){
        val result = SpeedUtil.checkSpeed(
            2.3f
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `speed below range`(){
        val result = SpeedUtil.checkSpeed(
            0.6f
        )
        assertThat(result).isFalse()
    }

}
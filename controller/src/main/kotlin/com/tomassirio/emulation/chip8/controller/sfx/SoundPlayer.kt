package com.tomassirio.emulation.chip8.controller.sfx

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

class SoundPlayer {
    private var clip: Clip? = null

    init {
        try {
            val audioStream = AudioSystem.getAudioInputStream(
                javaClass.getResourceAsStream("/beep.wav")?.let { BufferedInputStream(it) }
            )
            clip = AudioSystem.getClip().apply {
                open(audioStream)
            }
        } catch (e: Exception) {
            println("Error initializing sound: ${e.message}")
        }
    }

    fun play() {
        if (clip?.isRunning != true) {
            clip?.framePosition = 0
            clip?.loop(Clip.LOOP_CONTINUOUSLY)
        }
    }

    fun stop() {
        clip?.stop()
    }
}


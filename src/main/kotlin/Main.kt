package com.tomassirio

import com.tomassirio.cpu.CPUFactory

fun main() {
    val cpuFactory = CPUFactory()
    val cpu = cpuFactory.createCPU()

    cpu.debug()
}
package com.tomassirio

import com.tomassirio.cpu.CPUFactory


fun main() {
    val cpu = CPUFactory.createCPU()

    cpu.debug()
}
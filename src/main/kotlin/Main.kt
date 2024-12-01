package com.tomassirio

import com.tomassirio.factory.CPUFactory


fun main() {
    val cpu = CPUFactory.createCPU()

    cpu.debug()
}
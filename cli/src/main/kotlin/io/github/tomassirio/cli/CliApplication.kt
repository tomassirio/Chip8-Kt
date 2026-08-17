package io.github.tomassirio.cli

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CliApplication

fun main(args: Array<String>) {
    runApplication<CliApplication>(*args)
}

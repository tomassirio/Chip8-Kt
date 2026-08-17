# CLI Module Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a new Maven module `cli` that lets Chip8-KT be played in a terminal, built as a Spring Boot application, reusing the existing `controller` module (no JavaFX/`ui` dependency).

**Architecture:** A `@SpringBootApplication` with a single `ApplicationRunner` bean parses CLI args, builds a `SystemController` (same as the JavaFX `EmulatorUI` does), opens a JLine terminal in raw mode, and drives a plain `while` game loop that ticks the CPU, renders the display as ANSI text, and polls keyboard input non-blockingly. Held-key state (for `SKP`/`SKNP` opcodes) is simulated via a timeout: a key counts as "released" once no new keystroke for it has arrived within ~100ms, since terminals don't emit real key-up events.

**Tech Stack:** Kotlin (JVM 17 for this module), Maven, Spring Boot 3.3.4 (`spring-boot-starter` only, no web), JLine 3.26.3 (`jline-terminal` + `jline-terminal-jna`), JUnit 5 + AssertJ (already used project-wide).

## Global Constraints

- New module is named `cli`, added to root `pom.xml`'s `<modules>`, depends on `controller` version `1.0.1` — never on `ui`.
- This module overrides `kotlin.compiler.jvmTarget` to `17` locally (root default is `1.8`; Spring Boot 3 requires Java 17+).
- Kotlin classes that Spring needs to proxy (`@SpringBootApplication`, `@Component`, etc.) must be auto-opened via the `kotlin-maven-allopen` compiler plugin with the `spring` preset — Kotlin classes are `final` by default and Spring Boot's CGLIB enhancement of `@Configuration` classes fails otherwise.
- CLI args: `--rom=<path>` (required), `--cpu=CHIP8|SCHIP8` (default `CHIP8`), `--fps=<n>` (default `15`). No `--scale`, no `--debug`.
- Rendering: 2 terminal characters per CHIP-8 pixel (`██`/`  `), redraw via ANSI cursor-home (`[H`), never a full clear per frame.
- Held-key timeout is ~100ms, implemented as an injectable-time pure class (no wall-clock dependency in the class itself) so it's unit-testable.
- Terminal must always be restored to cooked mode (original `Attributes`) before the process exits, including on error — restoration lives in a `finally` block wrapping the game loop.
- A bad `--rom`/`--cpu` must fail with a clean stderr message + exit code 1, **before** the terminal is put into raw mode.
- Only pure-logic units get automated tests: `CliParamsFactory`, `KeyMapper`, `KeyHoldTracker`. Rendering, terminal I/O, and the game loop are thin glue verified by manually running the packaged jar against a real ROM in a real terminal (this repo's sandboxed test environment has no real TTY, so that final check is on whoever runs the plan interactively).

---

### Task 1: Module scaffold + Spring Boot smoke boot

**Files:**
- Modify: `pom.xml` (root) — add `cli` module, `spring.boot.version` and `jline.version` properties
- Create: `cli/pom.xml`
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/CliApplication.kt`

**Interfaces:**
- Produces: a packaged `cli/target/cli-1.0.1.jar` runnable with `java -jar`, and the `io.github.tomassirio.cli` package as the home for every class in later tasks.

- [ ] **Step 1: Add module + version properties to root `pom.xml`**

In `pom.xml`, add `<module>cli</module>` to the `<modules>` block (after `<module>chip8-app</module>`):

```xml
    <modules>
        <module>system</module>
        <module>ui</module>
        <module>controller</module>
        <module>chip8-app</module>
        <module>cli</module>
    </modules>
```

Add two properties to the existing `<properties>` block (after `<codehaus.mojo.version>1.6.0</codehaus.mojo.version>`):

```xml
        <spring.boot.version>3.3.4</spring.boot.version>
        <jline.version>3.26.3</jline.version>
```

- [ ] **Step 2: Create `cli/pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>io.github.tomassirio</groupId>
        <artifactId>chip8-kt</artifactId>
        <version>${revision}${changelist}</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    <packaging>jar</packaging>

    <artifactId>cli</artifactId>
    <version>1.0.1</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <kotlin.code.style>official</kotlin.code.style>
        <kotlin.compiler.jvmTarget>17</kotlin.compiler.jvmTarget>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>io.github.tomassirio</groupId>
            <artifactId>controller</artifactId>
            <version>1.0.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.jline</groupId>
            <artifactId>jline-terminal</artifactId>
            <version>${jline.version}</version>
        </dependency>
        <dependency>
            <groupId>org.jline</groupId>
            <artifactId>jline-terminal-jna</artifactId>
            <version>${jline.version}</version>
        </dependency>

        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib</artifactId>
        </dependency>

        <!--Test-->
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-test-junit5</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src/main/kotlin</sourceDirectory>
        <testSourceDirectory>src/test/kotlin</testSourceDirectory>
        <plugins>
            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <configuration>
                    <compilerPlugins>
                        <plugin>spring</plugin>
                    </compilerPlugins>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-allopen</artifactId>
                        <version>${kotlin.stdlib.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>compile</id>
                        <phase>compile</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>test-compile</id>
                        <phase>test-compile</phase>
                        <goals>
                            <goal>test-compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
            </plugin>
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring.boot.version}</version>
                <configuration>
                    <mainClass>io.github.tomassirio.cli.CliApplicationKt</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: Create the Spring Boot entry point**

`cli/src/main/kotlin/io/github/tomassirio/cli/CliApplication.kt`:

```kotlin
package io.github.tomassirio.cli

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CliApplication

fun main(args: Array<String>) {
    runApplication<CliApplication>(*args)
}
```

- [ ] **Step 4: Build and smoke-test**

Run: `mvn -pl cli -am clean package -q`
Expected: `BUILD SUCCESS`, producing `cli/target/cli-1.0.1.jar`.

Run: `java -jar cli/target/cli-1.0.1.jar`
Expected: Spring Boot startup log lines, then the process exits cleanly (exit code `0`, no stack trace, no CGLIB/final-class enhancement error). There's no `ApplicationRunner` yet, so it starts the context and exits immediately — that's correct for this step.

- [ ] **Step 5: Commit**

```bash
git add pom.xml cli/pom.xml cli/src/main/kotlin/io/github/tomassirio/cli/CliApplication.kt
git commit -m "feat(cli): scaffold Spring Boot cli module"
```

---

### Task 2: CLI argument parsing (`CliParams` / `CliParamsFactory`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/params/CliParams.kt`
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/params/CliParamsFactory.kt`
- Test: `cli/src/test/kotlin/io/github/tomassirio/cli/params/CliParamsFactoryTest.kt`

**Interfaces:**
- Consumes: `io.github.tomassirio.system.cpu.CPUType.getByName(name: String): CPUType` (throws `CPUNotFoundException`, a subclass of `IllegalArgumentException`).
- Produces: `data class CliParams(val romPath: String, val cpuType: CPUType, val fps: Int)` and `object CliParamsFactory { fun fromArguments(args: org.springframework.boot.ApplicationArguments): CliParams }` — used by `EmulatorRunner` in Task 8.

- [ ] **Step 1: Write the failing tests**

`cli/src/test/kotlin/io/github/tomassirio/cli/params/CliParamsFactoryTest.kt`:

```kotlin
package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.DefaultApplicationArguments

class CliParamsFactoryTest {

    @Test
    fun `fromArguments with only rom uses defaults for cpu and fps`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG")

        val params = CliParamsFactory.fromArguments(args)

        assertThat(params.romPath).isEqualTo("roms/games/PONG")
        assertThat(params.cpuType).isEqualTo(CPUType.CHIP8)
        assertThat(params.fps).isEqualTo(15)
    }

    @Test
    fun `fromArguments reads cpu and fps when provided`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG", "--cpu=SCHIP8", "--fps=30")

        val params = CliParamsFactory.fromArguments(args)

        assertThat(params.cpuType).isEqualTo(CPUType.SCHIP8)
        assertThat(params.fps).isEqualTo(30)
    }

    @Test
    fun `fromArguments without rom throws IllegalArgumentException`() {
        val args = DefaultApplicationArguments()

        assertThrows<IllegalArgumentException> {
            CliParamsFactory.fromArguments(args)
        }
    }

    @Test
    fun `fromArguments with unknown cpu type throws exception`() {
        val args = DefaultApplicationArguments("--rom=roms/games/PONG", "--cpu=NOPE")

        assertThrows<IllegalArgumentException> {
            CliParamsFactory.fromArguments(args)
        }
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `mvn -pl cli -am test -Dtest=CliParamsFactoryTest -q`
Expected: compile error — `CliParamsFactory` and `CliParams` don't exist yet.

- [ ] **Step 3: Implement `CliParams` and `CliParamsFactory`**

`cli/src/main/kotlin/io/github/tomassirio/cli/params/CliParams.kt`:

```kotlin
package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType

data class CliParams(
    val romPath: String,
    val cpuType: CPUType,
    val fps: Int
)
```

`cli/src/main/kotlin/io/github/tomassirio/cli/params/CliParamsFactory.kt`:

```kotlin
package io.github.tomassirio.cli.params

import io.github.tomassirio.system.cpu.CPUType
import org.springframework.boot.ApplicationArguments

object CliParamsFactory {
    private const val ROM_ARG = "rom"
    private const val CPU_ARG = "cpu"
    private const val FPS_ARG = "fps"

    fun fromArguments(args: ApplicationArguments): CliParams {
        val romPath = args.getOptionValues(ROM_ARG)?.firstOrNull()
            ?: throw IllegalArgumentException("A Rom is required to run the emulator. Pass it with --rom=<path>")
        val cpuType = args.getOptionValues(CPU_ARG)?.firstOrNull()?.let { CPUType.getByName(it) }
            ?: CPUType.CHIP8
        val fps = args.getOptionValues(FPS_ARG)?.firstOrNull()?.toIntOrNull() ?: 15

        return CliParams(romPath, cpuType, fps)
    }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `mvn -pl cli -am test -Dtest=CliParamsFactoryTest -q`
Expected: `BUILD SUCCESS`, 4 tests passing.

- [ ] **Step 5: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/params cli/src/test/kotlin/io/github/tomassirio/cli/params
git commit -m "feat(cli): add CLI argument parsing"
```

---

### Task 3: Key mapping (`KeyMapper`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/mapping/KeyMapper.kt`
- Test: `cli/src/test/kotlin/io/github/tomassirio/cli/mapping/KeyMapperTest.kt`

**Interfaces:**
- Produces: `class KeyMapper { fun mapToChip8Key(key: Char): Char? }` — used by `EmulatorLoop` in Task 7.

- [ ] **Step 1: Write the failing test**

`cli/src/test/kotlin/io/github/tomassirio/cli/mapping/KeyMapperTest.kt`:

```kotlin
package io.github.tomassirio.cli.mapping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeyMapperTest {

    private val keyMapper = KeyMapper()

    @Test
    fun `maps physical keys to chip8 keys`() {
        assertThat(keyMapper.mapToChip8Key('1')).isEqualTo('1')
        assertThat(keyMapper.mapToChip8Key('4')).isEqualTo('C')
        assertThat(keyMapper.mapToChip8Key('q')).isEqualTo('4')
        assertThat(keyMapper.mapToChip8Key('r')).isEqualTo('D')
        assertThat(keyMapper.mapToChip8Key('z')).isEqualTo('A')
        assertThat(keyMapper.mapToChip8Key('v')).isEqualTo('F')
    }

    @Test
    fun `mapping is case insensitive`() {
        assertThat(keyMapper.mapToChip8Key('Q')).isEqualTo('4')
    }

    @Test
    fun `unmapped key returns null`() {
        assertThat(keyMapper.mapToChip8Key('k')).isNull()
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -pl cli -am test -Dtest=KeyMapperTest -q`
Expected: compile error — `KeyMapper` doesn't exist yet.

- [ ] **Step 3: Implement `KeyMapper`**

`cli/src/main/kotlin/io/github/tomassirio/cli/mapping/KeyMapper.kt`:

```kotlin
package io.github.tomassirio.cli.mapping

class KeyMapper {
    fun mapToChip8Key(key: Char): Char? = keyMap[key.lowercaseChar()]

    private val keyMap = mapOf(
        '1' to '1', '2' to '2', '3' to '3', '4' to 'C',
        'q' to '4', 'w' to '5', 'e' to '6', 'r' to 'D',
        'a' to '7', 's' to '8', 'd' to '9', 'f' to 'E',
        'z' to 'A', 'x' to '0', 'c' to 'B', 'v' to 'F'
    )
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -pl cli -am test -Dtest=KeyMapperTest -q`
Expected: `BUILD SUCCESS`, 3 tests passing.

- [ ] **Step 5: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/mapping cli/src/test/kotlin/io/github/tomassirio/cli/mapping
git commit -m "feat(cli): add terminal key mapping"
```

---

### Task 4: Held-key timeout tracking (`KeyHoldTracker`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/input/KeyHoldTracker.kt`
- Test: `cli/src/test/kotlin/io/github/tomassirio/cli/input/KeyHoldTrackerTest.kt`

**Interfaces:**
- Produces: `class KeyHoldTracker(timeoutMillis: Long = 100L) { fun keyReceived(key: Char, now: Long): Boolean; fun releaseExpired(now: Long): List<Char> }` — used by `EmulatorLoop` in Task 7. `now` is always caller-supplied (no internal clock), which is what makes this unit-testable.

- [ ] **Step 1: Write the failing tests**

`cli/src/test/kotlin/io/github/tomassirio/cli/input/KeyHoldTrackerTest.kt`:

```kotlin
package io.github.tomassirio.cli.input

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeyHoldTrackerTest {

    @Test
    fun `keyReceived returns true for a new press and false while still held`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)

        assertThat(tracker.keyReceived('A', now = 0L)).isTrue()
        assertThat(tracker.keyReceived('A', now = 10L)).isFalse()
    }

    @Test
    fun `releaseExpired keeps key pressed within timeout`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)

        val expired = tracker.releaseExpired(now = 50L)

        assertThat(expired).isEmpty()
    }

    @Test
    fun `releaseExpired releases key after timeout with no refresh`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)

        val expired = tracker.releaseExpired(now = 150L)

        assertThat(expired).containsExactly('A')
    }

    @Test
    fun `key can be pressed again as new press after being released`() {
        val tracker = KeyHoldTracker(timeoutMillis = 100L)
        tracker.keyReceived('A', now = 0L)
        tracker.releaseExpired(now = 150L)

        assertThat(tracker.keyReceived('A', now = 200L)).isTrue()
    }
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `mvn -pl cli -am test -Dtest=KeyHoldTrackerTest -q`
Expected: compile error — `KeyHoldTracker` doesn't exist yet.

- [ ] **Step 3: Implement `KeyHoldTracker`**

`cli/src/main/kotlin/io/github/tomassirio/cli/input/KeyHoldTracker.kt`:

```kotlin
package io.github.tomassirio.cli.input

class KeyHoldTracker(private val timeoutMillis: Long = 100L) {
    private val lastSeen = mutableMapOf<Char, Long>()

    fun keyReceived(key: Char, now: Long): Boolean {
        val isNewPress = !lastSeen.containsKey(key)
        lastSeen[key] = now
        return isNewPress
    }

    fun releaseExpired(now: Long): List<Char> {
        val expired = lastSeen.filterValues { now - it > timeoutMillis }.keys.toList()
        expired.forEach { lastSeen.remove(it) }
        return expired
    }
}
```

- [ ] **Step 4: Run tests to verify they pass**

Run: `mvn -pl cli -am test -Dtest=KeyHoldTrackerTest -q`
Expected: `BUILD SUCCESS`, 4 tests passing.

- [ ] **Step 5: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/input/KeyHoldTracker.kt cli/src/test/kotlin/io/github/tomassirio/cli/input/KeyHoldTrackerTest.kt
git commit -m "feat(cli): add held-key timeout tracking"
```

---

### Task 5: Terminal rendering (`TerminalRenderer`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/render/TerminalRenderer.kt`

**Interfaces:**
- Consumes: `io.github.tomassirio.system.io.display.DisplayState` — `isExtended(): Boolean`, `getPixel(x: Int, y: Int): Boolean`; `io.github.tomassirio.system.io.display.DisplayType.CHIP8/.SCHIP8` (`.width`, `.height`).
- Produces: `class TerminalRenderer(writer: java.io.PrintWriter) { fun render(display: DisplayState) }` — used by `EmulatorRunner` in Task 8 (constructed with `terminal.writer()`).

No automated test for this task — per the spec, rendering is thin glue verified by manual run in Task 8. This task's "test cycle" is a manual visual check.

- [ ] **Step 1: Implement `TerminalRenderer`**

`cli/src/main/kotlin/io/github/tomassirio/cli/render/TerminalRenderer.kt`:

```kotlin
package io.github.tomassirio.cli.render

import io.github.tomassirio.system.io.display.DisplayState
import io.github.tomassirio.system.io.display.DisplayType
import java.io.PrintWriter

private const val ON = "██"
private const val OFF = "  "
private const val CURSOR_HOME = "[H"

class TerminalRenderer(private val writer: PrintWriter) {
    fun render(display: DisplayState) {
        val (width, height) = if (display.isExtended()) {
            DisplayType.SCHIP8.width to DisplayType.SCHIP8.height
        } else {
            DisplayType.CHIP8.width to DisplayType.CHIP8.height
        }

        val frame = StringBuilder(CURSOR_HOME)
        for (y in 0 until height) {
            for (x in 0 until width) {
                frame.append(if (display.getPixel(x, y)) ON else OFF)
            }
            frame.append('\n')
        }

        writer.print(frame)
        writer.flush()
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `mvn -pl cli -am compile -q`
Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/render/TerminalRenderer.kt
git commit -m "feat(cli): add terminal display renderer"
```

---

### Task 6: Non-blocking terminal input (`TerminalInputReader`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/input/TerminalInputReader.kt`

**Interfaces:**
- Consumes: `org.jline.utils.NonBlockingReader` — `read(timeout: Long): Int` (returns a char code, or `NonBlockingReader.READ_EXPIRED` / `NonBlockingReader.EOF`).
- Produces: `class TerminalInputReader(reader: NonBlockingReader) { fun pollChars(): List<Char> }` — used by `EmulatorLoop` in Task 7 (constructed with `terminal.reader()`, which already returns a `NonBlockingReader`).

No automated test for this task — thin JLine glue, per spec verified manually in Task 8. A timeout of `1L` ms per read is used instead of `0L`: JLine's `read(0)` means "block forever", not "return immediately", so a 1ms timeout is used to get an effectively non-blocking poll without busy-looping.

- [ ] **Step 1: Implement `TerminalInputReader`**

`cli/src/main/kotlin/io/github/tomassirio/cli/input/TerminalInputReader.kt`:

```kotlin
package io.github.tomassirio.cli.input

import org.jline.utils.NonBlockingReader

class TerminalInputReader(private val reader: NonBlockingReader) {
    fun pollChars(): List<Char> {
        val chars = mutableListOf<Char>()
        while (true) {
            val c = reader.read(1L)
            if (c == NonBlockingReader.READ_EXPIRED || c == NonBlockingReader.EOF) break
            chars.add(c.toChar())
        }
        return chars
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `mvn -pl cli -am compile -q`
Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/input/TerminalInputReader.kt
git commit -m "feat(cli): add non-blocking terminal input reader"
```

---

### Task 7: Game loop (`EmulatorLoop`)

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/loop/EmulatorLoop.kt`

**Interfaces:**
- Consumes:
  - `io.github.tomassirio.controller.SystemController` — `tick()`, `getDisplayState(): DisplayState`, `onKeyPressed(chip8Key: Char)`, `onKeyReleased(chip8Key: Char)`.
  - `io.github.tomassirio.cli.render.TerminalRenderer` — `render(display: DisplayState)`.
  - `io.github.tomassirio.cli.input.TerminalInputReader` — `pollChars(): List<Char>`.
  - `io.github.tomassirio.cli.mapping.KeyMapper` — `mapToChip8Key(key: Char): Char?`.
  - `io.github.tomassirio.cli.input.KeyHoldTracker` — `keyReceived(key: Char, now: Long): Boolean`, `releaseExpired(now: Long): List<Char>`.
- Produces: `class EmulatorLoop(systemController, renderer, inputReader, keyMapper, keyHoldTracker, cyclesPerFrame: Int) { fun run() }` — used by `EmulatorRunner` in Task 8. `run()` blocks until `q` or `Esc` is read from input.

No automated test for this task — it's the top-level orchestration glue the spec calls out as manually verified (Task 8 runs it end-to-end against a real ROM).

- [ ] **Step 1: Implement `EmulatorLoop`**

`cli/src/main/kotlin/io/github/tomassirio/cli/loop/EmulatorLoop.kt`:

```kotlin
package io.github.tomassirio.cli.loop

import io.github.tomassirio.cli.input.KeyHoldTracker
import io.github.tomassirio.cli.input.TerminalInputReader
import io.github.tomassirio.cli.mapping.KeyMapper
import io.github.tomassirio.cli.render.TerminalRenderer
import io.github.tomassirio.controller.SystemController

private const val TARGET_FPS = 60
private const val NANOS_PER_FRAME = 1_000_000_000L / TARGET_FPS
private const val ESCAPE = 27.toChar()

class EmulatorLoop(
    private val systemController: SystemController,
    private val renderer: TerminalRenderer,
    private val inputReader: TerminalInputReader,
    private val keyMapper: KeyMapper,
    private val keyHoldTracker: KeyHoldTracker,
    private val cyclesPerFrame: Int
) {
    fun run() {
        var lastFrameTime = System.nanoTime()
        var running = true

        while (running) {
            val now = System.nanoTime()
            if (now - lastFrameTime >= NANOS_PER_FRAME) {
                repeat(cyclesPerFrame) { systemController.tick() }
                renderer.render(systemController.getDisplayState())
                running = handleInput()
                lastFrameTime = now
            } else {
                Thread.sleep(1)
            }
        }
    }

    private fun handleInput(): Boolean {
        val nowMillis = System.currentTimeMillis()

        for (char in inputReader.pollChars()) {
            if (char == 'q' || char == ESCAPE) return false

            val chip8Key = keyMapper.mapToChip8Key(char) ?: continue
            if (keyHoldTracker.keyReceived(chip8Key, nowMillis)) {
                systemController.onKeyPressed(chip8Key)
            }
        }

        keyHoldTracker.releaseExpired(nowMillis).forEach { systemController.onKeyReleased(it) }
        return true
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `mvn -pl cli -am compile -q`
Expected: `BUILD SUCCESS`.

- [ ] **Step 3: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/loop/EmulatorLoop.kt
git commit -m "feat(cli): add terminal game loop"
```

---

### Task 8: Wire it together (`EmulatorRunner`) and verify end-to-end

**Files:**
- Create: `cli/src/main/kotlin/io/github/tomassirio/cli/EmulatorRunner.kt`

**Interfaces:**
- Consumes everything produced by Tasks 2–7: `CliParamsFactory.fromArguments`, `SystemController` (via `io.github.tomassirio.system.cpu.factory.CPUFactory.createCPU(cpuType: CPUType): CPU` and `SystemController(cpu: CPU)`, `loadRom(data: ByteArray)`), `TerminalRenderer`, `TerminalInputReader`, `KeyMapper`, `KeyHoldTracker`, `EmulatorLoop`. Terminal via `org.jline.terminal.TerminalBuilder.builder().system(true).build()` — `Terminal.enterRawMode(): Attributes`, `Terminal.writer(): PrintWriter`, `Terminal.reader(): NonBlockingReader`, `Terminal.setAttributes(Attributes)`, `Terminal.close()`.
- Produces: nothing consumed further — this is the application's composition root.

- [ ] **Step 1: Implement `EmulatorRunner`**

`cli/src/main/kotlin/io/github/tomassirio/cli/EmulatorRunner.kt`:

```kotlin
package io.github.tomassirio.cli

import io.github.tomassirio.cli.input.KeyHoldTracker
import io.github.tomassirio.cli.input.TerminalInputReader
import io.github.tomassirio.cli.loop.EmulatorLoop
import io.github.tomassirio.cli.mapping.KeyMapper
import io.github.tomassirio.cli.params.CliParamsFactory
import io.github.tomassirio.cli.render.TerminalRenderer
import io.github.tomassirio.controller.SystemController
import io.github.tomassirio.system.cpu.factory.CPUFactory
import org.jline.terminal.TerminalBuilder
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.io.File
import kotlin.system.exitProcess

@Component
class EmulatorRunner : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val params = try {
            CliParamsFactory.fromArguments(args)
        } catch (e: IllegalArgumentException) {
            System.err.println("Error: ${e.message}")
            exitProcess(1)
        }

        val systemController = SystemController(CPUFactory.createCPU(params.cpuType))
        systemController.loadRom(File(params.romPath).readBytes())

        val terminal = TerminalBuilder.builder().system(true).build()
        val originalAttributes = terminal.enterRawMode()
        try {
            EmulatorLoop(
                systemController,
                TerminalRenderer(terminal.writer()),
                TerminalInputReader(terminal.reader()),
                KeyMapper(),
                KeyHoldTracker(),
                params.fps
            ).run()
        } finally {
            terminal.attributes = originalAttributes
            terminal.close()
        }
    }
}
```

- [ ] **Step 2: Build the full module**

Run: `mvn -pl cli -am clean package -q`
Expected: `BUILD SUCCESS`, all existing unit tests (Tasks 2–4) still pass, `cli/target/cli-1.0.1.jar` produced.

- [ ] **Step 3: Verify the missing-ROM error path (no terminal needed)**

Run: `java -jar cli/target/cli-1.0.1.jar`
Expected: stderr prints `Error: A Rom is required to run the emulator. Pass it with --rom=<path>`, process exits with code `1`, no stack trace, no terminal left in a broken state (none was opened).

Run: `java -jar cli/target/cli-1.0.1.jar --rom=roms/games/PONG --cpu=NOPE`
Expected: stderr prints an error from `CPUNotFoundException`'s message (unsupported CPU type), exit code `1`.

- [ ] **Step 4: Manual interactive verification (requires a real terminal)**

This step needs a real TTY and cannot be run from a non-interactive sandbox — do this from an actual terminal window:

Run: `java -jar cli/target/cli-1.0.1.jar --rom=roms/games/PONG`
Expected: the CHIP-8 display renders as block characters in the terminal, updating live; `1/2/q/w/a/s/z/x` etc. control the game per the `KeyMapper` layout; holding a movement key moves continuously (confirms the timeout-release heuristic works, not just single taps); pressing `q` or `Esc` exits cleanly and the shell prompt behaves normally afterward (confirms terminal attributes were restored).

- [ ] **Step 5: Commit**

```bash
git add cli/src/main/kotlin/io/github/tomassirio/cli/EmulatorRunner.kt
git commit -m "feat(cli): wire terminal emulator runner end-to-end"
```

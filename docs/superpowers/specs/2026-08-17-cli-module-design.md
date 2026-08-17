# CLI Module Design

**Date:** 2026-08-17
**Status:** Approved

## Goal

Add a second way to play the Chip8-KT emulator: a terminal (CLI) frontend, alongside the existing JavaFX `ui` module. Built as a new Maven module named `cli`, using Spring Boot.

## Module setup

New module `cli`, sibling to `system` / `ui` / `controller` / `chip8-app`, added to root `pom.xml` `<modules>`.

Dependencies:
- `controller` (reuses `SystemController`, `RomLoader`, `KeyHandler`, `SoundPlayer`) — does **not** depend on `ui`; no JavaFX involved.
- `spring-boot-starter` (via `spring-boot-dependencies` BOM import in `dependencyManagement`) — no web/data starters needed, this is a plain CLI app.
- `spring-boot-maven-plugin` for a runnable fat jar, parallel to how `chip8-app` uses `maven-shade-plugin`.
- `jline` (`jline-terminal` + a suitable jline provider, e.g. `jline-terminal-jansi` or `jline-native`) for raw terminal mode, non-blocking key reads, and cross-platform ANSI handling.

Entry point: a `@SpringBootApplication` class with a `CommandLineRunner` that reads ROM path / cpu type / fps from Spring Boot's `ApplicationArguments`, builds a `SystemController` (via `CPUFactory.createCPU`), and drives the render/input loop.

## Rendering

Terminal character cells are roughly twice as tall as wide, so each CHIP-8 pixel is printed as **2 characters wide** (`██` for on, `  ` for off) to keep the displayed image close to correct aspect ratio. One display row per terminal line.

Each frame: move the cursor to the top-left via the ANSI escape `[H` and overwrite the grid in place — no full screen clear per frame, to avoid flicker.

Both display modes are supported the same way `EmulatorUI` does it: check `DisplayState.isExtended()` each frame and use `DisplayType.SCHIP8` (128x64) dimensions instead of `DisplayType.CHIP8` (64x32) when extended mode is active.

There is no `--scale` option — scaling doesn't apply to terminal cells.

## Input handling

Use a JLine `Terminal` opened in raw mode. A non-blocking reader is polled once per frame for available input.

Key mapping: a CLI-local `KeyMapper` mirrors the JavaFX one's physical layout, mapping input characters to CHIP-8 keys:

```
1 2 3 4        1 2 3 C
q w e r   ->   4 5 6 D
a s d f        7 8 9 E
z x c v        A 0 B F
```

**Held-key simulation:** terminals don't emit a real key-up event. Each time a mapped character is read, `SystemController.onKeyPressed` fires (if not already marked pressed) and a per-key "last seen" timestamp is refreshed. This relies on the OS's own key-repeat while a key is physically held down, which resends the same character repeatedly. Once per frame, any currently-pressed key whose timestamp is older than a ~100ms timeout gets `SystemController.onKeyReleased` fired and is cleared. This timeout logic is extracted into a small pure class parameterized by a time source, so it's unit-testable without a real terminal.

`Esc` or `q` exits the loop. The terminal **must** be restored to cooked mode before the process exits — done in a `finally` block (and ideally also a JVM shutdown hook) so a crash or Ctrl+C doesn't leave the user's shell broken.

## Game loop

A plain `while` loop replaces JavaFX's `AnimationTimer`:
- Target 60 FPS, gated on `System.nanoTime()` deltas, same frame-budget math as `EmulatorUI`.
- `cyclesPerFrame` calls to `systemController.tick()` per frame (default 15, configurable).
- Render the frame.
- Poll input (see above).
- `Thread.sleep` any remaining frame budget to avoid busy-spinning.

## CLI arguments

Parsed via Spring Boot's `ApplicationArguments` — no need for a CLI-specific equivalent of `EmulatorParamsFactory`:

| Flag | Required | Default | Meaning |
|------|----------|---------|---------|
| `--rom=<path>` | yes | — | Path to the ROM file to load |
| `--cpu=CHIP8\|SCHIP8` | no | `CHIP8` | CPU/display mode, via `CPUType.getByName` |
| `--fps=<n>` | no | `15` | CPU cycles executed per rendered frame |

No `--debug` flag / debug pane in this first version — `CPUDebugger`'s `println` output would corrupt raw-mode terminal rendering. Out of scope for now.

## Error handling

- Missing or invalid `--rom` (or a `CPUNotFoundException` from a bad `--cpu` value): print a clear message to stderr and exit with code 1, **before** the terminal is switched into raw mode, so a bad invocation never leaves the shell in a broken state.
- Any exception raised during the loop: restore the terminal to cooked mode before propagating/exiting.

## Testing

- CLI `KeyMapper`: pure mapping, unit tested directly (mirrors the existing JavaFX `KeyMapper` test approach if one exists).
- Held-key timeout-release logic: extracted as a small pure class taking an injectable time source, unit tested for press/refresh/timeout-release behavior without needing a real terminal.
- Rendering and JLine I/O wiring are thin glue and are verified manually by running the CLI against a real ROM from `roms/games`, not covered by automated tests.

## Out of scope

- Debug pane / memory view (JavaFX-only for now).
- Sound is reused as-is from `controller.sfx.SoundPlayer` (already terminal-safe, no changes needed) — not re-verified in this design beyond that.
- `--scale` option (meaningless for terminal cells).

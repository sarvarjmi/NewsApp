# Macrobenchmark Configuration

This document describes the performance benchmarking setup for the NewsApp.

## Purpose
Macrobenchmarks measure high-level user interactions and ensure no performance regressions occur as the app evolves.

## Benchmarks

### 1. Startup Benchmarks
- **Metric**: `StartupTimingMetric`.
- **Target**: < 2 seconds COLD.
- **Coverage**: Time from process start to the first frame of the headlines feed.

### 2. Scroll Benchmarks
- **Metric**: `FrameTimingMetric`.
- **Target**: 60 FPS (Minimal dropped frames).
- **Coverage**: Measures jank during infinite scrolling of the news list.

## How to Run
- Run tests from the `:benchmark` module on a **physical device** in a controlled environment.
- Use the `benchmark` build type for accurate results.

# Macrobenchmark Configuration

This document describes the performance benchmarking setup for the NewsApp.

## Purpose
Macrobenchmarks measure high-level user interactions and ensure no performance regressions occur as the app evolves.

## Benchmarks
- **StartupBenchmark**: Measures time to first frame displayed.
- **ScrollBenchmark**: Measures frame timing and jank during list scrolling.

## How to Run
Run tests from the `:benchmark` module on a physical device.

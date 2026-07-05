# Performance Testing & Benchmarking

We use the `Macrobenchmark` library to measure high-level user interactions and ensure no regressions in performance.

---

## 1. Startup Benchmarks

Measures the time from process start to the first frame displayed.
- **Metric**: `StartupTimingMetric`.
- **Target**: < 2 seconds COLD.

---

## 2. Scroll Benchmarks

Measures frame timing and jank during infinite scrolling of the news feed.
- **Metric**: `FrameTimingMetric`.
- **Target**: 60 FPS (minimal dropped frames).

---

## 3. Memory Profiling

Use the **Android Studio Profiler** to check for:
- Memory Leaks (LeakCanary integrated).
- Excessive allocations during list scrolling.
- WebView resource cleanup.

---

## 4. Automation

Benchmarks should be run on a physical device in a controlled environment for accurate results.

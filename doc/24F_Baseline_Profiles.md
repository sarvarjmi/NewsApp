# Baseline Profiles Implementation

Baseline Profiles are a list of classes and methods included in an APK used by Android Runtime (ART) during installation to pre-compile critical path to machine code. This improves app startup and reduces jank.

---

## 1. Setup

We have created a `:benchmark` module to generate these profiles.

### Configuration
- `androidx.baselineprofile` plugin applied.
- `profileinstaller` dependency added to `:app`.

---

## 2. Profile Generation

To generate a new profile, run:
```bash
./gradlew :app:generateBaselineProfile
```

---

## 3. Critical User Journeys (CUJs)

Our profile covers:
- **Cold Startup**: Optimized through `BaselineProfileGenerator`.
- **Headlines Scrolling**: Optimized by flinging the `news_list` during collection.
- **Search Interaction**: (Future) Add search CUJs.

---

## 4. Verification

Use the `:benchmark` module to run `ScrollBenchmark` and compare results before and after profile application.

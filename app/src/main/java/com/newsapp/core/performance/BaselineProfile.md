# Baseline Profile Configuration

This document describes the Baseline Profile setup for the NewsApp.

## Purpose
Baseline Profiles improve app performance by pre-compiling critical code paths (like startup and scrolling) during installation.

## Generation
Profiles are generated using the `:benchmark` module.
Run: `./gradlew :app:generateBaselineProfile`

## CUJs Covered
- Cold Startup
- Headlines List Scrolling
- Category Switching

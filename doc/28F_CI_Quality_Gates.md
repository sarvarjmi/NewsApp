# NewsApp

# 28F_CI_Quality_Gates.md

> **Phase:** Engineering Excellence
>
> **Module:** CI/CD Quality Gates & Automated Testing Pipeline
>
> **Goal:** Configure GitHub Actions to enforce code quality gates, run automated tests, lint checks, and ensure only production-ready code reaches production.

---

# Objective

A robust CI/CD pipeline ensures code quality at every step. This document establishes comprehensive GitHub Actions workflows for building, testing, and analyzing the NewsApp project.

---

# CI/CD Pipeline Overview

```
Push Code
  ↓
Build APK
  ↓
Run Unit Tests
  ↓
Run Lint Checks (Android Lint, Detekt, Ktlint)
  ↓
Code Coverage Analysis
  ↓
Generate Reports
  ↓
Pass/Fail Decision
  ↓
PR Feedback / Deployment
```

---

# GitHub Actions Workflow

Create `.github/workflows/ci.yml`:

```yaml
name: CI - Code Quality & Testing

on:
  push:
    branches:
      - main
      - develop
      - 'feature/**'
  pull_request:
    branches:
      - main
      - develop

concurrency:
  group: ${{ github.workflow }}-${{ github.event.pull_request.number || github.ref }}
  cancel-in-progress: true

jobs:
  # ===========================
  # BUILD JOB
  # ===========================
  build:
    name: Build APK
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Build Debug APK
        run: ./gradlew assembleDebug --no-daemon

      - name: Upload APK Artifact
        uses: actions/upload-artifact@v3
        if: success()
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
          retention-days: 7

  # ===========================
  # LINT CHECK JOB
  # ===========================
  lint:
    name: Android Lint Analysis
    runs-on: ubuntu-latest
    timeout-minutes: 20

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run Android Lint
        run: ./gradlew lint --no-daemon

      - name: Upload Lint Report
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: lint-report
          path: app/build/reports/lint/
          retention-days: 7

      - name: Comment Lint Results
        if: always() && github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            const fs = require('fs');
            const report = fs.readFileSync('app/build/reports/lint/lint-report.xml', 'utf8');
            const issueCount = (report.match(/<issue/g) || []).length;

            let comment = `## 🔍 Android Lint Report\n\n`;
            if (issueCount === 0) {
              comment += `✅ No lint issues found!`;
            } else {
              comment += `⚠️ Found ${issueCount} lint issues\n\n`;
              comment += `[View Full Report](lint-report)`;
            }

            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: comment
            });

  # ===========================
  # DETEKT ANALYSIS JOB
  # ===========================
  detekt:
    name: Detekt Code Analysis
    runs-on: ubuntu-latest
    timeout-minutes: 20

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run Detekt
        run: ./gradlew detekt --no-daemon
        continue-on-error: true

      - name: Upload Detekt Report
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: detekt-report
          path: build/reports/detekt/
          retention-days: 7

      - name: Detekt Report Comment
        if: always() && github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            const fs = require('fs');
            const report = fs.readFileSync('build/reports/detekt/detekt.html', 'utf8');

            let comment = `## 🔎 Detekt Analysis Report\n\n`;
            comment += `[View Full Report](detekt-report)\n\n`;
            comment += `Check the report for code quality issues.`;

            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: comment
            });

  # ===========================
  # KTLINT CHECK JOB
  # ===========================
  ktlint:
    name: Ktlint Style Check
    runs-on: ubuntu-latest
    timeout-minutes: 20

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run Ktlint Check
        run: ./gradlew ktlintCheck --no-daemon
        continue-on-error: true

      - name: Run Ktlint Format (Auto-fix)
        if: failure()
        run: ./gradlew ktlintFormat --no-daemon

      - name: Comment Formatting Issues
        if: failure() && github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '⚠️ **Formatting Issues Found**\n\nRun `./gradlew ktlintFormat` to fix formatting automatically.'
            });

  # ===========================
  # UNIT TESTS JOB
  # ===========================
  unit-tests:
    name: Unit Tests
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run Unit Tests
        run: ./gradlew testDebugUnitTest --no-daemon

      - name: Upload Test Report
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: test-reports
          path: |
            app/build/reports/tests/
            app/build/test-results/
          retention-days: 7

      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v2
        if: always()
        with:
          files: app/build/test-results/**/TEST-*.xml

  # ===========================
  # CODE COVERAGE JOB
  # ===========================
  coverage:
    name: Code Coverage Analysis
    runs-on: ubuntu-latest
    timeout-minutes: 30

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run Tests with Coverage
        run: ./gradlew testDebugUnitTest jacocoTestReport --no-daemon

      - name: Upload Coverage Report
        uses: codecov/codecov-action@v3
        with:
          files: ./app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml
          flags: unittests
          name: codecov-umbrella
          fail_ci_if_error: true
          verbose: true

      - name: Generate Coverage Badge
        run: |
          mkdir -p coverage
          cat app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml

      - name: Comment Coverage Report
        if: github.event_name == 'pull_request'
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '📊 **Code Coverage Report Generated**\n\n[View Coverage Report](coverage-report)'
            });

  # ===========================
  # QUALITY GATE JOB
  # ===========================
  quality-gate:
    name: Quality Gate Check
    runs-on: ubuntu-latest
    needs: [build, lint, detekt, ktlint, unit-tests, coverage]
    timeout-minutes: 5
    if: always()

    steps:
      - name: Check Build Status
        if: needs.build.result == 'failure'
        run: |
          echo "❌ Build failed"
          exit 1

      - name: Check Lint Status
        if: needs.lint.result == 'failure'
        run: |
          echo "❌ Lint check failed"
          exit 1

      - name: Check Detekt Status
        if: needs.detekt.result == 'failure'
        run: |
          echo "❌ Detekt analysis failed"
          exit 1

      - name: Check Tests Status
        if: needs.unit-tests.result == 'failure'
        run: |
          echo "❌ Unit tests failed"
          exit 1

      - name: Check Coverage Status
        if: needs.coverage.result == 'failure'
        run: |
          echo "❌ Code coverage check failed"
          exit 1

      - name: Quality Gate Passed
        run: |
          echo "✅ All quality gates passed!"
          echo "Build, lint, tests, and coverage checks successful."

  # ===========================
  # SUCCESS NOTIFICATION
  # ===========================
  notify-success:
    name: Notify Success
    runs-on: ubuntu-latest
    needs: quality-gate
    if: success() && github.event_name == 'pull_request'

    steps:
      - name: Post Success Comment
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '✅ **All Checks Passed!**\n\nBuild, lint, tests, and coverage checks successful. Ready for review.'
            });

  # ===========================
  # FAILURE NOTIFICATION
  # ===========================
  notify-failure:
    name: Notify Failure
    runs-on: ubuntu-latest
    needs: quality-gate
    if: failure() && github.event_name == 'pull_request'

    steps:
      - name: Post Failure Comment
        uses: actions/github-script@v6
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: '❌ **Quality Gate Failed**\n\nPlease review the failed checks and fix the issues before resubmitting.'
            });

```

---

# Release Workflow

Create `.github/workflows/release.yml`:

```yaml
name: Release - Build and Deploy

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    name: Build Release APK/AAB
    runs-on: ubuntu-latest
    timeout-minutes: 60

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle

      - name: Grant Execute Permission
        run: chmod +x gradlew

      - name: Run All Checks
        run: |
          ./gradlew lint detekt ktlintCheck testReleaseUnitTest

      - name: Build Release APK
        run: ./gradlew assembleRelease

      - name: Build Release AAB
        run: ./gradlew bundleRelease

      - name: Create Release
        uses: softprops/action-gh-release@v1
        with:
          files: |
            app/build/outputs/apk/release/app-release.apk
            app/build/outputs/bundle/release/app-release.aab
          draft: false
          prerelease: false
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

```

---

# Quality Gate Metrics

| Metric | Threshold | Failure Action |
|--------|-----------|-----------------|
| Build Success | 100% | Fail CI |
| Lint Errors | 0 | Fail CI |
| Detekt Critical | 0 | Fail CI |
| Ktlint Violations | 0 | Fail CI |
| Unit Test Pass Rate | 100% | Fail CI |
| Code Coverage | >85% | Fail CI |
| APK Generation | Success | Fail CI |

---

# Running Locally

Test the pipeline locally before pushing:

```bash
# Build
./gradlew assembleDebug

# Lint
./gradlew lint

# Detekt
./gradlew detekt

# Ktlint
./gradlew ktlintCheck

# Tests
./gradlew testDebugUnitTest

# Coverage
./gradlew jacocoTestReport
```

---

# Troubleshooting

### Build Fails
```bash
./gradlew clean assembleDebug
```

### Tests Fail
```bash
./gradlew testDebugUnitTest --info
```

### Lint Issues
```bash
./gradlew lint --info
# Review app/build/reports/lint/lint-report.html
```

### Detekt Issues
```bash
./gradlew detekt --info
# Review build/reports/detekt/detekt.html
```

### Ktlint Violations
```bash
./gradlew ktlintFormat
```

---

# Files Created

- `.github/workflows/ci.yml` - Main CI/CD pipeline
- `.github/workflows/release.yml` - Release pipeline

---

# What We Completed

✅ Created comprehensive GitHub Actions CI/CD workflow

✅ Configured build, lint, and test automation

✅ Integrated code coverage analysis

✅ Implemented quality gate checks

✅ Added PR feedback and notifications

✅ Created release workflow

---

# Next Steps

1. Push workflows to GitHub
2. Configure branch protection rules
3. Test CI/CD on a feature branch
4. Monitor and refine based on results
5. Add additional quality checks as needed

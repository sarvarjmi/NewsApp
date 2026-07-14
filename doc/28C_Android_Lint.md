# NewsApp

# 28C_Android_Lint.md

> **Phase:** Engineering Excellence
>
> **Module:** Android Lint Configuration & Rules
>
> **Goal:** Configure Android Lint to enforce security, performance, accessibility, and composition best practices across the NewsApp project.

---

# Objective

Android Lint is a static analysis tool that identifies performance issues, accessibility problems, security vulnerabilities, and Android API misuse. This document establishes comprehensive Lint configuration for the NewsApp project.

---

# Android Lint Overview

Android Lint detects:

- Security vulnerabilities
- Performance anti-patterns
- Accessibility issues
- Compose best practices
- API misuse
- Resource problems
- Internationalization issues

---

# Configuration: lint.xml

Create `config/lint.xml` in the project root:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<lint>
    <!-- Set baseline for lint issues -->
    <!-- <ignore path="path/to/ignore" /> -->

    <!-- Enable all checks -->
    <default severity="warning" />

    <!-- ============================= -->
    <!-- CRITICAL RULES (FATAL)       -->
    <!-- ============================= -->

    <!-- Security -->
    <issue id="HardcodedDebugMode" severity="fatal" />
    <issue id="MissingPermission" severity="fatal" />
    <issue id="UsesCleartextTraffic" severity="fatal" />
    <issue id="InsecureProtectedComponent" severity="fatal" />
    <issue id="ExportedContentProvider" severity="fatal" />
    <issue id="ExportedService" severity="fatal" />
    <issue id="ExportedReceiver" severity="fatal" />
    <issue id="ExportedActivity" severity="fatal" />

    <!-- API Issues -->
    <issue id="NewApi" severity="fatal" />
    <issue id="InlinedApi" severity="fatal" />

    <!-- Correctness -->
    <issue id="MissingTranslation" severity="error" />
    <issue id="ExtraTranslation" severity="error" />

    <!-- ============================= -->
    <!-- ERROR RULES                  -->
    <!-- ============================= -->

    <!-- Compose -->
    <issue id="ComposableModifierMissing" severity="error" />
    <issue id="ComposableFunctionNaming" severity="error" />
    <issue id="ComposableEventParametersNaming" severity="error" />
    <issue id="ModifierComposable" severity="error" />
    <issue id="UnstableCollections" severity="error" />
    <issue id="MutableCollectionMutableExtensions" severity="error" />

    <!-- Performance -->
    <issue id="DisableBaselineAlignment" severity="error" />
    <issue id="VectorPath" severity="error" />
    <issue id="UseCompatLoadingForDrawables" severity="error" />

    <!-- Accessibility -->
    <issue id="ContentDescription" severity="error" />
    <issue id="ClickableViewAccessibility" severity="error" />
    <issue id="LabelFor" severity="error" />

    <!-- ============================= -->
    <!-- WARNING RULES                -->
    <!-- ============================= -->

    <!-- Compose Warnings -->
    <issue id="MultipleComposables" severity="warning" />
    <issue id="UnusedMaterialScaffoldPaddingParameter" severity="warning" />
    <issue id="ModifierMissing" severity="warning" />
    <issue id="ModifierWithoutDefault" severity="warning" />
    <issue id="ModifierReused" severity="warning" />
    <issue id="ContentTrailingLambda" severity="warning" />

    <!-- Performance Warnings -->
    <issue id="RtlHardcoded" severity="warning" />
    <issue id="UseValueOf" severity="warning" />
    <issue id="ObsoleteSdkInt" severity="warning" />
    <issue id="Deprecated" severity="warning" />
    <issue id="DrawAllocation" severity="warning" />
    <issue id="ViewConstructor" severity="warning" />
    <issue id="FieldGetter" severity="warning" />
    <issue id="StaticFieldLeak" severity="warning" />

    <!-- Accessibility Warnings -->
    <issue id="SmallSp" severity="warning" />
    <issue id="TextFields" severity="warning" />
    <issue id="TouchTargetSizeCheck" severity="warning" />
    <issue id="GetContentDescriptionOverride" severity="warning" />

    <!-- Resource Warnings -->
    <issue id="NotSibling" severity="warning" />
    <issue id="DuplicateIds" severity="warning" />
    <issue id="DuplicateDefinition" severity="warning" />
    <issue id="DuplicateIncludedIds" severity="warning" />
    <issue id="UnusedResources" severity="warning" />
    <issue id="UnusedIds" severity="warning" />
    <issue id="UnusedDimen" severity="warning" />

    <!-- Naming Warnings -->
    <issue id="ResourceName" severity="warning" />
    <issue id="TypographyDashes" severity="warning" />

    <!-- Layout Warnings -->
    <issue id="MissingDimen" severity="warning" />
    <issue id="IncludeLayoutParam" severity="warning" />
    <issue id="LayoutParams" severity="warning" />
    <issue id="MissingConstraints" severity="warning" />
    <issue id="MockLocation" severity="warning" />

    <!-- Data Binding Warnings -->
    <issue id="MissingDataBindingSetContentView" severity="warning" />
    <issue id="BindingResourceIdentifier" severity="warning" />

    <!-- Internationalization Warnings -->
    <issue id="HardcodedText" severity="warning" />
    <issue id="SelectableText" severity="warning" />

    <!-- ============================= -->
    <!-- DISABLED RULES               -->
    <!-- ============================= -->

    <!-- Too noisy for development -->
    <issue id="GooglePlayUnknownDependency" severity="ignore" />
    <issue id="GradleDependency" severity="ignore" />
    <issue id="NestedWeights" severity="ignore" />
    <issue id="MissingDimen" severity="ignore" />
    <issue id="MissingQuantityPlural" severity="ignore" />
    <issue id="PluralsCancel" severity="ignore" />
    <issue id="UnusedAttribute" severity="ignore" />
    <issue id="ExifInterface" severity="ignore" />
    <issue id="RtlCompatible" severity="ignore" />
    <issue id="AdapterViewChildren" severity="ignore" />
    <issue id="ProguardSplit" severity="ignore" />
    <issue id="AllowBackup" severity="ignore" />
    <issue id="UseAppTint" severity="ignore" />

    <!-- ============================= -->
    <!-- MODULE-SPECIFIC EXCLUSIONS   -->
    <!-- ============================= -->

    <!-- Ignore generated code -->
    <ignore path="**/build/**" />
    <ignore path="**/generated/**" />
    <ignore path="**/intermediates/**" />

    <!-- Ignore test directories -->
    <ignore path="**/test/**" />
    <ignore path="**/androidTest/**" />

</lint>
```

---

# Configuring lint.xml in build.gradle.kts

Add to `app/build.gradle.kts`:

```kotlin
android {
    lint {
        // Configuration file
        lintConfig = file("$rootDir/config/lint.xml")

        // Fail build on error
        abortOnError = true

        // Fail on warnings (strict mode)
        warningsAsErrors = false

        // Enable all checks
        checkAllWarnings = true

        // Disable specific checks
        disable.add("MissingTranslation")

        // Enable specific checks
        enable.add("ExtraTranslation")

        // Report paths
        htmlReport = true
        htmlOutput = file("$buildDir/reports/lint/lint-report.html")
        xmlReport = true
        xmlOutput = file("$buildDir/reports/lint/lint-report.xml")
        sarif = true
    }
}
```

---

# Key Lint Rules Explained

## Security Rules

### MissingPermission
Detects operations requiring permissions without declaring them in AndroidManifest.xml

```kotlin
// ❌ Risky: Missing permission check
@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun getLocation() {
    // ...
}

// ✅ Good: Declare permission
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

### UsesCleartextTraffic
Warns against unencrypted HTTP traffic

```kotlin
// ❌ Avoid: Cleartext traffic
val retrofit = Retrofit.Builder()
    .baseUrl("http://api.example.com/")
    .build()

// ✅ Good: Use HTTPS only
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/")
    .build()
```

### ExportedComponent
Prevents unintended component exposure

```kotlin
<!-- ❌ Bad: Exported without protection -->
<activity
    android:name=".MainActivity"
    android:exported="true" />

<!-- ✅ Good: Protected or not exported -->
<activity
    android:name=".MainActivity"
    android:exported="false" />

<!-- Or with permission -->
<activity
    android:name=".AdminActivity"
    android:exported="true"
    android:permission="com.example.ADMIN_PERMISSION" />
```

---

## Accessibility Rules

### ContentDescription
Images without descriptions are inaccessible

```kotlin
// ❌ Bad: Missing content description
Image(
    painter = painterResource(R.drawable.news_image),
    contentDescription = null
)

// ✅ Good: Provide meaningful description
Image(
    painter = painterResource(R.drawable.news_image),
    contentDescription = stringResource(R.string.article_image_description)
)

// Or decorative:
Image(
    painter = painterResource(R.drawable.decorative_divider),
    contentDescription = null,
    modifier = Modifier.size(24.dp)
)
```

### SmallSp
Font sizes below 12sp are hard to read

```kotlin
// ❌ Bad: Too small font
Text(
    text = "Small text",
    fontSize = 8.sp
)

// ✅ Good: Minimum 12sp for body text
Text(
    text = "Article preview",
    fontSize = 14.sp
)
```

### TouchTargetSizeCheck
Touch targets should be at least 48dp

```kotlin
// ❌ Bad: Too small
Button(
    onClick = { /* ... */ },
    modifier = Modifier.size(24.dp)
)

// ✅ Good: At least 48dp
Button(
    onClick = { /* ... */ },
    modifier = Modifier.size(48.dp)
)
```

---

## Compose Rules

### ComposableModifierMissing
Composables that accept UI modification should have a Modifier parameter

```kotlin
// ❌ Bad: Missing Modifier parameter
@Composable
fun NewsCard(article: NewsArticle) {
    Card {
        Text(article.title)
    }
}

// ✅ Good: Include Modifier parameter
@Composable
fun NewsCard(
    article: NewsArticle,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Text(article.title)
    }
}
```

### ComposableFunctionNaming
Composable names should start with uppercase

```kotlin
// ❌ Bad: Lowercase naming
@Composable
fun newsCard() { }

// ✅ Good: Uppercase naming
@Composable
fun NewsCard() { }
```

### UnstableCollections
Use immutable collections in Composables

```kotlin
// ❌ Bad: Mutable list
@Composable
fun NewsList(articles: MutableList<NewsArticle>) {
    LazyColumn {
        items(articles) { article ->
            NewsCard(article)
        }
    }
}

// ✅ Good: Immutable list
@Composable
fun NewsList(
    articles: List<NewsArticle>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(articles) { article ->
            NewsCard(article)
        }
    }
}
```

---

## Performance Rules

### DrawAllocation
Avoid allocating objects inside draw methods

```kotlin
// ❌ Bad: Allocation in onDraw
override fun onDraw(canvas: Canvas) {
    val paint = Paint() // Allocation!
    canvas.drawCircle(100f, 100f, 50f, paint)
}

// ✅ Good: Allocate in init
private val paint = Paint()

override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(100f, 100f, 50f, paint)
}
```

### StaticFieldLeak
Avoid static references to Contexts or Views

```kotlin
// ❌ Bad: Static context leak
companion object {
    private var context: Context? = null
}

// ✅ Good: Use Application context or weak references
companion object {
    private var appContext: WeakReference<Context>? = null
}
```

---

## Correctness Rules

### MissingTranslation
All strings should be translated to all languages

```xml
<!-- values/strings.xml -->
<string name="article_title">News Article</string>

<!-- values-es/strings.xml -->
<!-- MISSING! Will be flagged -->

<!-- Fix: Add translation -->
<!-- values-es/strings.xml -->
<string name="article_title">Artículo de Noticias</string>
```

---

# Running Lint

## Command Line

```bash
# Run lint analysis
./gradlew lint

# Run lint on specific module
./gradlew :app:lint

# Run lint and generate report
./gradlew lint --info
```

## In IDE

- Build → Analyze → Run Lint
- Right-click file/folder → Analyze → Run Inspection by Name → Lint

## CI Integration

```yaml
- name: Run Lint Check
  run: ./gradlew lint

- name: Upload Lint Report
  uses: actions/upload-artifact@v3
  with:
    name: lint-report
    path: app/build/reports/lint/
```

---

# Best Practices

1. **Fail on Errors**: Set `abortOnError = true` in build.gradle.kts
2. **Review Reports**: Check HTML/XML reports for details
3. **Fix Early**: Address lint issues during development, not before release
4. **Baseline**: Use `baseline.xml` to ignore legacy issues
5. **Custom Rules**: Create custom lint detectors for project-specific patterns

---

# Creating a Lint Baseline

To ignore existing issues and track only new ones:

```bash
./gradlew lintDebugBaseline
```

This creates `lint-baseline.xml` to suppress known issues.

---

# Files Modified/Created

- `config/lint.xml` - Main lint configuration
- `app/build.gradle.kts` - Gradle lint configuration

---

# What We Completed

✅ Created comprehensive lint.xml configuration

✅ Configured security, accessibility, and performance rules

✅ Documented all key lint rules

✅ Provided setup instructions and running commands

✅ Explained Compose-specific lint checks

✅ Showed examples of violations and fixes

---

# Lint Severity Levels

| Level | Action |
|-------|--------|
| FATAL | Build fails |
| ERROR | Warning during build |
| WARNING | Reported but doesn't fail build |
| IGNORE | Not reported |

---

# Next Steps

1. Run `./gradlew lint` to identify issues
2. Review lint report in `app/build/reports/lint/lint-report.html`
3. Fix critical and error-level issues
4. Integrate into CI/CD pipeline
5. Use baseline to manage legacy issues

# NewsApp

# 28B_Ktlint_Setup.md

> **Phase:** Engineering Excellence
>
> **Module:** Ktlint Formatting & Style Enforcement
>
> **Goal:** Configure Ktlint to automatically enforce Kotlin formatting standards, maintain consistent code style, and provide pre-commit hooks for automatic formatting.

---

# Objective

Ktlint is a linter and formatter for Kotlin that enforces coding style and automatically fixes formatting issues. This document establishes Ktlint configuration for the NewsApp project.

---

# Ktlint Overview

Ktlint:

- Enforces Kotlin coding style automatically
- Fixes formatting issues in-place
- Provides instant feedback in the IDE
- Integrates with pre-commit hooks
- Ensures consistency across the project

---

# Installation & Configuration

## Step 1: Add Ktlint Plugin to build.gradle.kts (Project Level)

```kotlin
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        version.set("1.1.1")
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        coloredOutput.set(true)

        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
            exclude("**/test/**")
        }

        kotlinScriptAdditionalPaths {
            include(project.fileTree("scripts/"))
        }
    }
}
```

## Step 2: Add Ktlint to App Level build.gradle.kts

```kotlin
plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    version.set("1.1.1")
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    disabledRules.set(setOf())

    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}
```

---

# Formatting Rules & Configuration

## EditorConfig Setup

Create `.editorconfig` in project root:

```ini
root = true

[*.{kt,kts}]
indent_size = 4
indent_style = space
max_line_length = 120
insert_final_newline = true
trim_trailing_whitespace = true
ij_kotlin_allow_trailing_comma = true
ij_kotlin_allow_trailing_comma_on_call_site = true

# Naming conventions
ij_kotlin_name_count_to_use_star_import = 5
ij_kotlin_name_count_to_use_star_import_for_members = 3

# Imports
ij_kotlin_imports_layout = *,java.**,javax.**,kotlin.**,^

# Line breaks
ij_kotlin_line_break_after_multiline_let = true
ij_kotlin_assignment_wrap = normal

# Spaces
ij_kotlin_spaces_around_when_keywords = true
ij_kotlin_continuation_indent_size = 4

# Composable
ij_kotlin_method_parameters_new_line_when_multiple = true
ij_kotlin_method_parameters_wrap = normal
ij_kotlin_method_call_arguments_wrap = normal
```

---

# Running Ktlint

## Format All Files

```bash
# Format all Kotlin files
./gradlew ktlintFormat

# Format specific module
./gradlew :app:ktlintFormat

# Check without fixing
./gradlew ktlintCheck

# Format only changed files
./gradlew ktlintCheck ktlintFormat
```

## IDE Integration

### Android Studio

1. Go to **Settings** → **Editor** → **Code Style**
2. Set **Scheme** to **Kotlin/Android Convention**
3. Ensure **Line length** is 120

### Pre-commit Hook Setup

Create `.git/hooks/pre-commit`:

```bash
#!/bin/bash
echo "Running ktlint..."
./gradlew ktlintCheck

if [ $? -ne 0 ]; then
    echo "Ktlint check failed. Running formatter..."
    ./gradlew ktlintFormat
    echo "Files formatted. Please review and stage again."
    exit 1
fi
exit 0
```

Make executable:

```bash
chmod +x .git/hooks/pre-commit
```

---

# Ktlint Rules Explained

## Import Organization

```kotlin
import java.util.*
import kotlin.collections.List
import androidx.compose.runtime.*
import com.example.newsapp.*
```

**Order:**
1. Java imports
2. Kotlin imports
3. Third-party imports (androidx, etc.)
4. Project imports

---

## Line Length & Wrapping

**Maximum line length:** 120 characters

```kotlin
// ✅ Good: Break long lines
val newsArticle = repository.fetchArticles(
    query = "kotlin",
    limit = 10,
    sortBy = "publishedAt"
)

// ❌ Avoid: Exceed line limit
val newsArticle = repository.fetchArticles(query = "kotlin", limit = 10, sortBy = "publishedAt")
```

---

## Indentation

**Standard:** 4 spaces

```kotlin
// ✅ Good: Proper indentation
fun loadNews() {
    viewModelScope.launch {
        val articles = repository.getArticles()
        updateState(articles)
    }
}

// ❌ Avoid: Inconsistent indentation
fun loadNews() {
  viewModelScope.launch {
        val articles = repository.getArticles()
    updateState(articles)
  }
}
```

---

## Spacing Rules

### Around Operators

```kotlin
// ✅ Good
val sum = a + b
val result = x * y
val isValid = flag && condition

// ❌ Avoid
val sum = a+b
val result = x*y
val isValid = flag&&condition
```

### Around Braces

```kotlin
// ✅ Good
if (condition) {
    doSomething()
}

// ❌ Avoid
if(condition){
    doSomething()
}
```

### In Function Parameters

```kotlin
// ✅ Good
fun doSomething(a: Int, b: String, c: Boolean) {
    // ...
}

// ❌ Avoid
fun doSomething(a:Int,b:String,c:Boolean) {
    // ...
}
```

---

## Naming Conventions

```kotlin
// ✅ Good
class NewsArticle { }
interface ArticleRepository { }
fun loadArticles() { }
val articleCount = 0
const val ARTICLE_LIMIT = 100

// ❌ Avoid
class news_article { }
interface IArticleRepository { }
fun Load_Articles() { }
val article_count = 0
const val articleLimit = 100
```

---

## Trailing Commas

```kotlin
// ✅ Good: Allowed with ktlint
data class News(
    val id: String,
    val title: String,
    val content: String,
)

// Also acceptable
data class News(
    val id: String,
    val title: String,
    val content: String
)
```

---

## No Wildcard Imports

```kotlin
// ✅ Good
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

// ❌ Avoid
import androidx.compose.foundation.layout.*
```

---

## Blank Lines

```kotlin
// ✅ Good: One blank line between functions
class NewsRepository {
    fun getArticles(): List<Article> {
        // ...
    }

    fun searchArticles(query: String): List<Article> {
        // ...
    }
}

// ❌ Avoid: No blank lines
class NewsRepository {
    fun getArticles(): List<Article> {
        // ...
    }
    fun searchArticles(query: String): List<Article> {
        // ...
    }
}
```

---

## Composable Specific Rules

```kotlin
// ✅ Good: Proper Composable formatting
@Composable
fun NewsArticleCard(
    article: NewsArticle,
    onArticleClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = article.title)
        }
    }
}

// ❌ Avoid: Overcrowded lines
@Composable fun NewsArticleCard(article: NewsArticle, onArticleClick: (NewsArticle) -> Unit, modifier: Modifier = Modifier) { Card(modifier = modifier.fillMaxWidth()) { Column(modifier = Modifier.padding(16.dp)) { Text(text = article.title) } } }
```

---

# Best Practices

1. **Run Before Commit**: Use pre-commit hook to format automatically
2. **Check Regularly**: Run `ktlintCheck` in CI pipeline
3. **IDE Integration**: Set up IDE formatter to match ktlint
4. **Team Agreement**: All team members use same formatter settings
5. **Don't Suppress**: Avoid overusing `@Suppress("ktlint:rule-name")`

---

# Gradle Tasks

| Task | Purpose |
|------|---------|
| `ktlintCheck` | Check for style violations |
| `ktlintFormat` | Automatically fix violations |
| `ktlintFormatCheck` | Check specific files |
| `ktlint` | Run all ktlint checks |

---

# Common Violations & Fixes

### Max Line Length Exceeded

```kotlin
// ❌ Before: 127 characters
val newsArticles = viewModelScope.launch { repository.fetchArticles("query", 10, "date").collect { updateState(it) } }

// ✅ After: Split into multiple lines
viewModelScope.launch {
    repository.fetchArticles("query", 10, "date")
        .collect { articles ->
            updateState(articles)
        }
}
```

### Imports Not Organized

```bash
./gradlew ktlintFormat
# Automatically fixes import ordering
```

### Spacing Issues

```bash
./gradlew ktlintFormat
# Automatically fixes all spacing issues
```

---

# Integration with CI/CD

Add to GitHub Actions workflow:

```yaml
- name: Run Ktlint Check
  run: ./gradlew ktlintCheck

- name: Fix Ktlint Violations
  if: failure()
  run: ./gradlew ktlintFormat

- name: Commit Formatted Changes
  if: failure()
  run: |
    git add -A
    git commit -m "style: auto-format with ktlint"
```

---

# Files Modified/Created

- `.editorconfig` - Editor configuration (create if doesn't exist)
- `build.gradle.kts` (root) - Updated with ktlint plugin
- `build.gradle.kts` (app) - Updated with ktlint configuration
- `.git/hooks/pre-commit` - Pre-commit hook (optional)

---

# What We Completed

✅ Added Ktlint to Gradle build

✅ Created comprehensive .editorconfig

✅ Documented all formatting rules

✅ Provided setup instructions and running commands

✅ Created pre-commit hook for automatic formatting

✅ Explained IDE integration steps

---

# Verification Commands

```bash
# Check style
./gradlew ktlintCheck

# Format all files
./gradlew ktlintFormat

# Verify formatting is applied
./gradlew ktlintCheck
```

---

# Next Steps

1. Run `./gradlew ktlintFormat` to format all existing code
2. Configure pre-commit hook for automatic formatting
3. Set up IDE formatter to match ktlint configuration
4. Integrate into CI/CD pipeline

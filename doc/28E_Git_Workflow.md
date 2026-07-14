# NewsApp

# 28E_Git_Workflow.md

> **Phase:** Engineering Excellence
>
> **Module:** Git Workflow & Collaborative Development
>
> **Goal:** Establish a consistent Git workflow with conventional commits, branch strategy, and pull request standards for the NewsApp project.

---

# Objective

A well-defined Git workflow ensures code quality, traceability, and team collaboration. This document establishes best practices for version control and collaborative development in the NewsApp project.

---

# Git Workflow Overview

```
main (production-ready)
  ↑
develop (integration branch)
  ↑
feature/* (feature development)
release/* (release preparation)
hotfix/* (production fixes)
```

---

# Branch Strategy

## Main Branch (`main`)

- **Purpose**: Production-ready code
- **Protection**: Require pull request reviews
- **Deployment**: Automatic deployment to production
- **Tags**: Version tags (v1.0.0, v1.0.1, etc.)

```bash
# Never commit directly to main
# Always go through develop branch
```

## Develop Branch (`develop`)

- **Purpose**: Integration branch for features
- **Merging from**: feature/*, release/*, hotfix/*
- **Merging to**: Tested before merging to main
- **CI/CD**: Run full test suite

```bash
# Create from main initially
git checkout -b develop main

# Protect this branch - require reviews
```

## Feature Branches (`feature/*`)

- **Naming**: `feature/feature-name` (kebab-case)
- **Created from**: `develop`
- **Merged back to**: `develop`
- **Lifetime**: Delete after merge

```bash
# Create feature branch
git checkout -b feature/implement-bookmark-screen develop

# Work on feature
git add .
git commit -m "feat: add bookmark screen UI"

# Push to remote
git push -u origin feature/implement-bookmark-screen

# Create pull request to develop
```

## Release Branches (`release/*`)

- **Naming**: `release/v1.x.0` (semantic versioning)
- **Created from**: `develop` when ready for release
- **Purpose**: Prepare release, fix bugs, bump version
- **Merged to**: Both `main` and back to `develop`

```bash
# Prepare release
git checkout -b release/v1.0.0 develop

# Update version numbers
# Fix critical bugs only

# Create pull request to main
```

## Hotfix Branches (`hotfix/*`)

- **Naming**: `hotfix/critical-bug-fix`
- **Created from**: `main`
- **Purpose**: Emergency fixes for production issues
- **Merged to**: Both `main` and `develop`

```bash
# Emergency fix
git checkout -b hotfix/fix-login-crash main

# Fix and test
git add .
git commit -m "fix: resolve login crash on Android 12"

# Create pull request to main
```

---

# Conventional Commits

All commits follow the Conventional Commits specification.

## Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

## Types

| Type | Purpose |
|------|---------|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation changes |
| `style` | Formatting (no logic change) |
| `refactor` | Code restructuring (no logic change) |
| `perf` | Performance improvements |
| `test` | Test additions/updates |
| `chore` | Build, dependencies, tooling |
| `ci` | CI/CD configuration |

## Scopes

Scopes relate to project modules:

| Scope | Description |
|-------|-------------|
| `home` | Home screen feature |
| `search` | Search feature |
| `detail` | Article detail feature |
| `bookmark` | Bookmark feature |
| `network` | Network layer |
| `database` | Database layer |
| `di` | Dependency injection |
| `ui` | UI/design system |
| `navigation` | Navigation logic |
| `performance` | Performance optimization |

## Examples

### Feature Commit

```bash
git commit -m "feat(bookmark): add toggle bookmark functionality"
```

Body:
```
Implement bookmark toggle in BookmarkUseCase.

Users can now click the bookmark icon to add/remove articles
from their bookmark collection. Changes persist to local database.

Closes #123
```

### Bug Fix Commit

```bash
git commit -m "fix(search): resolve pagination reset on query change"
```

### Performance Improvement

```bash
git commit -m "perf(home): optimize LazyColumn rendering with key"
```

### Documentation Update

```bash
git commit -m "docs: update architecture guide for new data flow"
```

### Refactor

```bash
git commit -m "refactor(repository): extract data mapping logic"
```

## Full Example

```bash
git commit -m "feat(home): implement infinite scroll pagination

- Add PagingData integration to NewsArticleSource
- Update HomeViewModel to handle page changes
- Implement LoadState handling for loading/error states
- Add LoadStateFooter for page state indicators

Previously pagination was manual with Next/Previous buttons.
Now the list automatically loads more articles when scrolling
near the end of the list.

Fixes #456
Closes #789"
```

---

# Pull Request Standards

## PR Title Format

Follow conventional commits format:

```
feat(scope): Brief description

fix(scope): Brief description

docs: Brief description
```

## PR Description Template

Create `.github/pull_request_template.md`:

```markdown
## Description
Brief description of changes made

## Type of Change
- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update
- [ ] Performance improvement
- [ ] Refactoring

## Related Issues
Closes #(issue number)

## Testing Done
- [ ] Unit tests added/updated
- [ ] Manual testing performed
- [ ] Device tested: (API level, device type)

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] No new warnings generated
- [ ] Tests pass locally
- [ ] Lint passes (`./gradlew lint`)
- [ ] Detekt passes (`./gradlew detekt`)
- [ ] Ktlint passes (`./gradlew ktlintCheck`)

## Screenshots (if UI changes)
<!-- Add before/after screenshots -->
```

## PR Review Checklist

Reviewers must verify:

- [ ] **Code Quality**
  - Follows naming conventions
  - No code smells or duplicates
  - Proper error handling
  - Null safety

- [ ] **Architecture**
  - Clean architecture followed
  - MVVM pattern implemented
  - Dependency injection used
  - Proper layer separation

- [ ] **Testing**
  - Unit tests provided
  - Test coverage >85%
  - Edge cases handled
  - Tests are meaningful

- [ ] **Performance**
  - No memory leaks
  - No unnecessary allocations
  - Efficient queries
  - Proper coroutine scoping

- [ ] **Accessibility**
  - Content descriptions provided
  - 48dp minimum touch targets
  - Proper contrast ratios
  - Semantic accessibility

- [ ] **Security**
  - No hardcoded secrets
  - Proper permission handling
  - Input validation
  - HTTPS only

---

# Git Configuration

## Configure Git User

```bash
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

## Global Configuration

```bash
# Configure global user (optional)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Set default branch name
git config --global init.defaultBranch main

# Enable autostash during rebase
git config --global rebase.autostash true
```

---

# Workflow Examples

## Creating a Feature

```bash
# 1. Update develop and create feature branch
git checkout develop
git pull origin develop
git checkout -b feature/add-search-filters

# 2. Make changes
# Edit files...

# 3. Commit with conventional format
git add src/
git commit -m "feat(search): add category filter to search"

# 4. Push to remote
git push -u origin feature/add-search-filters

# 5. Create pull request on GitHub
# (Include description following PR template)

# 6. After review and approval, merge
# (Use "Squash and merge" for clean history)
```

## Updating Feature with Develop Changes

```bash
# Fetch latest develop
git checkout develop
git pull origin develop

# Rebase feature on top of develop
git checkout feature/my-feature
git rebase develop

# Resolve conflicts if any
# git add .
# git rebase --continue

# Force push to remote
git push origin feature/my-feature --force-with-lease
```

## Creating a Release

```bash
# 1. Create release branch from develop
git checkout -b release/v1.1.0 develop

# 2. Update version numbers
# Edit build.gradle.kts, AndroidManifest.xml, etc.

# 3. Commit version bump
git commit -m "chore: bump version to 1.1.0"

# 4. Push and create PR to main
git push -u origin release/v1.1.0

# 5. After testing and approval, merge to main
# 6. Tag the release
git tag -a v1.1.0 -m "Release version 1.1.0"
git push origin v1.1.0

# 7. Merge back to develop
git checkout develop
git merge release/v1.1.0
git push origin develop

# 8. Delete release branch
git branch -d release/v1.1.0
git push origin --delete release/v1.1.0
```

## Creating a Hotfix

```bash
# 1. Create hotfix branch from main
git checkout -b hotfix/critical-crash main

# 2. Fix the issue
# Edit files...

# 3. Commit fix
git commit -m "fix(crash): prevent NullPointerException in HomeScreen"

# 4. Push and create PR to main
git push -u origin hotfix/critical-crash

# 5. After approval, merge to main
# 6. Tag the hotfix
git tag -a v1.0.1 -m "Hotfix version 1.0.1"
git push origin v1.0.1

# 7. Merge back to develop
git checkout develop
git merge hotfix/critical-crash
git push origin develop

# 8. Delete hotfix branch
git branch -d hotfix/critical-crash
git push origin --delete hotfix/critical-crash
```

---

# Commit Best Practices

1. **Atomic Commits**: Each commit should be a complete logical unit
2. **Descriptive Messages**: Use conventional commits format
3. **Frequent Commits**: Commit often, push regularly
4. **Rebase Before Push**: Keep history clean

```bash
# Squash multiple commits before pushing
git rebase -i HEAD~3
# Mark 2nd and 3rd commits as 'squash'
```

5. **Review Before Committing**: Use `git diff` to review changes

```bash
git diff          # Changes not staged
git diff --staged # Changes staged for commit
```

---

# Tags

Create tags for releases and milestones:

```bash
# Create annotated tag
git tag -a v1.0.0 -m "NewsApp v1.0.0 - Production release"

# Push tag
git push origin v1.0.0

# Push all tags
git push origin --tags

# List tags
git tag -l
```

---

# Protection Rules

Configure branch protection on GitHub:

For `main`:
- [ ] Require pull request reviews (2 reviewers)
- [ ] Require status checks to pass (CI/CD)
- [ ] Require branches to be up to date
- [ ] Require code review from code owners
- [ ] Dismiss stale pull request approvals
- [ ] Require conversation resolution before merging

For `develop`:
- [ ] Require pull request reviews (1 reviewer)
- [ ] Require status checks to pass
- [ ] Require branches to be up to date

---

# Files Modified/Created

- `.github/pull_request_template.md` - Pull request template
- `.git/config` - Local Git configuration (example)
- `.editorconfig` - Editor configuration for formatting

---

# What We Completed

✅ Defined branch strategy (main, develop, feature/*, release/*, hotfix/*)

✅ Established Conventional Commits format

✅ Created pull request standards and template

✅ Documented review checklist

✅ Provided workflow examples

✅ Explained tagging strategy

---

# Next Steps

1. Set up branch protection rules on GitHub
2. Enforce Conventional Commits in CI (commitlint)
3. Configure automated changelog generation from commits
4. Train team on workflow and conventions
5. Start enforcing pull requests for all changes

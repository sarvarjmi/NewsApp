# NewsApp

# 17_UI_Design_System.md

> **Phase:** Presentation Layer
>
> **Module:** Design System
>
> **Goal:** Define a complete Material 3 design system that provides a consistent visual language, reusable UI components, responsive layouts, accessibility support, and scalable styling across the entire NewsApp.

---

# Objective

A production application should not design each screen independently.

Instead, every screen should be built from a shared Design System.

The NewsApp Design System will define:

- Material 3 Theme
- Color Palette
- Typography
- Shapes
- Spacing
- Elevation
- Icons
- Motion
- Adaptive Layouts
- Accessibility
- Reusable Components

Every future Compose screen will use this design system.

---

# Why a Design System?

Without a Design System

```text
Home

↓

Random Colors

↓

Random Fonts

↓

Random Buttons
```

Problems

- Inconsistent UI
- Difficult maintenance
- Poor accessibility
- Duplicate code
- Hard theme updates

---

With a Design System

```text
Theme

↓

Tokens

↓

Reusable Components

↓

Screens
```

Benefits

- Consistency
- Scalability
- Reusability
- Easier maintenance
- Better accessibility

---

# Design Philosophy

NewsApp follows these principles:

- Clean
- Modern
- Minimal
- Content-first
- Accessible
- Fast
- Responsive

The content (news articles) should always remain the primary focus.

---

# Material 3

The application uses **Material Design 3**.

Reasons:

- Official Android recommendation
- Dynamic color support
- Better accessibility
- Modern component library
- Responsive layouts

---

# Design Tokens

Everything in the UI should be derived from design tokens.

```text
Colors

↓

Typography

↓

Shapes

↓

Spacing

↓

Elevation

↓

Components
```

Avoid hardcoded values throughout the UI.

---

# Theme Architecture

```text
Theme

│

├── Colors

├── Typography

├── Shapes

├── Dimensions

├── Elevation

└── Motion
```

Every screen consumes the same theme.

---

# Color System

The color system defines:

- Primary
- Secondary
- Tertiary
- Background
- Surface
- Error
- Outline

Additional semantic colors:

- Success
- Warning
- Info

All colors should meet Material 3 contrast recommendations.

---

# Typography

Typography hierarchy:

```text
Display

Headline

Title

Body

Label
```

Every text element uses predefined typography styles.

Avoid custom font sizes inside screens.

---

# Shape System

Reusable corner radii:

```text
Extra Small

Small

Medium

Large

Extra Large
```

Examples:

- Buttons
- Cards
- Chips
- Dialogs

All components use standardized shapes.

---

# Spacing System

Use a 4dp grid.

Examples:

```text
4dp

8dp

12dp

16dp

24dp

32dp

48dp
```

No arbitrary spacing values.

---

# Elevation

Elevation tokens:

- Level 0
- Level 1
- Level 2
- Level 3
- Level 4
- Level 5

Use elevation consistently across cards, dialogs, and app bars.

---

# Iconography

Use Material Icons where possible.

Custom icons only when necessary.

Icons should:

- Be consistent
- Match Material guidelines
- Support accessibility labels

---

# Illustrations

Illustrations are used for:

- Empty state
- Error state
- Offline state
- Search with no results

They should be simple and unobtrusive.

---

# Motion

Animations should be subtle.

Examples:

- Screen transitions
- Card appearance
- Loading shimmer
- Pull-to-refresh
- Snackbar

Motion should support, not distract from, the reading experience.

---

# Adaptive Layouts

Support:

- Phones
- Foldables
- Tablets
- Landscape mode

Future enhancement:

```text
List | Detail
```

Two-pane layout on large screens.

---

# Dark Theme

Provide full dark mode support.

Requirements:

- High contrast
- Comfortable reading
- Consistent semantic colors
- Proper image handling

The theme should switch automatically based on system settings, with optional manual override in the future.

---

# Accessibility

The design system should support:

- Dynamic font scaling
- Minimum 48dp touch targets
- Screen reader labels
- High color contrast
- Keyboard navigation (where applicable)

Accessibility is a first-class requirement, not an afterthought.

---

# Component Strategy

Reusable components include:

- NewsCard
- SearchBar
- CategoryChip
- BookmarkButton
- LoadingShimmer
- ErrorView
- EmptyState
- RetryButton
- SectionHeader

Components should be stateless whenever possible.

---

# Layout Principles

Use consistent layout rules:

- Edge padding
- Content spacing
- Maximum readable width
- Safe drawing areas
- Scaffold structure

These rules improve readability across different device sizes.

---

# Folder Structure

```text
presentation

theme

├── Theme.kt

├── Color.kt

├── Typography.kt

├── Shapes.kt

├── Dimens.kt

├── Elevation.kt

├── Motion.kt

└── PreviewTheme.kt

--------------------------------

presentation

components

├── NewsCard.kt

├── SearchBar.kt

├── LoadingShimmer.kt

├── ErrorView.kt

├── EmptyState.kt

├── BookmarkButton.kt

└── RetryButton.kt
```

---

# Design Tokens Flow

```text
Design Tokens

↓

Material Theme

↓

Reusable Components

↓

Screens
```

Every screen consumes the same design language.

---

# Design Principles Checklist

- Consistency
- Reusability
- Accessibility
- Responsiveness
- Simplicity
- Scalability
- Material 3 compliance

---

# Testing Strategy

The design system should be validated through:

- Compose previews
- Screenshot tests
- Accessibility checks
- Dark theme previews
- Large font previews
- Tablet previews

---

# Files Created

None.

This document defines the overall design system architecture.

---

# What We Completed

✅ Defined the Design System architecture

✅ Planned Material 3 integration

✅ Established design tokens

✅ Planned reusable components

✅ Defined accessibility strategy

✅ Planned adaptive layouts

✅ Established testing approach

---

# Next Documents

The design system implementation will be divided into compile-safe milestones.

## 17A_Material3_Theme.md

Create:

- `Theme.kt`
- MaterialTheme configuration
- Light & Dark themes
- Dynamic color support

---

## 17B_Color_System.md

Implement:

- `Color.kt`
- Semantic color palette
- Light/Dark color schemes
- Material 3 color mapping

---

## 17C_Typography.md

Implement:

- `Typography.kt`
- Font family
- Text styles
- Material typography hierarchy

---

## 17D_Shape_System.md

Implement:

- `Shapes.kt`
- Corner radius tokens
- Shape definitions

---

## 17E_Spacing_System.md

Implement:

- `Dimens.kt`
- Spacing tokens
- Layout constants
- Elevation tokens

---

## 17F_Component_Guidelines.md

Document and implement:

- NewsCard
- SearchBar
- BookmarkButton
- ErrorView
- EmptyState
- LoadingShimmer
- RetryButton

---

## 17G_Icons_Illustrations.md

Implement:

- Icon mapping
- Empty-state illustrations
- Placeholder image strategy

---

## 17H_Animation_System.md

Implement:

- Motion tokens
- Screen transitions
- Loading animations
- Component animations

---

## 17I_Dark_Mode.md

Implement:

- Automatic theme switching
- Manual override preparation
- Contrast validation

---

## 17J_Adaptive_Layouts.md

Implement:

- Phone layouts
- Tablet layouts
- Foldable support
- Landscape adjustments

---

## 17K_Accessibility.md

Implement:

- Semantics
- Content descriptions
- Dynamic type
- TalkBack support
- Focus order

---

## 17L_Design_System_Testing.md

Implement:

- Compose previews
- Screenshot tests
- Accessibility validation
- Theme verification

---

# Prompt for Code Generation

## Prompt: 17A_Material3_Theme.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete Material 3 theme implementation.

### Requirements

1. Create:
    - `Theme.kt`
2. Configure:
    - Light theme
    - Dark theme
    - Dynamic color support (Android 12+)
3. Use Material 3 `MaterialTheme`.
4. Expose reusable color, typography, and shape systems.
5. Add KDoc documentation.
6. Explain every design decision.
7. Provide complete runnable Kotlin code.

---

## Prompt: 17B_Color_System.md

Generate the complete `Color.kt`.

Requirements:

- Define semantic color tokens.
- Create Light and Dark color schemes.
- Follow Material 3 naming conventions.
- Ensure accessibility contrast.
- Explain every color role.
- Provide complete runnable Kotlin code.

---

## Prompt: 17F_Component_Guidelines.md

Generate the reusable UI component library.

Requirements:

Create:

- `NewsCard.kt`
- `SearchBar.kt`
- `BookmarkButton.kt`
- `LoadingShimmer.kt`
- `ErrorView.kt`
- `EmptyState.kt`
- `RetryButton.kt`

Requirements:

- Stateless composables
- Material 3 components
- Accessibility support
- Responsive layouts
- Preview functions
- KDoc documentation
- Complete runnable Compose code with explanations.

---

# UI Design Prompt

## Prompt: NewsApp Visual Design System

Create the complete visual design specification for **NewsApp**.

### Brand Personality

- Modern
- Minimal
- Professional
- Trustworthy
- Content-first

### Deliverables

1. Mood board
2. Color palette (Light & Dark)
3. Typography scale
4. Shape system
5. Spacing system (4dp grid)
6. Elevation system
7. Iconography guidelines
8. Component library (cards, buttons, search bar, chips, app bars, bottom navigation)
9. Empty, loading, offline, and error state designs
10. Tablet and phone responsive layouts
11. Accessibility recommendations
12. Motion and animation guidelines
13. Design token specification ready for Jetpack Compose implementation

Do **not** generate implementation code. Produce a comprehensive design specification that will serve as the foundation for all upcoming UI screens.
# NewsApp

# 26_DeepLinks.md

> **Phase:** Navigation & Platform Integration
>
> **Module:** Deep Links & App Links
>
> **Goal:** Design and implement a secure, scalable Deep Link architecture that allows users to open NewsApp content directly from browsers, notifications, emails, QR codes, and external applications using Jetpack Compose Navigation.

---

# Objective

Modern Android applications should allow users to open specific content directly.

Instead of always opening the Home Screen, Deep Links enable navigation directly to:

- News Article
- Search Results
- Bookmark Screen
- Category
- Settings (Future)
- Notification Target

The implementation should support:

- Deep Links
- Android App Links
- Notification Links
- Browser Links
- QR Code Links
- Share Links
- Secure URL validation

---

# Why Deep Links?

Without Deep Links

```text
User Clicks Link

↓

App Opens

↓

Home

↓

Search

↓

Find Article
```

Poor user experience.

---

With Deep Links

```text
User Clicks Link

↓

App Opens

↓

Article Detail
```

Immediate access to content.

---

# Navigation Architecture

```text
Browser

↓

Intent

↓

Deep Link

↓

NavHost

↓

Destination

↓

Compose Screen
```

---

# Supported Deep Links

Current implementation

```text
Article Detail

Search

Bookmarks
```

Future implementation

```text
Categories

Sources

Settings

Profile

Notifications
```

---

# Supported Entry Points

Users may open NewsApp from:

- Browser
- Google Search
- Chrome Custom Tabs
- Push Notification
- Email
- QR Code
- Another App
- Share Intent

---

# Deep Link Flow

```text
User

↓

https://newsapp.com/article/123

↓

Android Intent

↓

NavController

↓

Detail Screen
```

---

# URL Strategy

Examples

Article

```text
https://newsapp.com/article/{id}
```

Search

```text
https://newsapp.com/search?q=android
```

Bookmarks

```text
https://newsapp.com/bookmarks
```

Future

```text
/category/technology

/source/bbc

/settings
```

---

# Navigation Graph

```text
Root

│

├── Home

├── Search

├── Bookmarks

└── Detail
        ↑

    Deep Link
```

---

# App Links

Preferred implementation

Android App Links

Benefits

- Verified domain
- Opens directly
- No chooser dialog
- Better security

Requirements

- HTTPS
- Digital Asset Links
- Domain verification

---

# Deep Links vs App Links

| Deep Link | App Link |
|------------|----------|
| Any URI | Verified HTTPS |
| May show chooser | Opens directly |
| No verification | Domain verification |
| Simpler | More secure |

---

# Intent Flow

```text
Browser

↓

Intent

↓

MainActivity

↓

NavHost

↓

Destination
```

---

# Manifest Configuration

Intent Filters

Support

- HTTPS
- Custom Scheme (optional)
- Auto Verify

Future

- Multiple domains

---

# Safe URL Validation

Before navigation

Validate

- HTTPS only
- Trusted domain
- Valid article ID
- Non-empty parameters

Reject

- Invalid scheme
- Malformed URL
- Unknown host

---

# Detail Screen Flow

```text
Deep Link

↓

Article ID

↓

DetailViewModel

↓

Repository

↓

API

↓

UI
```

---

# Search Flow

```text
https://newsapp.com/search?q=android

↓

SearchViewModel

↓

Search Query

↓

Paging

↓

Results
```

---

# Bookmark Flow

```text
/bookmarks

↓

Bookmark Screen
```

---

# Notification Integration

Future

```text
Push Notification

↓

Deep Link

↓

Detail Screen
```

---

# Share Integration

Flow

```text
Share

↓

Article URL

↓

Receiver Opens

↓

Detail Screen
```

---

# QR Code Support

Example

```text
QR Code

↓

https://newsapp.com/article/54321

↓

App Opens

↓

Detail
```

---

# Browser Integration

If app unavailable

```text
Browser

↓

Website
```

If app installed

```text
Browser

↓

NewsApp
```

---

# Back Stack

Deep Link

↓

Detail

↓

Back

↓

Home

Expected navigation should feel natural.

---

# Error Handling

Invalid Link

↓

Error Screen

↓

Return Home

Examples

- Missing ID
- Invalid URL
- Unsupported path

---

# Offline Behavior

Deep Link

↓

Offline

↓

Bookmark Exists?

↓

Open Offline

↓

Else

Offline Screen

---

# Security

Always

- HTTPS
- Verify domain
- Validate parameters
- Sanitize query strings
- Reject unknown schemes

Never trust external input.

---

# Analytics

Track

- Deep Link source
- Open success
- Open failure
- Search links
- Notification opens

Future

Firebase Analytics.

---

# Accessibility

Requirements

- Proper navigation announcements
- Restored focus
- Screen reader support
- Predictable navigation

---

# Folder Structure

```text
presentation

navigation

├── DeepLinks.kt

├── AppLinks.kt

├── RouteParser.kt

├── DeepLinkHandler.kt

└── NavigationExtensions.kt

--------------------------------

core

deeplink

├── DeepLinkValidator.kt

├── UriParser.kt

└── LinkConstants.kt
```

---

# Architecture Diagram

```text
External URI

↓

Intent

↓

DeepLinkHandler

↓

RouteParser

↓

NavController

↓

Destination
```

---

# Best Practices

- Use HTTPS App Links
- Validate every URI
- Keep routes centralized
- Never hardcode paths
- Support graceful fallback
- Keep ViewModels navigation-agnostic
- Use typed route arguments

---

# Testing Strategy

Verify

- Browser links
- Notification links
- Search links
- Invalid links
- Unknown paths
- App Links verification
- Back stack behavior
- Configuration changes

Use

- Compose Navigation Tests
- Espresso Intents
- Deep Link instrumentation tests

---

# Build Verification

After implementation

- Browser opens Detail screen
- Search links work
- Bookmarks link works
- Invalid links handled gracefully
- Verified App Links open directly
- Back navigation behaves correctly
- Accessibility passes

---

# Files Created

None.

This document defines the Deep Link architecture and implementation strategy.

---

# What We Completed

✅ Designed Deep Link architecture

✅ Planned App Links

✅ Planned browser integration

✅ Planned notification integration

✅ Planned security validation

✅ Planned analytics

---

# Next Documents

The Deep Link implementation will be divided into compile-safe milestones.

---

## 26A_Manifest_DeepLinks.md

Create

- Intent Filters
- App Links
- Auto Verify
- Domain configuration

---

## 26B_Route_Parser.md

Implement

- `RouteParser.kt`
- URI parsing
- Route mapping
- Parameter extraction

---

## 26C_DeepLink_Handler.md

Implement

- `DeepLinkHandler.kt`
- Navigation routing
- Error handling
- Unknown URI fallback

---

## 26D_AppLinks.md

Implement

- HTTPS App Links
- Digital Asset Links configuration
- Domain verification
- Testing guide

---

## 26E_Notification_DeepLinks.md

Implement

- Notification PendingIntent
- Navigation integration
- Back stack handling

---

## 26F_Share_DeepLinks.md

Implement

- Share article URLs
- Open shared links
- Browser fallback

---

## 26G_DeepLink_Testing.md

Implement

- Navigation tests
- Browser tests
- App Links verification
- Invalid URI tests
- Instrumentation tests

---

# Prompt for Code Generation

## Prompt: 26A_Manifest_DeepLinks.md

You are a Senior Android Engineer building the **NewsApp** project.

Generate the complete AndroidManifest configuration for Deep Links and Android App Links.

### Requirements

1. Configure Intent Filters.
2. Support:
    - HTTPS App Links
    - Auto Verify
    - Article routes
    - Search routes
    - Bookmark routes
3. Explain every XML element.
4. Provide complete runnable AndroidManifest code.

---

## Prompt: 26B_Route_Parser.md

Generate the complete `RouteParser.kt`.

Requirements:

- Parse incoming URIs.
- Validate parameters.
- Return typed navigation destinations.
- Reject malformed URLs.
- Explain every function.
- Provide complete runnable Kotlin code.

---

## Prompt: 26C_DeepLink_Handler.md

Generate the complete `DeepLinkHandler.kt`.

Requirements:

- Receive Intent data.
- Parse URI.
- Navigate to destination.
- Handle invalid links gracefully.
- Integrate with Compose Navigation.
- Explain every implementation.
- Provide complete runnable Kotlin code.

---

## Prompt: 26D_AppLinks.md

Generate the complete Android App Links implementation.

Requirements:

- Configure verified HTTPS links.
- Explain Digital Asset Links.
- Show `assetlinks.json` structure.
- Explain domain verification.
- Provide complete production-ready configuration.

---

## Prompt: 26E_Notification_DeepLinks.md

Generate the notification deep-link implementation.

Requirements:

- PendingIntent
- Deep Link navigation
- Proper back stack
- Notification tap handling
- Explain every implementation.
- Provide complete runnable Kotlin code.

---

# UI Design Prompt

## Prompt: Deep Link & Navigation UX Specification

Design the complete **Deep Link** user experience for **NewsApp**.

### Include

1. Browser → App transition
2. Notification → Detail flow
3. Search link flow
4. Bookmark link flow
5. Invalid link screen
6. Offline deep-link behavior
7. Loading state while resolving links
8. Back navigation behavior
9. Tablet and phone navigation differences
10. Accessibility requirements
11. Material 3 transition and motion guidelines
12. Error recovery UX for broken or expired links

Do **not** generate Jetpack Compose code. Produce a complete navigation and UX specification that ensures deep links feel seamless, secure, and consistent with the rest of the application.

---

# Production Notes

## Recommended URI Structure

```text
https://newsapp.com/

├── article/{articleId}

├── search?q={query}

├── bookmarks

├── category/{category}

├── source/{sourceId}

└── settings
```

---

## Future Enhancements

The Deep Link infrastructure can later support:

- Firebase Dynamic Links replacement strategies
- QR-code campaign links
- Marketing attribution parameters (UTM)
- Instant App compatibility
- Wear OS and Android Auto entry points
- Multi-module feature navigation
- AI-generated share links
- Universal links across Android and web

These enhancements build on the same navigation architecture without requiring changes to the existing Presentation or Domain layers.
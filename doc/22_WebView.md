# NewsApp

> **Phase:** Presentation Layer
>
> **Module:** Article Reader (WebView & Chrome Custom Tabs)
>
> **Goal:** Design and implement a secure, production-ready article reading experience using Android WebView and Chrome Custom Tabs with proper lifecycle management, security, offline handling, and modern Material 3 UI.

---

# Objective

The Detail Screen allows users to preview an article.

However, many news APIs return truncated content.

The **WebView Screen** enables users to read the complete article directly inside the application.

The implementation should support:

- Internal article reading
- Chrome Custom Tabs
- External Browser fallback
- Progress indicator
- Error handling
- Offline support
- Secure WebView configuration
- Dark Mode
- State restoration
- Deep Links

---

# Why WebView?

Many news providers restrict API content.

Example

```text
API

↓

Title

↓

Description

↓

Short Content

↓

Read Full Article

↓

Original Website
```

Instead of opening every article in Chrome, we provide an integrated reading experience.

---

# Reading Strategy

Preferred order

```text
Chrome Custom Tabs

↓

WebView

↓

External Browser
```

Chrome Custom Tabs are recommended because they:

- Share cookies
- Use Chrome security
- Load faster
- Provide browser features

WebView is used when an embedded reader is required.

---

# Responsibilities

The WebView module is responsible for:

- Load article URL
- Display loading progress
- Handle redirects
- Display errors
- Support back navigation
- Restore state
- Handle SSL errors safely
- Support downloads
- Handle external intents

The module should NOT:

- Fetch API data
- Manage bookmarks
- Perform business logic

---

# Architecture

```text
Detail Screen

↓

Open Article

↓

Navigation Event

↓

WebView Screen

↓

Chrome Custom Tabs

or

WebView

↓

News Website
```

---

# User Journey

```text
Home

↓

Detail

↓

Read Full Article

↓

WebView

↓

Back

↓

Detail

↓

Back

↓

Home
```

---

# Screen Layout

```text
Scaffold

│

├── Top App Bar

├── Linear Progress Indicator

└── AndroidView(WebView)
```

---

# Layout Wireframe

```text
------------------------------------------------

← Back      Article      Share

------------------------------------------------

Progress Indicator

------------------------------------------------

WebView

------------------------------------------------

```

---

# Components Used

```text
AppTopBar

ProgressIndicator

ErrorView

RetryButton

OfflineBanner

LoadingOverlay
```

---

# Top App Bar

Contains

- Back
- Page Title
- Share
- Open in Browser
- More Menu (future)

---

# WebView Configuration

Enable

- JavaScript (only if required)
- DOM Storage
- Zoom Controls
- Wide Viewport
- Safe Browsing
- Mixed Content disabled

Disable

- File Access
- Universal Access
- Unused plugins

---

# Security Best Practices

Always

- Use HTTPS
- Validate URLs
- Disable insecure file access
- Enable Safe Browsing
- Prevent arbitrary JavaScript interfaces
- Restrict navigation to trusted domains where appropriate

Never enable unnecessary permissions.

---

# Chrome Custom Tabs

Preferred implementation

```text
Detail

↓

Chrome Custom Tabs

↓

Browser Rendering
```

Benefits

- Better security
- Better performance
- Shared cookies
- Material animations

Fallback

```text
WebView

↓

Browser
```

---

# Loading Flow

```text
Open URL

↓

Loading

↓

Progress Bar

↓

Page Loaded

↓

Hide Progress
```

---

# Progress Indicator

Display

```text
0%

↓

50%

↓

100%
```

A linear indicator appears at the top while the page loads.

---

# Error Handling

Possible errors

- No internet
- SSL error
- Invalid URL
- Page not found
- Timeout

Display

- Illustration
- Friendly message
- Retry
- Open in Browser

---

# Offline Support

When offline

Display

```text
Offline Illustration

↓

Retry

↓

Open Later
```

Cached WebView pages are optional.

---

# Redirect Handling

Handle

- HTTP redirects
- Deep links
- External apps
- Mail links
- Telephone links

External schemes should launch the appropriate Android Intent.

---

# Download Handling

Future support

- PDF download
- Image download
- File download

Using Android DownloadManager.

---

# Back Navigation

Flow

```text
WebView History

↓

Previous Page

↓

Exit Screen
```

If WebView can go back

```text
webView.goBack()
```

Otherwise

```text
Navigate Up
```

---

# Share

Flow

```text
Share Button

↓

Android Share Sheet

↓

Article URL
```

---

# Open in Browser

Flow

```text
Toolbar Button

↓

External Browser
```

Useful when WebView rendering is incompatible.

---

# State Restoration

Preserve

- Scroll position
- Page history
- Current URL
- Loading state

Across configuration changes.

---

# Dark Mode

Requirements

- Force dark where supported
- Respect website styles
- Material top app bar

---

# Accessibility

Requirements

- TalkBack labels
- Accessible toolbar
- Loading announcements
- Focus management
- Large text compatibility

---

# Performance

- Reuse WebView carefully
- Clear resources on dispose
- Avoid memory leaks
- Enable hardware acceleration
- Use lifecycle-aware cleanup

---

# Folder Structure

```text
presentation

webview

├── WebViewScreen.kt

├── WebViewContent.kt

├── WebViewTopBar.kt

├── WebViewViewModel.kt

├── WebViewUiState.kt

├── WebViewEvent.kt

├── WebViewSideEffect.kt

└── WebViewClientFactory.kt

--------------------------------

core

web

ChromeCustomTabsManager.kt

BrowserLauncher.kt

UrlValidator.kt
```

---

# UI State

```text
Loading

↓

Loaded

↓

Error

↓

Offline
```

---

# Interaction Flow

```text
User

↓

Read Full Article

↓

Navigation

↓

WebView

↓

Back

↓

Detail
```

---

# Deep Link Support

Supported

```text
https://example.com/article/123
```

Future

- App Links
- Notification links
- Share targets

---

# Testing Strategy

Verify

- URL loading
- Progress indicator
- Back navigation
- External browser
- Chrome Custom Tabs
- Error handling
- Offline mode
- State restoration
- Accessibility

Use

- Espresso Web
- Compose UI Tests
- Fake URL handlers
- Screenshot Tests

---

# Build Verification

After implementation

- WebView loads article
- Chrome Custom Tabs open correctly
- Progress indicator updates
- Errors display properly
- External browser fallback works
- Back navigation behaves correctly
- Dark mode renders correctly
- Accessibility passes

---

# Files Created

None.

This document defines the WebView architecture and implementation strategy.

---

# What We Completed

✅ Designed the WebView architecture

✅ Planned Chrome Custom Tabs integration

✅ Planned secure WebView configuration

✅ Planned loading and error handling

✅ Planned state restoration

✅ Planned accessibility

---

# Next Documents

The WebView implementation will be divided into compile-safe milestones.

## 22A_WebView_UI_State.md

Create

- `WebViewUiState.kt`
- Loading
- Loaded
- Error
- Offline states

---

## 22B_WebView_Events.md

Create

- `WebViewEvent.kt`
- Retry
- Share
- OpenBrowser
- BackPressed

---

## 22C_WebView_Screen.md

Implement

- `WebViewScreen.kt`
- Scaffold
- AndroidView(WebView)
- Progress indicator
-
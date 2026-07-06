# Android App Links Configuration

This document describes how to configure verified HTTPS App Links for NewsApp.

---

## 1. assetlinks.json

To enable App Links, you must host a file named `assetlinks.json` in the `.well-known` directory of your web domain.

**URL**: `https://newsapp.com/.well-known/assetlinks.json`

### Content Structure

```json
[
  {
    "relation": ["delegate_permission/common.handle_all_urls"],
    "target": {
      "namespace": "android_app",
      "package_name": "com.newsapp",
      "sha256_cert_fingerprints": [
        "FA:C6:17:45:DC:09:03:78:6C:B9:ED:62:08:30:92:CB:9A:2F:8B:20:94:F5:7E:03:7A:B4:73:97:32:00:1E:C5"
      ]
    }
  }
]
```

---

## 2. Manifest Configuration

The `AndroidManifest.xml` must contain `android:autoVerify="true"` in the intent filter.

```xml
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="https" />
    <data android:host="newsapp.com" />

    <!-- Home Route (Root) -->
    <data android:path="/" />
    <data android:path="/home" />
    <!-- Article Route -->
    <data android:pathPrefix="/article/" />
    <!-- Search Route -->
    <data android:pathPrefix="/search" />
    <!-- Bookmarks Route -->
    <data android:pathPrefix="/bookmarks" />
</intent-filter>
```

---

## 3. Verification Process

1. **Install APK**: When the app is installed, Android attempts to fetch the `assetlinks.json` file.
2. **System Check**: If the file exists and the package name/fingerprint match, the domain is verified.
3. **Automatic Open**: Verified links will open directly in the app without showing a disambiguation dialog.

---

## 4. Sample Deep Link URLs

Below is a comprehensive list of supported URLs for testing and verification.

### Home / Headlines
- **HTTPS (Root)**: `https://newsapp.com/`
- **HTTPS (Path)**: `https://newsapp.com/home`
- **Custom Scheme**: `newsapp://home`

### Article Detail
The app supports navigating to a native detail view for cached articles. If the article is not in the cache, it falls back to an internal WebView.

- **HTTPS (Standard Encoded)**: `https://newsapp.com/article/https%3A%2F%2Fapnews.com%2Farticle%2Fpope-july-4-immigration-migrants-lampedusa-1dd8b2964586f719e99faf4aaeaaf205`
- **HTTPS (Raw URL)**: `https://newsapp.com/article/https://www.bbc.com/news/world-middle-east-68490918`
- **HTTPS (Path with query)**: `https://newsapp.com/article/https://cnn.com/2024/03/05/politics/super-tuesday-takeaways/index.html`
- **Custom Scheme**: `newsapp://article/https%3A%2F%2Ftechcrunch.com%2F2024%2F03%2F05%2Fapple-macbook-air-m3%2F`

### Search
- **HTTPS (Single Keyword)**: `https://newsapp.com/search?q=android`
- **HTTPS (Multi-word with Plus)**: `https://newsapp.com/search?q=jetpack+compose+tutorial`
- **HTTPS (Multi-word with Encoding)**: `https://newsapp.com/search?q=kotlin%20multiplatform`
- **HTTPS (Empty Search)**: `https://newsapp.com/search`
- **Custom Scheme**: `newsapp://search?q=pixel+9+pro`

### Bookmarks
- **HTTPS**: `https://newsapp.com/bookmarks`
- **Custom Scheme**: `newsapp://bookmarks`

---

## 5. Testing App Links

### Using ADB
Use ADB to simulate opening verified links and test back-stack behavior:

#### Test Article Detail
```bash
adb shell am start -a android.intent.action.VIEW \
    -c android.intent.category.BROWSABLE \
    -d "https://newsapp.com/article/https%3A%2F%2Fapnews.com%2Farticle%2F123"
```

#### Test Search
```bash
adb shell am start -a android.intent.action.VIEW \
    -c android.intent.category.BROWSABLE \
    -d "https://newsapp.com/search?q=jetpack+compose"
```

#### Test Bookmarks
```bash
adb shell am start -a android.intent.action.VIEW \
    -c android.intent.category.BROWSABLE \
    -d "https://newsapp.com/bookmarks"
```

### Verification Status
To check if the system has verified your domain:
```bash
adb shell pm get-app-links com.newsapp
```
Look for `domain: newsapp.com` and `status: verified`.

# MandiriNews

Aplikasi berita Android untuk tugas akhir Virtual Internship Experience (VIE) Bank Mandiri x Rakamin Academy.

## Screenshots

| Home Light | Home Dark | Detail |
|------------|-----------|--------|
| ![Home](screenshots/home_light.png) | ![Dark](screenshots/home_dark.png) | ![Detail](screenshots/detail.png) |

## Fitur

- **Carousel Berita Terkini** - ViewPager2 dengan auto-slide dan navigasi
- **List Berita** - Infinite scroll dengan pagination
- **Detail Berita** - Halaman detail dengan share functionality
- **Pencarian** - Cari berita berdasarkan keyword
- **Filter Kategori** - Bisnis, Teknologi, Olahraga, Hiburan, Politik
- **Bookmark** - Simpan artikel favorit ke local database
- **Dark/Light Mode** - Toggle tema gelap dan terang
- **Multi Bahasa** - Dukungan Indonesia dan English

## Teknologi

| Komponen | Teknologi |
|----------|-----------|
| Bahasa | Kotlin |
| Arsitektur | MVVM (Model-View-ViewModel) |
| Networking | Retrofit + OkHttp |
| Async | Kotlin Coroutines |
| UI State | LiveData |
| Local DB | Room Database |
| Image Loading | Glide |
| UI Components | Material Design 3 |
| View Binding | Android ViewBinding |

## Arsitektur MVVM

```
app/
├── data/
│   ├── local/          # Room Database (Bookmark)
│   ├── model/          # Data classes
│   ├── network/        # Retrofit API
│   └── repository/     # Repository pattern
├── ui/
│   ├── allnews/        # All news list
│   ├── bookmark/       # Bookmark ViewModel
│   ├── headline/       # Carousel headline
│   ├── saved/          # Saved articles
│   └── search/         # Search feature
└── utils/              # Helper classes
```

## API

Aplikasi menggunakan [NewsAPI.org](https://newsapi.org/) untuk mengambil data berita.

**Endpoints:**
- `GET /v2/everything` - Mengambil semua berita
- `GET /v2/everything?q={query}` - Pencarian berita

## Instalasi

1. Clone repository
```bash
git clone https://github.com/rohidrivaldi/rakamin_mandiri.git
```

2. Buka project di Android Studio

3. Sync Gradle

4. Jalankan aplikasi

## Konfigurasi API Key

API Key sudah dikonfigurasi di `app/src/main/java/dev/rakamin/newsapp/utils/Constants.kt`

```kotlin
object Constants {
    const val BASE_URL = "https://newsapi.org/"
    const val API_KEY = "YOUR_API_KEY"
    // ...
}
```

## Requirements

- Android Studio Hedgehog atau lebih baru
- JDK 17
- Min SDK 24 (Android 7.0)
- Target SDK 35

## Dependencies

```kotlin
// Retrofit + OkHttp
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Glide
implementation("com.github.bumptech.glide:glide:4.16.0")

// Material Design
implementation("com.google.android.material:material:1.12.0")
```

## Penulis

**Rohid Rivaldi**  
Virtual Internship Experience - Bank Mandiri x Rakamin Academy

## License

```
Copyright 2025 Rohid Rivaldi

Licensed under the Apache License, Version 2.0
```

# DOKUMENTASI FINAL TASK - MOBILE APPS DEVELOPER BANK MANDIRI
## Aplikasi MandiriNews - Virtual Internship Rakamin x Bank Mandiri

---

## 1. INFORMASI PROJECT

| Item | Detail |
|------|--------|
| **Nama Aplikasi** | MandiriNews |
| **Package Name** | dev.rakamin.newsapp |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 35 (Android 15) |
| **Arsitektur** | MVVM + Repository Pattern |
| **API** | NewsAPI (newsapi.org) |
| **APK Location** | `app/build/outputs/apk/debug/app-debug.apk` |

---

## 2. STRUKTUR ARSITEKTUR MVVM

```
dev.rakamin.newsapp/
├── data/
│   ├── model/
│   │   ├── Article.kt         # Data class untuk artikel berita
│   │   ├── Source.kt          # Data class untuk sumber berita
│   │   └── ArticleResponse.kt # Response wrapper dari API
│   ├── network/
│   │   ├── NewsApiClient.kt   # Retrofit singleton instance
│   │   └── NewsInterface.kt   # Endpoint API definitions
│   └── repository/
│       └── NewsRepository.kt  # Repository pattern untuk data access
├── ui/
│   ├── MainActivity.kt        # Host Activity
│   ├── HomeFragment.kt        # Fragment utama dengan UI logic
│   ├── headline/
│   │   ├── HeadlineAdapter.kt    # ViewPager2 adapter untuk carousel
│   │   └── HeadlineViewModel.kt  # ViewModel untuk headline
│   └── allnews/
│       ├── NewsAdapter.kt        # RecyclerView adapter untuk list berita
│       └── AllNewsViewModel.kt   # ViewModel dengan pagination
└── utils/
    ├── Constants.kt           # API_KEY, BASE_URL
    ├── DateFormatter.kt       # Utility format tanggal
    └── Resource.kt            # Sealed class untuk state management
```

---

## 3. FITUR APLIKASI

### 3.1 Headline News (Carousel)
- Menampilkan 5 berita utama dalam ViewPager2
- Gambar full-width dengan gradient overlay
- Indicator dots untuk navigasi
- Swipe horizontal untuk berganti berita

### 3.2 Semua Berita (Infinite Scroll)
- RecyclerView dengan card layout
- Pagination otomatis saat scroll ke bawah
- Loading indicator saat memuat halaman baru
- Pull-to-refresh untuk memperbarui data

### 3.3 Error Handling
- Loading state dengan ProgressBar
- Error state dengan tombol retry
- Empty state jika tidak ada data

### 3.4 UI/UX
- Warna tema Bank Mandiri (Biru #003D79 & Gold #FFB81C)
- Material Design 3 components
- Smooth scrolling experience
- Dark mode support

---

## 4. DAFTAR SCREENSHOT UNTUK PPT

Ambil screenshot berikut untuk slide presentasi:

### Screenshot Wajib:
1. **Splash/Launcher** - Icon aplikasi di home screen
2. **Tampilan Utama** - HomeFragment dengan headline carousel dan list berita
3. **Detail Headline** - Carousel headline dalam posisi berbeda
4. **Scroll State** - Aplikasi saat menampilkan berita di bagian bawah
5. **Loading State** - Saat aplikasi memuat data (bisa dengan disable wifi sesaat)
6. **Error State** - Tampilan error dengan tombol retry (matikan internet)

### Screenshot Tambahan (Opsional):
7. **Pull to Refresh** - Saat menarik layar ke bawah
8. **Dark Mode** - Jika device dalam dark mode
9. **Landscape Mode** - Orientasi horizontal (jika didukung)

---

## 5. PANDUAN POIN-POIN SLIDE PPT

### Slide 1: Cover
- Judul: "Final Task Mobile Apps Developer"
- Subtitle: "Virtual Internship Experience - Bank Mandiri x Rakamin"
- Nama peserta & periode

### Slide 2: Latar Belakang
- Penjelasan challenge dari Bank Mandiri
- Tujuan pembuatan aplikasi news
- Relevansi dengan pengembangan Livin' Mandiri

### Slide 3: Tech Stack
- Kotlin + Android SDK
- MVVM Architecture
- Retrofit + OkHttp (Networking)
- Glide (Image Loading)
- Coroutines (Async)
- Material Design 3

### Slide 4: Arsitektur Aplikasi
- Diagram MVVM
- Penjelasan flow data: UI → ViewModel → Repository → API
- Manfaat separation of concerns

### Slide 5: Fitur Utama
- Headline News Carousel
- Infinite Scroll List
- Pull-to-Refresh
- Error Handling

### Slide 6: Screenshot Aplikasi
- Tampilkan 4-6 screenshot utama
- Beri keterangan pada setiap screenshot

### Slide 7: API Integration
- NewsAPI endpoints yang digunakan
- Contoh response JSON
- Penjelasan pagination

### Slide 8: Tantangan & Solusi
- Challenge yang dihadapi selama pengembangan
- Bagaimana mengatasinya
- Lessons learned

### Slide 9: Demo Video
- Screenshot atau thumbnail video
- Link video (jika diperlukan)

### Slide 10: Kesimpulan
- Ringkasan pembelajaran
- Keterampilan yang didapat
- Rencana pengembangan selanjutnya

### Slide 11: Terima Kasih
- Contact information
- Link GitHub repository (jika ada)

---

## 6. CHECKLIST VIDEO DEMO

Durasi rekomendasi: 2-3 menit

### Scene 1: Pembukaan (15 detik)
- [ ] Perkenalkan diri
- [ ] Sebutkan nama aplikasi: "MandiriNews"
- [ ] Jelaskan ini adalah Final Task Bank Mandiri

### Scene 2: Icon & Launch (15 detik)
- [ ] Tunjukkan icon aplikasi di home screen
- [ ] Buka aplikasi
- [ ] Tunggu loading selesai

### Scene 3: Headline Carousel (30 detik)
- [ ] Jelaskan section "Berita Terkini"
- [ ] Swipe carousel ke kanan/kiri
- [ ] Tunjukkan indicator dots bergerak
- [ ] Tap salah satu headline (buka di browser)

### Scene 4: Semua Berita List (30 detik)
- [ ] Jelaskan section "Semua Berita"
- [ ] Scroll ke bawah dengan smooth
- [ ] Tunjukkan pagination (loading more)
- [ ] Tap salah satu berita (buka di browser)

### Scene 5: Pull to Refresh (15 detik)
- [ ] Scroll ke atas
- [ ] Tarik layar ke bawah untuk refresh
- [ ] Tunjukkan indicator refresh

### Scene 6: Error Handling (30 detik)
- [ ] Matikan internet/WiFi
- [ ] Coba refresh data
- [ ] Tunjukkan error state
- [ ] Tap tombol "Coba Lagi"
- [ ] Nyalakan internet kembali
- [ ] Data berhasil dimuat

### Scene 7: Arsitektur (opsional, 30 detik)
- [ ] Tunjukkan struktur folder di Android Studio
- [ ] Jelaskan MVVM pattern
- [ ] Highlight key files

### Scene 8: Penutup (15 detik)
- [ ] Ringkas fitur utama
- [ ] Ucapkan terima kasih

---

## 7. DEPENDENCIES YANG DIGUNAKAN

```kotlin
// Core Android
androidx.core:core-ktx:1.16.0
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.12.0
androidx.constraintlayout:constraintlayout:2.1.4

// Networking
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0
com.squareup.okhttp3:logging-interceptor:4.12.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// Lifecycle
androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
androidx.lifecycle:lifecycle-livedata-ktx:2.7.0
androidx.lifecycle:lifecycle-runtime-ktx:2.7.0

// UI Components
androidx.viewpager2:viewpager2:1.0.0
androidx.recyclerview:recyclerview:1.3.2
androidx.swiperefreshlayout:swiperefreshlayout:1.1.0
androidx.fragment:fragment-ktx:1.6.2

// Image Loading
com.github.bumptech.glide:glide:4.16.0
```

---

## 8. CARA MENJALANKAN APLIKASI

### Via Android Studio:
1. Buka project di Android Studio
2. Sync Gradle (File > Sync Project with Gradle Files)
3. Pilih device/emulator
4. Klik Run (Shift + F10)

### Via APK:
1. Salin file `app-debug.apk` ke device
2. Enable "Install from Unknown Sources" jika diminta
3. Install APK
4. Buka aplikasi MandiriNews

---

## 9. API ENDPOINTS

```
Base URL: https://newsapi.org/

1. Headlines (Berita Utama Indonesia):
   GET /v2/top-headlines?country=id&apiKey=YOUR_KEY

2. Everything (Semua Berita dengan Pagination):
   GET /v2/everything?q=indonesia&page=1&pageSize=20&apiKey=YOUR_KEY
```

---

## 10. CATATAN PENTING

1. **API Rate Limit**: NewsAPI free tier memiliki limit 100 requests/hari
2. **Internet Required**: Aplikasi memerlukan koneksi internet untuk fetch data
3. **Minimum Android**: Android 7.0 (API 24) ke atas
4. **Best Experience**: Gunakan di device dengan layar 5.5" atau lebih

---

*Dokumentasi ini dibuat untuk Final Task Mobile Apps Developer - Virtual Internship Bank Mandiri x Rakamin*

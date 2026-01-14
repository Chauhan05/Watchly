# Watchly - Movie & TV Show Discovery App

A modern Android application built with Jetpack Compose that allows users to discover movies and TV shows using the Watchmode API.

## 📱 Screenshots
<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/3daf3587-a576-4a24-a8ae-3facdcefd60b" 
           alt="Reports" width="250" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/aa9d7a5e-5db9-4517-98b2-b80472ef4b55" 
           alt="Transaction History" width="250" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/e7c0e2d3-2ef1-452a-9fd4-aae9241c1247" 
           alt="Home Screen" width="250" />
  </tr>
  <tr>
    <td align="center"><b>Home Screen</b></td>
    <td align="center"><b>Add Transaction</b></td>
    <td align="center"><b>History with filters</b></td>
  </tr>
</table>

## ✨ Features

- **Home Screen**: Browse movies and TV shows with tab navigation
- **Detail Screen**: View detailed information including title, description, release date, rating, genres, and poster
- **Shimmer Loading**: Smooth loading animations while fetching data
- **Error Handling**: User-friendly error messages with retry functionality
- **Simultaneous API Calls**: Fetches movies and TV shows data in parallel using RxKotlin

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Kotlin** | Programming language |
| **Jetpack Compose** | Modern UI toolkit |
| **MVVM** | Architecture pattern |
| **Hilt** | Dependency Injection |
| **Retrofit** | Network calls |
| **RxKotlin (Single.zip)** | Simultaneous API calls |
| **Coil** | Image loading |
| **Material 3** | UI components |

## 📁 Project Structure
```
com.example.watchly/
├── data/
│   ├── remote/
│   │   ├── ApiService.kt
│   │   └── response/
│   │       ├── ItemResponse.kt
│   │       └── ItemDetailResponse.kt
│   └── repository/
│       └── WatchlyRepository.kt
├── di/
│   └── NetworkModule.kt
├── ui/
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── HomeScreenUiState.kt
│   ├── detail/
│   │   ├── DetailScreen.kt
│   │   ├── DetailScreenViewModel.kt
│   │   └── DetailScreenUiState.kt
│   └── theme/
└── WatchlyApp.kt
```

## 🚀 Setup Instructions

1. **Clone the repository**
```bash
   git clone https://github.com/yourusername/watchly.git
```

2. **Add API Key**
   
   Open `local.properties` and add your Watchmode API key:
```properties
   WATCHMODE_API_KEY=your_api_key_here
```

3. **Build and Run**
   
   Open the project in Android Studio and run on an emulator or device.

## 📋 Requirements Implemented

-  Home screen with Movies & TV Shows tabs
-  Shimmer effect during loading
-  Detail screen with title, description, release date, and poster
-  Simultaneous API calls using `Single.zip` (RxKotlin)
-  Retrofit for networking
-  Error handling with Snackbar
-  MVVM architecture
-  Dependency Injection (Hilt)

## 🔧 API Reference

This app uses the [Watchmode API](https://api.watchmode.com/) to fetch movie and TV show data.

## 📝 Assumptions

- API key is stored securely in `local.properties` (not committed to version control)
- Minimum SDK version is 24 (Android 7.0)
- Internet connection is required for fetching data

## 🎥 Demo Video

[Link to demo video](your_video_link_here)

## 📦 APK Download

[Download APK](your_apk_link_here)

---

Built with ❤️ using Jetpack Compose

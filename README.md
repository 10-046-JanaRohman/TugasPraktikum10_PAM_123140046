# NotesApp - Praktikum 5 + Praktikum 7 + Praktikum 8

Project ini adalah kelanjutan langsung dari **Praktikum 5 NotesApp**. Fitur awal Praktikum 5 tetap dipertahankan, lalu digabung dengan tugas **Praktikum 7 Local Data Storage** dan **Praktikum 8 Platform-Specific Features**.

## Identitas

- Nama: Jana Rohman Wasiso
- NIM: 123140046
- Mata Kuliah: Pengembangan Aplikasi Mobile

## Fitur dari Praktikum 5 yang tetap dipertahankan

- List notes
- Add note
- Edit note
- Delete note
- Detail note
- Favorite note
- Bottom navigation: Notes, Favorites, Profile

## Upgrade Praktikum 7 - Local Data Storage

- SQLDelight database untuk menyimpan notes secara lokal.
- CRUD operations:
  - Create note
  - Read/list note
  - Update note
  - Delete note
- Favorite tersimpan di database lokal.
- Search functionality untuk mencari note berdasarkan title/content.
- Settings screen:
  - Theme: light, dark, system
  - Sort order: terbaru/terlama
- Offline-first: semua data notes berasal dari database lokal, sehingga tetap ada setelah aplikasi ditutup.
- Empty state pada list notes dan hasil pencarian.

## Upgrade Praktikum 8 - Platform-Specific Features

- Koin Dependency Injection untuk seluruh dependency utama.
- `expect/actual` untuk:
  - `DatabaseDriverFactory`
  - `SettingsFactory`
  - `DeviceInfo`
  - `NetworkMonitor`
- Device Info di Profile & Settings screen:
  - Device name
  - OS version
  - App version
- Network Status Indicator di halaman Notes.
- Android implementation memakai `ConnectivityManager`.
- iOS implementation disediakan sebagai stub sederhana agar struktur KMP tetap lengkap.

## Database Schema

File schema ada di:

```text
composeApp/src/commonMain/sqldelight/com/example/demop4app_123140046/database/NoteEntity.sq
```

Schema utama:

```sql
CREATE TABLE NoteEntity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    is_favorite INTEGER AS Boolean NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);
```

Query yang tersedia:

- `selectAllNewest`
- `selectAllOldest`
- `selectById`
- `selectFavorites`
- `searchNewest`
- `searchOldest`
- `insert`
- `update`
- `toggleFavorite`
- `deleteById`

## Architecture Diagram

```text
UI Compose Screens
       |
       v
ViewModel
NotesViewModel / SettingsViewModel
       |
       v
Repository Layer
NoteRepository / SettingsRepository
       |
       v
Local Data Layer
SQLDelight NotesDatabase / Multiplatform Settings
       |
       v
Platform Layer expect/actual
DatabaseDriverFactory / SettingsFactory / DeviceInfo / NetworkMonitor
       |
       v
Koin DI Modules
commonModule + platformModule
```

## Dependency Injection

Dependency utama diregistrasikan di:

```text
composeApp/src/commonMain/kotlin/com/example/demop4app_123140046/di/AppModule.kt
```

Android platform module:

```text
composeApp/src/androidMain/kotlin/com/example/demop4app_123140046/platform/PlatformModule.android.kt
```

Koin diinisialisasi dari `MainActivity` agar Android context dapat digunakan oleh database, settings, device info, dan network monitor.

## Cara menjalankan

1. Buka project di Android Studio.
2. Pastikan koneksi internet aktif saat Gradle sync pertama kali karena project memakai SQLDelight, Koin, dan Multiplatform Settings.
3. Jalankan konfigurasi Android `composeApp`.
4. Test fitur:
   - tambah note
   - edit note
   - delete note
   - favorite note
   - search note
   - ubah theme dan sort order
   - matikan internet/aktifkan airplane mode untuk melihat network indicator

## Catatan Pengumpulan

Branch yang disarankan:

- `week-7-8` atau sesuai instruksi dosen/asisten.

Isi demo video 45 detik yang disarankan:

1. Tambah note baru.
2. Edit note.
3. Search note.
4. Favorite/unfavorite note.
5. Buka Profile & Settings dan tampilkan Device Info.
6. Matikan internet untuk menampilkan Network Status Indicator.

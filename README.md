# Tugas Praktikum 10 - Testing & Dependency Injection

**Nama**  : Jana Rohman Wasiso  
**NIM**   : 123140046  
**Kelas** : Pengembangan Aplikasi Mobile

---

## Deskripsi Proyek

Proyek ini berfokus pada implementasi **Testing** yang komprehensif dan **Dependency Injection (DI)
** menggunakan Koin pada aplikasi Notes berbasis Kotlin Multiplatform (KMP). Tujuan utamanya adalah
memastikan reliabilitas logika bisnis (Repository & ViewModel), aliran data reaktif (Flow), serta
interaksi antarmuka pengguna (UI) berjalan dengan sempurna.

## Implementasi Testing

Pengujian dibagi menjadi empat kategori utama untuk mencakup seluruh lapisan arsitektur aplikasi:

### 1. Repository Test

Menguji integritas data layer yang terhubung dengan SQLDelight. Fokus pengujian meliputi:

- Validasi fungsi CRUD (Create, Read, Update, Delete) pada database.
- Pemastian query pencarian (*Search*) dan filter favorit bekerja sesuai logika database.

### 2. ViewModel Test

Menguji logika bisnis pada layer presentasi menggunakan **MockK**. Fokus pengujian meliputi:

- Perubahan State UI saat terjadi aksi pengguna (tambah, edit, hapus).
- Validasi interaksi antara ViewModel dengan Repository menggunakan mock dependencies.

### 3. Flow Test (Reactive Logic)

Menggunakan library **Turbine** untuk menguji aliran data asinkron pada `StateFlow`. Pengujian
memastikan:

- Data berhasil di-emit dengan benar saat terjadi perubahan di database.
- Sinkronisasi antara query pencarian dengan daftar catatan yang ditampilkan.

### 4. UI Test (Compose)

Pengujian integrasi tampilan menggunakan `ComposeTestRule`. Test case yang dilakukan meliputi:

- Memastikan *Empty State* muncul saat tidak ada catatan.
- Validasi fungsionalitas *Search Input* (mengetik dan memfilter list).
- Memastikan daftar catatan (*Notes List*) tampil dengan benar setelah data dimuat.

## Library yang Digunakan

- **JUnit4**: Framework dasar pengujian unit.
- **MockK**: Library untuk mocking objek pada unit test.
- **Turbine**: Library khusus untuk pengujian Kotlin Coroutines Flow.
- **Kotlin Coroutines Test**: Untuk menangani eksekusi kode asinkron di lingkungan testing.
- **Compose UI Test**: Untuk menguji komponen UI Jetpack Compose.
- **Koin Test**: Untuk memvalidasi Dependency Injection di dalam pengujian UI.

### Screenshot Hasil Testing
<img width="1919" height="1021" alt="Screenshot PAM 10" src="https://github.com/user-attachments/assets/bbd65c60-fcf3-402b-9e93-652a66e92140" />
<img width="1919" height="970" alt="Screenshot 2026-05-13 221647" src="https://github.com/user-attachments/assets/9d14e24c-6a9a-46eb-93a5-5139844c09be" />
<img width="1919" height="984" alt="Screenshot 2026-05-13 223157" src="https://github.com/user-attachments/assets/95ab6750-6cae-43dd-9be0-215250140f92" />
<img width="1919" height="972" alt="Screenshot 2026-05-13 223221" src="https://github.com/user-attachments/assets/a5ac60ea-2819-4670-84ba-899cca817a21" />
<img width="1919" height="1016" alt="image" src="https://github.com/user-attachments/assets/a19964b1-9ab7-4608-99e5-e10ea08aa3bb" />

---

## Video Demo

https://drive.google.com/file/d/1BnyDFZylBklBlkmk3iiGEZJPPhP3ccmc/view?usp=drive_link 

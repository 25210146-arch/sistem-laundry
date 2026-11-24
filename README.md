# Sistem Reservasi Laundry O2O (Online-to-Offline)

Aplikasi web manajemen dan reservasi layanan laundry berbasis **Java Spring Boot** yang menerapkan konsep *Online-to-Offline* (O2O). Pelanggan dapat melakukan pemesanan dan memantau ketersediaan kuota slot secara daring sebelum mengantarkan cucian ke gerai untuk mencegah antrean fisik.

**Live Demo:** https://sistem-laundry-production.up.railway.app

---

## Akses & Kredensial Uji Coba (Role Admin)

Untuk keperluan pengujian dashboard pengelola/kasir (POS), silakan gunakan akses berikut:

- **URL Dashboard Admin:** https://sistem-laundry-production.up.railway.app/admin
- **Username:** `admin`
- **Password:** `admin123`

---

## Fitur Utama

- **Reservasi Pelanggan & Cek Kuota Real-Time:** Pelanggan memilih jenis layanan (**Reguler** atau **Express**) serta kategori cucian berdasarkan kapasitas slot yang tersedia dan diperbarui otomatis di database.
- **Nota Digital & QR Code:** Pembuatan kode reservasi unik beserta QR Code untuk mempercepat proses verifikasi saat pelanggan datang ke gerai.
- **Dashboard & Autentikasi Admin (POS):** Halaman khusus admin yang dilindungi sistem login untuk konfirmasi kedatangan, pemindaian QR Code, penimbangan berat cucian, perhitungan biaya otomatis, dan pembaruan status pengerjaan.
- **Database Terkelola (Cloud MySQL):** Penyimpanan data transaksi, pelanggan, kuota kapasitas, dan master layanan menggunakan MySQL yang di-hosting secara cloud.

---

## Teknologi yang Digunakan

- **Backend:** Java, Spring Boot (Spring Web, Spring Data JPA / Hibernate)
- **Frontend:** HTML5, CSS3, Thymeleaf, Bootstrap 5
- **Database:** MySQL
- **Build Tool:** Apache Maven
- **Deployment & Hosting:** Railway.app

---

## Cara Menjalankan di Komputer Lokal (Localhost)

1. **Clone repositori ini:**
   ```bash
   git clone [https://github.com/25210146-arch/sistem-laundry.git](https://github.com/25210146-arch/sistem-laundry.git)
   cd sistem-laundry/sistem-laundry
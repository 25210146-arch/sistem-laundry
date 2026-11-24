# Sistem Reservasi Laundry O2O (Online-to-Offline)

Aplikasi web manajemen dan reservasi layanan laundry berbasis **Java Spring Boot** yang menerapkan konsep *Online-to-Offline* (O2O). Pelanggan dapat melakukan pemesanan dan memantau ketersediaan kuota slot secara daring sebelum mengantarkan cucian ke gerai untuk mencegah antrean fisik.

**Live Demo:** [https://sistem-laundry-production.up.railway.app](https://sistem-laundry-production.up.railway.app)

---

## Fitur Utama

* **Reservasi Pelanggan & Cek Kuota Real-Time:** Pelanggan memilih jenis layanan (**Reguler** atau **Express**) serta kategori cucian berdasarkan kapasitas slot yang tersedia dan diperbarui otomatis di database.
* **Integrasi Data Pelanggan:** Pencatatan data pemesan (Nama Lengkap & Nomor WhatsApp/HP) sebagai identitas pengecekan nota dan pengambilan pakaian.
* **Manajemen Layanan & Harga Otomatis:** Perhitungan tarif per kilogram dan estimasi waktu pengerjaan sesuai paket layanan yang dipilih.
* **Database Terkelola (Cloud MySQL):** Penyimpanan data transaksi, kuota kapasitas, dan master layanan menggunakan MySQL yang di-hosting secara cloud.

---

## Teknologi yang Digunakan

* **Backend:** Java, Spring Boot (Spring Web, Spring Data JPA / Hibernate)
* **Frontend:** HTML5, CSS3, Thymeleaf / Bootstrap
* **Database:** MySQL
* **Build Tool:** Apache Maven
* **Deployment & Hosting:** Railway.app

---

## Cara Menjalankan di Komputer Lokal (Localhost)

1. **Clone repositori ini:**
   ```bash
   git clone [https://github.com/25210146-arch/sistem-laundry.git](https://github.com/25210146-arch/sistem-laundry.git)
   cd sistem-laundry/sistem-laundry
package com.laundry.sistem_laundry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pesanan")
public class Pesanan {

    @Id
    @Column(name = "id_pesanan", length = 50)
    private String idPesanan; // Akan diisi dengan ID Reservasi (Contoh: RSV-20261124-001) yang dijadikan QR Code

    @Column(name = "kategori_cucian", length = 50)
    private String kategoriCucian; // Pilihan: Cuci+Kering, Cuci+Lipat, Cuci+Setrika

    @Column(name = "tanggal_pesan")
    private LocalDateTime tanggalPesan;

    @Column(name = "batas_waktu_dropoff")
    private LocalDateTime batasWaktuDropoff; // Tenggat waktu bawa cucian ke toko

    @Column(name = "estimasi_selesai")
    private LocalDateTime estimasiSelesai; // Dihitung otomatis saat admin nimbang cucian

    // Menggunakan tipe objek 'Double' (bukan primitif 'double') agar bisa bernilai null saat pesanan baru dibuat lewat web
    @Column(name = "berat_kg")
    private Double beratKg;

    @Column(name = "total_harga")
    private Double totalHarga;

    @Column(name = "status", length = 50)
    private String status; // Alur: MENUNGGU_KONFIRMASI -> DIKONFIRMASI_ADMIN -> DIPROSES -> SELESAI -> DIAMBIL / HANGUS

    // Relasi ke tabel Pelanggan
    @ManyToOne
    @JoinColumn(name = "id_pelanggan", nullable = false)
    private Pelanggan pelanggan;

    @Column(name = "id_layanan", length = 50)
    private String idLayanan; // Kode L001 (Reguler) atau L002 (Express)

    @Column(name = "id_admin", length = 50)
    private String idAdmin;

    public Pesanan() {
        // Constructor kosong wajib untuk Spring Boot
    }

    // --- GETTER ---
    public String getIdPesanan() { return idPesanan; }
    public String getKategoriCucian() { return kategoriCucian; }
    public LocalDateTime getTanggalPesan() { return tanggalPesan; }
    public LocalDateTime getBatasWaktuDropoff() { return batasWaktuDropoff; }
    public LocalDateTime getEstimasiSelesai() { return estimasiSelesai; }
    public Double getBeratKg() { return beratKg; }
    public Double getTotalHarga() { return totalHarga; }
    public String getStatus() { return status; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public String getIdLayanan() { return idLayanan; }
    public String getIdAdmin() { return idAdmin; }

    // --- SETTER ---
    public void setIdPesanan(String idPesanan) { this.idPesanan = idPesanan; }
    public void setKategoriCucian(String kategoriCucian) { this.kategoriCucian = kategoriCucian; }
    public void setTanggalPesan(LocalDateTime tanggalPesan) { this.tanggalPesan = tanggalPesan; }
    public void setBatasWaktuDropoff(LocalDateTime batasWaktuDropoff) { this.batasWaktuDropoff = batasWaktuDropoff; }
    public void setEstimasiSelesai(LocalDateTime estimasiSelesai) { this.estimasiSelesai = estimasiSelesai; }
    public void setBeratKg(Double beratKg) { this.beratKg = beratKg; }
    public void setTotalHarga(Double totalHarga) { this.totalHarga = totalHarga; }
    public void setStatus(String status) { this.status = status; }
    public void setPelanggan(Pelanggan pelanggan) { this.pelanggan = pelanggan; }
    public void setIdLayanan(String idLayanan) { this.idLayanan = idLayanan; }
    public void setIdAdmin(String idAdmin) { this.idAdmin = idAdmin; }
}
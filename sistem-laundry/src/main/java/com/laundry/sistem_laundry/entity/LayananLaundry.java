package com.laundry.sistem_laundry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "layanan_laundry")
public class LayananLaundry {

    @Id
    @Column(name = "id_layanan", length = 50)
    private String idLayanan;

    @Column(name = "jenis_layanan", length = 100)
    private String jenisLayanan;

    @Column(name = "harga")
    private Double harga;

    @Column(name = "kuota_tersedia")
    private Integer kuotaTersedia;

    @Column(name = "estimasi_jam")
    private Integer estimasiJam;

    // 1. Constructor Kosong (Wajib untuk Spring Boot)
    public LayananLaundry() {
    }

    // 2. Constructor dengan 5 Parameter (Ini yang dibutuhkan oleh DataSeeder!)
    public LayananLaundry(String idLayanan, String jenisLayanan, Double harga, Integer kuotaTersedia, Integer estimasiJam) {
        this.idLayanan = idLayanan;
        this.jenisLayanan = jenisLayanan;
        this.harga = harga;
        this.kuotaTersedia = kuotaTersedia;
        this.estimasiJam = estimasiJam;
    }

    // --- GETTER ---
    public String getIdLayanan() { return idLayanan; }
    public String getJenisLayanan() { return jenisLayanan; }
    public Double getHarga() { return harga; }
    public Integer getKuotaTersedia() { return kuotaTersedia; }
    public Integer getEstimasiJam() { return estimasiJam; }

    // --- SETTER ---
    public void setIdLayanan(String idLayanan) { this.idLayanan = idLayanan; }
    public void setJenisLayanan(String jenisLayanan) { this.jenisLayanan = jenisLayanan; }
    public void setHarga(Double harga) { this.harga = harga; }
    public void setKuotaTersedia(Integer kuotaTersedia) { this.kuotaTersedia = kuotaTersedia; }
    public void setEstimasiJam(Integer estimasiJam) { this.estimasiJam = estimasiJam; }
}
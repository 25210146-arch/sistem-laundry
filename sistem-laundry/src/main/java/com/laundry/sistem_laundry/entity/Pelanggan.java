package com.laundry.sistem_laundry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pelanggan")
public class Pelanggan {

    @Id
    @Column(name = "id_pelanggan", length = 50)
    private String idPelanggan;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "no_hp", length = 20)
    private String noHp;

    // Constructor kosong wajib ada untuk Spring Boot / JPA
    public Pelanggan() {
    }

    // Constructor bawaan dari kode lamamu
    public Pelanggan(String idPelanggan, String nama, String noHp) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.noHp = noHp;
    }

    // Getter
    public String getIdPelanggan() { return idPelanggan; }
    public String getNama() { return nama; }
    public String getNoHp() { return noHp; }

    // Setter (sangat dibutuhkan Spring Boot untuk menangani form HTML)
    public void setIdPelanggan(String idPelanggan) { this.idPelanggan = idPelanggan; }
    public void setNama(String nama) { this.nama = nama; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
}
package com.laundry.sistem_laundry.controller;

import com.laundry.sistem_laundry.entity.LayananLaundry;
import com.laundry.sistem_laundry.entity.Pelanggan;
import com.laundry.sistem_laundry.entity.Pesanan;
import com.laundry.sistem_laundry.repository.LayananLaundryRepository;
import com.laundry.sistem_laundry.repository.PelangganRepository;
import com.laundry.sistem_laundry.repository.PesananRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class LaundryController {

    @Autowired
    private PelangganRepository pelangganRepo;

    @Autowired
    private PesananRepository pesananRepo;

    @Autowired
    private LayananLaundryRepository layananRepo;

    // ==========================================
    // 1. AREA PELANGGAN (FRONT-END)
    // ==========================================

    // Halaman Utama: Etalase Layanan & Form Pemesanan
    @GetMapping("/")
    public String halamanPelanggan(Model model) {
        List<LayananLaundry> daftarLayanan = layananRepo.findAll();
        
        // LOGIKA BARU: SAPU BERSIH DATA ERROR
        if (daftarLayanan.isEmpty()) {
            // 1. Jika benar-benar kosong, buat baru
            layananRepo.save(new LayananLaundry("L01", "Reguler", 7000.0, 10, 48));
            layananRepo.save(new LayananLaundry("L02", "Express", 10000.0, 5, 24));
        } else {
            // 2. Jika data sudah ada tapi kuotanya error/0/null, paksa perbaiki!
            for (LayananLaundry layanan : daftarLayanan) {
                if (layanan.getKuotaTersedia() == null || layanan.getKuotaTersedia() == 0) {
                    if ("L02".equals(layanan.getIdLayanan())) {
                        layanan.setKuotaTersedia(5); // Express kuota 5
                    } else {
                        layanan.setKuotaTersedia(10); // Reguler kuota 10
                    }
                    layananRepo.save(layanan);
                }
            }
        }
        
        // 3. Ambil ulang data yang sudah dipastikan sehat & berkuota
        daftarLayanan = layananRepo.findAll(); 
        model.addAttribute("daftarLayanan", daftarLayanan);
        return "index"; 
    }

    // Halaman Review: Validasi data sebelum konfirmasi permanen
    @PostMapping("/review-pesanan")
    public String reviewPesanan(
            @RequestParam String namaPelanggan,
            @RequestParam String noHp,
            @RequestParam String idLayanan,
            @RequestParam String kategoriCucian,
            Model model) {

        LayananLaundry layanan = layananRepo.findById(idLayanan).orElse(null);
        
        // Melempar data ke halaman review
        model.addAttribute("namaPelanggan", namaPelanggan);
        model.addAttribute("noHp", noHp);
        model.addAttribute("layanan", layanan);
        model.addAttribute("kategoriCucian", kategoriCucian);

        return "review"; 
    }

    // Simpan Pesanan (Booking Kuota)
    @PostMapping("/simpan-pesanan")
    public String simpanPesanan(
            @RequestParam String namaPelanggan,
            @RequestParam String noHp,
            @RequestParam String idLayanan,
            @RequestParam String kategoriCucian) {

        LayananLaundry layanan = layananRepo.findById(idLayanan).orElse(null);
        
        // Validasi ketersediaan kuota
        if (layanan != null && layanan.getKuotaTersedia() != null && layanan.getKuotaTersedia() > 0) {
            
            // 1. Kurangi Kuota (Booking sementara)
            layanan.setKuotaTersedia(layanan.getKuotaTersedia() - 1);
            layananRepo.save(layanan);

            // 2. Simpan/Cari Pelanggan (Gunakan No. HP sebagai basis ID agar tidak duplikat)
            String idPelanggan = "P" + noHp;
            Pelanggan plg = pelangganRepo.findById(idPelanggan).orElse(new Pelanggan(idPelanggan, namaPelanggan, noHp));
            pelangganRepo.save(plg);

            // 3. Generate ID Reservasi untuk QR Code (Format: RSV-YYYYMMDD-001)
            String tgl = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            long urutan = pesananRepo.count() + 1;
            String idReservasi = String.format("RSV-%s-%03d", tgl, urutan);

            // 4. Buat Record Pesanan Baru
            Pesanan psn = new Pesanan();
            psn.setIdPesanan(idReservasi);
            psn.setKategoriCucian(kategoriCucian);
            psn.setTanggalPesan(LocalDateTime.now());
            // Catatan: Berat (beratKg) & totalHarga dibiarkan null karena belum ditimbang
            psn.setStatus("MENUNGGU_KONFIRMASI");
            psn.setPelanggan(plg);
            psn.setIdLayanan(idLayanan);

            pesananRepo.save(psn);
            return "redirect:/nota?id=" + idReservasi; 
        }
        return "redirect:/?error=kuota-habis";
    }

    // Halaman Nota & QR Code Pelanggan
    @GetMapping("/nota")
    public String halamanNota(@RequestParam String id, Model model) {
        Pesanan psn = pesananRepo.findById(id).orElse(null);
        model.addAttribute("pesanan", psn);
        return "nota"; 
    }


    // ==========================================
    // 2. AREA ADMIN (BACK-END / POS)
    // ==========================================

    // Dashboard Admin
    @GetMapping("/admin")
    public String halamanAdmin(Model model) {
        model.addAttribute("daftarPesanan", pesananRepo.findAll());
        return "admin"; 
    }

    // Admin Menerima Pesanan & Mengaktifkan QR (Memberi Batas Waktu Drop-off)
    @PostMapping("/admin/konfirmasi")
    public String konfirmasiAdmin(@RequestParam String idPesanan) {
        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null && "MENUNGGU_KONFIRMASI".equals(psn.getStatus())) {
            psn.setStatus("DIKONFIRMASI_ADMIN");
            // Set toleransi waktu antar cucian: 2 Jam dari sekarang
            psn.setBatasWaktuDropoff(LocalDateTime.now().plusHours(2));
            pesananRepo.save(psn);
        }
        return "redirect:/admin";
    }

    // Admin Menimbang Cucian di Toko (Input Berat -> Hitung Biaya Otomatis)
    @PostMapping("/admin/timbang")
    public String timbangCucian(
            @RequestParam String idPesanan,
            @RequestParam Double beratKg) {

        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null) {
            LayananLaundry layanan = layananRepo.findById(psn.getIdLayanan()).orElse(null);
            psn.setBeratKg(beratKg);
            
            // Hitung Harga dan Estimasi Waktu
            if (layanan != null) {
                // Logika tambahan harga kategori (Misal: Setrika tambah 5000/kg)
                double tambahanKategori = psn.getKategoriCucian().toLowerCase().contains("setrika") ? (5000 * beratKg) : 0;
                psn.setTotalHarga((beratKg * layanan.getHarga()) + tambahanKategori);
                
                // Tambahkan jam estimasi ke waktu saat ini
                Integer estJam = layanan.getEstimasiJam() != null ? layanan.getEstimasiJam() : 24;
                psn.setEstimasiSelesai(LocalDateTime.now().plusHours(estJam));
            }

            psn.setStatus("DIPROSES");
            pesananRepo.save(psn);
        }
        return "redirect:/admin";
    }

    // Pembaruan Status Lanjutan (Selesai / Diambil / Hangus)
    @PostMapping("/admin/update-status")
    public String updateStatusLanjutan(
            @RequestParam String idPesanan, 
            @RequestParam String statusBaru) {
        
        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null) {
            psn.setStatus(statusBaru);
            pesananRepo.save(psn);

            // Pengembalian kuota kapasitas mesin jika cucian selesai/dibatalkan
            if ("SELESAI".equals(statusBaru) || "HANGUS".equals(statusBaru)) {
                LayananLaundry layanan = layananRepo.findById(psn.getIdLayanan()).orElse(null);
                if (layanan != null) {
                    layanan.setKuotaTersedia(layanan.getKuotaTersedia() + 1);
                    layananRepo.save(layanan);
                }
            }
        }
        return "redirect:/admin";
    }
}
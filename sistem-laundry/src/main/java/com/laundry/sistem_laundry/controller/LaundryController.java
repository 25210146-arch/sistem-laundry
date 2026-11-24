package com.laundry.sistem_laundry.controller;

import com.laundry.sistem_laundry.entity.LayananLaundry;
import com.laundry.sistem_laundry.entity.Pelanggan;
import com.laundry.sistem_laundry.entity.Pesanan;
import com.laundry.sistem_laundry.repository.LayananLaundryRepository;
import com.laundry.sistem_laundry.repository.PelangganRepository;
import com.laundry.sistem_laundry.repository.PesananRepository;
import jakarta.servlet.http.HttpSession;
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

    // Kredensial Khusus Admin
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    // ==========================================
    // 1. AREA PELANGGAN (FRONT-END)
    // ==========================================

    @GetMapping("/")
    public String halamanPelanggan(Model model) {
        List<LayananLaundry> daftarLayanan = layananRepo.findAll();
        
        if (daftarLayanan.isEmpty()) {
            layananRepo.save(new LayananLaundry("L01", "Reguler", 7000.0, 10, 48));
            layananRepo.save(new LayananLaundry("L02", "Express", 10000.0, 5, 24));
        } else {
            for (LayananLaundry layanan : daftarLayanan) {
                if (layanan.getKuotaTersedia() == null || layanan.getKuotaTersedia() == 0) {
                    if ("L02".equals(layanan.getIdLayanan())) {
                        layanan.setKuotaTersedia(5);
                    } else {
                        layanan.setKuotaTersedia(10);
                    }
                    layananRepo.save(layanan);
                }
            }
        }
        
        daftarLayanan = layananRepo.findAll(); 
        model.addAttribute("daftarLayanan", daftarLayanan);
        return "index"; 
    }

    @PostMapping("/review-pesanan")
    public String reviewPesanan(
            @RequestParam String namaPelanggan,
            @RequestParam String noHp,
            @RequestParam String idLayanan,
            @RequestParam String kategoriCucian,
            Model model) {

        LayananLaundry layanan = layananRepo.findById(idLayanan).orElse(null);
        
        model.addAttribute("namaPelanggan", namaPelanggan);
        model.addAttribute("noHp", noHp);
        model.addAttribute("layanan", layanan);
        model.addAttribute("kategoriCucian", kategoriCucian);

        return "review"; 
    }

    @PostMapping("/simpan-pesanan")
    public String simpanPesanan(
            @RequestParam String namaPelanggan,
            @RequestParam String noHp,
            @RequestParam String idLayanan,
            @RequestParam String kategoriCucian) {

        LayananLaundry layanan = layananRepo.findById(idLayanan).orElse(null);
        
        if (layanan != null && layanan.getKuotaTersedia() != null && layanan.getKuotaTersedia() > 0) {
            layanan.setKuotaTersedia(layanan.getKuotaTersedia() - 1);
            layananRepo.save(layanan);

            String idPelanggan = "P" + noHp;
            Pelanggan plg = pelangganRepo.findById(idPelanggan).orElse(new Pelanggan(idPelanggan, namaPelanggan, noHp));
            pelangganRepo.save(plg);

            String tgl = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            long urutan = pesananRepo.count() + 1;
            String idReservasi = String.format("RSV-%s-%03d", tgl, urutan);

            Pesanan psn = new Pesanan();
            psn.setIdPesanan(idReservasi);
            psn.setKategoriCucian(kategoriCucian);
            psn.setTanggalPesan(LocalDateTime.now());
            psn.setStatus("MENUNGGU_KONFIRMASI");
            psn.setPelanggan(plg);
            psn.setIdLayanan(idLayanan);

            pesananRepo.save(psn);
            return "redirect:/nota?id=" + idReservasi; 
        }
        return "redirect:/?error=kuota-habis";
    }

    @GetMapping("/nota")
    public String halamanNota(@RequestParam String id, Model model) {
        Pesanan psn = pesananRepo.findById(id).orElse(null);
        model.addAttribute("pesanan", psn);
        return "nota"; 
    }

    // ==========================================
    // 2. AUTENTIKASI ADMIN (LOGIN & LOGOUT)
    // ==========================================

    @GetMapping("/login")
    public String halamanLogin(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdminLoggedIn"))) {
            return "redirect:/admin";
        }
        return "login";
    }

    @PostMapping("/login")
    public String prosesLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            session.setAttribute("isAdminLoggedIn", true);
            return "redirect:/admin";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String prosesLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    // ==========================================
    // 3. AREA ADMIN (BACK-END / POS - TERPROTEKSI)
    // ==========================================

    @GetMapping("/admin")
    public String halamanAdmin(Model model, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdminLoggedIn"))) {
            return "redirect:/login";
        }
        model.addAttribute("daftarPesanan", pesananRepo.findAll());
        return "admin"; 
    }

    @PostMapping("/admin/konfirmasi")
    public String konfirmasiAdmin(@RequestParam String idPesanan, HttpSession session) {
        if (!Boolean.TRUE.equals(session.getAttribute("isAdminLoggedIn"))) {
            return "redirect:/login";
        }
        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null && "MENUNGGU_KONFIRMASI".equals(psn.getStatus())) {
            psn.setStatus("DIKONFIRMASI_ADMIN");
            psn.setBatasWaktuDropoff(LocalDateTime.now().plusHours(2));
            pesananRepo.save(psn);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/timbang")
    public String timbangCucian(
            @RequestParam String idPesanan,
            @RequestParam Double beratKg,
            HttpSession session) {

        if (!Boolean.TRUE.equals(session.getAttribute("isAdminLoggedIn"))) {
            return "redirect:/login";
        }
        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null) {
            LayananLaundry layanan = layananRepo.findById(psn.getIdLayanan()).orElse(null);
            psn.setBeratKg(beratKg);
            
            if (layanan != null) {
                double tambahanKategori = psn.getKategoriCucian().toLowerCase().contains("setrika") ? (5000 * beratKg) : 0;
                psn.setTotalHarga((beratKg * layanan.getHarga()) + tambahanKategori);
                
                Integer estJam = layanan.getEstimasiJam() != null ? layanan.getEstimasiJam() : 24;
                psn.setEstimasiSelesai(LocalDateTime.now().plusHours(estJam));
            }

            psn.setStatus("DIPROSES");
            pesananRepo.save(psn);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/update-status")
    public String updateStatusLanjutan(
            @RequestParam String idPesanan, 
            @RequestParam String statusBaru,
            HttpSession session) {
        
        if (!Boolean.TRUE.equals(session.getAttribute("isAdminLoggedIn"))) {
            return "redirect:/login";
        }
        Pesanan psn = pesananRepo.findById(idPesanan).orElse(null);
        if (psn != null) {
            psn.setStatus(statusBaru);
            pesananRepo.save(psn);

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
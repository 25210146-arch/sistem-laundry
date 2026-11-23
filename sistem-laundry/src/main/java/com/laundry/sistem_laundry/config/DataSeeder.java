package com.laundry.sistem_laundry.config;

import com.laundry.sistem_laundry.entity.LayananLaundry;
import com.laundry.sistem_laundry.repository.LayananLaundryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(LayananLaundryRepository repo) {
        return args -> {
            // Mengecek apakah data layanan sudah ada atau kosong
            if (repo.count() == 0) {
                // (ID Layanan, Jenis, Harga, Kuota Tersedia, Estimasi Jam)
                repo.save(new LayananLaundry("L01", "Reguler", 7000.0, 10, 48));
                repo.save(new LayananLaundry("L02", "Express", 10000.0, 5, 24));
            } else {
                // Jika data sudah ada tapi kuotanya null/0, update kuotanya menjadi penuh lagi
                LayananLaundry reguler = repo.findById("L01").orElse(null);
                if (reguler != null && (reguler.getKuotaTersedia() == null || reguler.getKuotaTersedia() == 0)) {
                    reguler.setKuotaTersedia(10);
                    repo.save(reguler);
                }

                LayananLaundry express = repo.findById("L02").orElse(null);
                if (express != null && (express.getKuotaTersedia() == null || express.getKuotaTersedia() == 0)) {
                    express.setKuotaTersedia(5);
                    repo.save(express);
                }
            }
        };
    }
}
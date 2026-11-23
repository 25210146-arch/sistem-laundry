package com.laundry.sistem_laundry.repository;

import com.laundry.sistem_laundry.entity.Pelanggan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PelangganRepository extends JpaRepository<Pelanggan, String> {
}
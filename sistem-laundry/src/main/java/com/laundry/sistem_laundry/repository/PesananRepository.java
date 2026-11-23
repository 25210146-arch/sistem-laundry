package com.laundry.sistem_laundry.repository;

import com.laundry.sistem_laundry.entity.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesananRepository extends JpaRepository<Pesanan, String> {
}
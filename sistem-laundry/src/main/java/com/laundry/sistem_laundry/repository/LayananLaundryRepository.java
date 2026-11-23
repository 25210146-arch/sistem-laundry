package com.laundry.sistem_laundry.repository;

import com.laundry.sistem_laundry.entity.LayananLaundry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LayananLaundryRepository extends JpaRepository<LayananLaundry, String> {
}
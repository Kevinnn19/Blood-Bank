package com.example.bloodbank.project.Repository;

import com.example.bloodbank.project.entities.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BloodStockRepository extends JpaRepository<BloodStock, Long> 
{
    Optional<BloodStock> findByBloodType(String bloodType);
	List<BloodStock> findByBloodTypeIn(List<String> compatibleTypes);
}

package com.example.bloodbank.project.Repository;

import com.example.bloodbank.project.entities.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> 
{
    List<BloodRequest> findByStatus(String status);
    List<BloodRequest> findByRecipientId(Long recipientId);
}

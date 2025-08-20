package com.example.bloodbank.project.Repository;

import com.example.bloodbank.project.entities.Recipient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> 
{
	Optional<Recipient> findByUsername(String username);
}

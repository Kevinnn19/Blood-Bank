package com.example.bloodbank.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.bloodbank.project.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> 
{}
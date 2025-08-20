package com.example.bloodbank.project.service;

import com.example.bloodbank.project.Repository.RecipientRepository;
import com.example.bloodbank.project.Repository.AuthorityRepository;
import com.example.bloodbank.project.entities.Recipient;
import com.example.bloodbank.project.entities.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RecipientService 
{
    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
//  Add Recipient
    public Recipient addRecipient(Recipient recipient) 
    {
        List<String> validBloodTypes = Arrays.asList("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-");
        if (!validBloodTypes.contains(recipient.getBloodType().toUpperCase())) 
        {
            throw new IllegalArgumentException("Invalid blood type. Allowed values are: A+, A-, B+, B-, O+, O-, AB+, AB-");
        }

        recipient.setPassword(passwordEncoder.encode(recipient.getPassword()));

        Recipient savedRecipient = recipientRepository.save(recipient);

        Authority authority = new Authority("ROLE_RECIPIENT", savedRecipient);
        authorityRepository.save(authority);

        return savedRecipient;
    }
    
//  Get all Recipients
    public List<Recipient> getAllRecipients() {
        return recipientRepository.findAll();
    }
    
//  Get Recipient By Id
    public Recipient getRecipientById(Long id) 
    {
        String loggedInUsername = getLoggedInUsername();

        Recipient loggedInRecipient = recipientRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!loggedInRecipient.getId().equals(id)) 
        {
            throw new RuntimeException("Access denied: You can only view your own profile.");
        }

        return loggedInRecipient;
    }

    private String getLoggedInUsername() 
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) 
        {
            return ((UserDetails) principal).getUsername();
        } 
        else 
        {
            return principal.toString();
        }
    }

//  Update Recippient By Admin
    public Recipient updateRecipientByAdmin(Long id, Recipient updatedRecipient) 
    {
        Recipient existing = recipientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        existing.setBloodType(updatedRecipient.getBloodType());
        existing.setContactNumber(updatedRecipient.getContactNumber());
        existing.setAddress(updatedRecipient.getAddress());
        existing.setEmail(updatedRecipient.getEmail());
        existing.setUsername(updatedRecipient.getUsername());

        return recipientRepository.save(existing);
    }

//  Delete Recipient   
    public void deleteRecipient(Long id) 
    {
        if (!recipientRepository.existsById(id)) 
        {
            throw new RuntimeException("Recipient not found.");
        }
        recipientRepository.deleteById(id);
    }
}

package com.example.bloodbank.project.controller;

import com.example.bloodbank.project.entities.Recipient;
import com.example.bloodbank.project.service.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipient")
public class RecipientController 
{
    @Autowired
    private RecipientService recipientService;
    
//  Add Recipient  
    @PostMapping("/add")
    public Recipient addRecipient(@RequestBody Recipient recipient) 
    {
        return recipientService.addRecipient(recipient);
    }
//    {
//    	  "username": "diya1",
//    	  "email": "diya1@gmail.com",
//    	  "password": "diya1",
//    	  "bloodType": "A+",
//    	  "age": 30,
//    	  "contactNumber": "9876543210",
//    	  "address": "xyz"
//    	}

//  Get all recipients 
    @GetMapping("/all")
    public List<Recipient> getAllRecipients() 
    {
        return recipientService.getAllRecipients();
    }
    
//  Get recipient by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipientById(@PathVariable Long id) 
    {
        try 
        {
            Recipient recipient = recipientService.getRecipientById(id);
            return ResponseEntity.ok(recipient);
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"error\": \"Access Denied: You can only view your own profile.\"}");
        }
    }
    
//  Update Recipient By Id
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecipientByAdmin(@PathVariable Long id, @RequestBody Recipient recipient) 
    {
        try 
        {
            Recipient updated = recipientService.updateRecipientByAdmin(id, recipient);
            return ResponseEntity.ok(updated);
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipient not found.");
        }
    }
    
//       {
//    	  "username": "updatedUser",
//    	  "email": "updated@example.com",
//    	  "bloodType": "B+",
//    	  "contactNumber": "9999999999",
//    	  "address": "Updated City"
//    	}


//  delete recipient by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecipient(@PathVariable Long id) 
    {
        try 
        {
            recipientService.deleteRecipient(id);
            return ResponseEntity.ok("Recipient deleted successfully.");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Recipient not found or could not be deleted.");
        }
    }
}

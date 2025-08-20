package com.example.bloodbank.project.controller;

import com.example.bloodbank.project.entities.BloodStock;
import com.example.bloodbank.project.service.BloodStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bloodstock")
public class BloodStockController 
{
    @Autowired
    private BloodStockService bloodStockService;
   
//	Get All BloodStock
    @GetMapping("/get/all")
    public ResponseEntity<List<BloodStock>> getAllStock() 
    {
        List<BloodStock> stocks = bloodStockService.getAllBloodStock();
        return ResponseEntity.ok(stocks);
    }

//	Get Stock By Bloodtype
    @GetMapping("/get/{bloodType}")
    public ResponseEntity<?> getStock(@PathVariable String bloodType) 
    {
        if (!VALID_BLOOD_TYPES.contains(bloodType)) 
        {
            return ResponseEntity.badRequest().body("Error: Not a valid blood type. Please use one of: " + VALID_BLOOD_TYPES);
        }
        
        Optional<BloodStock> stock = bloodStockService.getBloodStockByType(bloodType);
        if (stock.isEmpty()) 
        {
            return ResponseEntity.status(404).body("No stock available for blood type: " + bloodType);
        }

        return ResponseEntity.ok(stock.get());
    }
    
//	Update Stock
    @PutMapping("/update")
    public BloodStock updateStock(@RequestBody Map<String, Object> requestData) 
    {
        String bloodType = (String) requestData.get("bloodType");
        int quantity = (int) requestData.get("quantity");

        return bloodStockService.updateStock(bloodType, quantity);
    }
    
//  List of valid blood types
    private static final List<String> VALID_BLOOD_TYPES = Arrays.asList
    (
            "A+", "AB+", "B+", "O+", "A-", "AB-", "B-", "O-"
    );
    
//  {
//	  "bloodType": "B+",
//	  "quantity": 5
//	}
}

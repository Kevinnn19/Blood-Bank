package com.example.bloodbank.project.service;

import com.example.bloodbank.project.Repository.BloodStockRepository;
import com.example.bloodbank.project.entities.BloodStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodStockService 
{
    @Autowired
    private BloodStockRepository bloodStockRepository;
    
 // Get all blood stock records
    public List<BloodStock> getAllBloodStock() 
    {
        return bloodStockRepository.findAll();
    }

//    Get Blood Stock by Type
    public Optional<BloodStock> getBloodStockByType(String bloodType) 
    {
        return bloodStockRepository.findByBloodType(bloodType);
    }

//  Update BloodStock
    public BloodStock updateStock(String bloodType, int quantity)
    {
        Optional<BloodStock> stockOpt = bloodStockRepository.findByBloodType(bloodType);

        BloodStock stock;
        if (stockOpt.isPresent()) 
        {
            stock = stockOpt.get();
            stock.setQuantity(stock.getQuantity() + quantity);
        } 
        else 
        {
            stock = new BloodStock();
            stock.setBloodType(bloodType);
            stock.setQuantity(quantity);
        }
        
        return bloodStockRepository.save(stock);
    }
}

package com.example.bloodbank.project.controller;

import com.example.bloodbank.project.entities.BloodRequest;
import com.example.bloodbank.project.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController 
{
    @Autowired
    private BloodRequestService bloodRequestService;

//	Get all pending requests
    @GetMapping("/pending")
    public List<BloodRequest> getPendingRequests() 
    {
        return bloodRequestService.getPendingRequests();
    }
    
//  Create Blood  Request
    @PostMapping("/create")
    public ResponseEntity<?> createBloodRequest(@RequestBody Map<String, Object> requestData,
                                                Principal principal) 
    {
        Long recipientId = Long.valueOf(requestData.get("recipientId").toString());
        String bloodType = requestData.get("bloodType").toString();
        int quantity = Integer.parseInt(requestData.get("quantity").toString());

        String loggedInUsername = principal.getName();

        try 
        {
            BloodRequest savedRequest = bloodRequestService.createRequest(recipientId, bloodType, quantity, loggedInUsername);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
        } 
        catch (SecurityException e) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You can only make requests for your own account.");
        } 
        catch (RuntimeException e) 
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipient not found.");
        }
    }
    
//	  json input 
//    {
//    	  "recipientId": 4,
//    	  "bloodType": "O+",
//    	  "quantity": 2
//    }

//  Approve request and update blood stock
    @PutMapping("/approve/{requestId}")
    public String approveRequest(@PathVariable Long requestId) 
    {
        return bloodRequestService.approveRequest(requestId);
    }
    
//    json input 
//    {
//    	 "requestId": 1
//    }

//  Reject request
    @PutMapping("/reject/{requestId}")
    public String rejectRequest(@PathVariable Long requestId) 
    {
        return bloodRequestService.rejectRequest(requestId);
    }
    
//  json input 
//  {
//  	"requestId": 1
//  }  
}

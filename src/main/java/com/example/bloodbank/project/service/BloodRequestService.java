package com.example.bloodbank.project.service;
import com.example.bloodbank.project.Repository.BloodRequestRepository;
import com.example.bloodbank.project.Repository.BloodStockRepository;
import com.example.bloodbank.project.Repository.RecipientRepository;
//
//import com.example.bloodbank.project.Repository.BloodRequestRepository;
//import com.example.bloodbank.project.Repository.BloodStockRepository;
//import com.example.bloodbank.project.Repository.RecipientRepository;
//import com.example.bloodbank.project.entities.BloodRequest;
//import com.example.bloodbank.project.entities.BloodStock;
//import com.example.bloodbank.project.entities.Recipient;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class BloodRequestService {
//
//    @Autowired
//    private BloodRequestRepository bloodRequestRepository;
//
//    @Autowired
//    private BloodStockRepository bloodStockRepository;
//
//    // Valid Blood Types
//    private static final Set<String> VALID_BLOOD_TYPES = Set.of("A+", "AB+", "B+", "O+", "A-", "AB-", "B-", "O-");
//
//    // ✅ View All Pending Requests
//    public List<BloodRequest> getPendingRequests() {
//        return bloodRequestRepository.findByStatus(BloodRequest.RequestStatus.PENDING);
//    }
//
////    // ✅ Recipients Request Blood
//    public BloodRequest createRequest(Long recipientId, String bloodType) {
//        Recipient recipient = RecipientRepository.findById(recipientId)
//            .orElseThrow(() -> new RuntimeException("Recipient not found"));
//
//        BloodRequest request = new BloodRequest(recipient, bloodType);
//        return bloodRequestRepository.save(request);
//    }
//
//    public List<BloodRequest> getRequestsByRecipient(Long recipientId) {
//        return bloodRequestRepository.findByRecipientId(recipientId);
//    }
//
//    public BloodRequest updateRequestStatus(Long requestId, String status) {
//        BloodRequest request = bloodRequestRepository.findById(requestId)
//            .orElseThrow(() -> new RuntimeException("Request not found"));
//
//        request.setStatus(status);
//        return bloodRequestRepository.save(request);
//    }
//    
//
//    // ✅ Admin Approves Request & Updates Stock
//    @Transactional
//    public String approveRequest(Long requestId) {
//        Optional<BloodRequest> requestOpt = bloodRequestRepository.findById(requestId);
//        if (requestOpt.isEmpty()) {
//            return "Error: Request not found!";
//        }
//
//        BloodRequest request = requestOpt.get();
//        Optional<BloodStock> stockOpt = bloodStockRepository.findByBloodType(request.getBloodType());
//
//        if (stockOpt.isEmpty()) {
//            request.rejectRequest();
//            bloodRequestRepository.save(request);
//            return "Error: Blood stock not available for the requested type.";
//        }
//
//        BloodStock stock = stockOpt.get();
//        if (stock.getQuantity() < request.getQuantity()) {
//            request.rejectRequest();
//            bloodRequestRepository.save(request);
//            return "Error: Not enough stock available. Request rejected.";
//        }
//
//        // Update stock
//        stock.decreaseStock(request.getQuantity());
//        bloodStockRepository.save(stock);
//
//        // Approve request
//        request.approveRequest();
//        bloodRequestRepository.save(request);
//        return "Success: Request approved!";
//    }
//
//    // ✅ Admin Rejects Request
//    public String rejectRequest(Long requestId) {
//        Optional<BloodRequest> requestOpt = bloodRequestRepository.findById(requestId);
//        if (requestOpt.isPresent()) {
//            BloodRequest request = requestOpt.get();
//            request.rejectRequest();
//            bloodRequestRepository.save(request);
//            return "Success: Request rejected.";
//        }
//        return "Error: Request not found!";
//    }
//}
import com.example.bloodbank.project.entities.BloodRequest;
import com.example.bloodbank.project.entities.BloodStock;
import com.example.bloodbank.project.entities.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService 
{
    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private RecipientRepository recipientRepository;

    @Autowired
    private BloodStockRepository bloodStockRepository;

//  Get all pending requests
    public List<BloodRequest> getPendingRequests() 
    {
        return bloodRequestRepository.findByStatus("PENDING");
    }
    
//  Make Request For blood
    public BloodRequest createRequest(Long recipientId, String bloodType, int quantity, String username) 
    {
        Recipient recipient = recipientRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        if (!recipient.getUsername().equals(username)) 
        {
            throw new SecurityException("You are not allowed to make requests for another user.");
        }

        BloodRequest request = new BloodRequest(recipient, bloodType, quantity);
        
//      Check for A+
        if ("A+".equalsIgnoreCase(bloodType)) 
        {
            List<String> compType = Arrays.asList("A+", "A-", "O+", "O-");
            List<BloodStock> compStock = bloodStockRepository.findByBloodTypeIn(compType);

            int Available = compStock.stream()
                    .mapToInt(BloodStock::getQuantity)
                    .sum();

            if (Available < quantity) 
            {
                request.rejectRequest();
            } 
            else 
            {
                int remaining = quantity;
                for (BloodStock stock : compStock) {
                    int current = stock.getQuantity();
                    if (current >= remaining) 
                    {
                        stock.decreaseStock(remaining);
                        bloodStockRepository.save(stock);
                        break;
                    } 
                    else 
                    {
                        stock.decreaseStock(current);
                        bloodStockRepository.save(stock);
                        remaining -= current;
                    }
                }
                request.approveRequest();
            }
        } 
        else 
        {
            BloodStock stock = bloodStockRepository.findByBloodType(bloodType).orElse(null);
            if (stock == null || stock.getQuantity() < quantity) 
            {
                request.rejectRequest();
            } 
            else 
            {
                stock.decreaseStock(quantity);
                bloodStockRepository.save(stock);
                request.approveRequest();
            }
        }
//      Check for B+
        if ("B+".equalsIgnoreCase(bloodType)) 
        {
            List<String> compType = Arrays.asList("B+", "B-", "O+", "O-");
            List<BloodStock> compStock = bloodStockRepository.findByBloodTypeIn(compType);

            int Available = compStock.stream()
                    .mapToInt(BloodStock::getQuantity)
                    .sum();

            if (Available < quantity) 
            {
                request.rejectRequest();
            } 
            else 
            {
                int remaining = quantity;
                for (BloodStock stock : compStock) {
                    int current = stock.getQuantity();
                    if (current >= remaining) 
                    {
                        stock.decreaseStock(remaining);
                        bloodStockRepository.save(stock);
                        break;
                    } 
                    else 
                    {
                        stock.decreaseStock(current);
                        bloodStockRepository.save(stock);
                        remaining -= current;
                    }
                }
                request.approveRequest();
            }
        } 
        else 
        {
            BloodStock stock = bloodStockRepository.findByBloodType(bloodType).orElse(null);
            if (stock == null || stock.getQuantity() < quantity) 
            {
                request.rejectRequest();
            } 
            else 
            {
                stock.decreaseStock(quantity);
                bloodStockRepository.save(stock);
                request.approveRequest();
            }
        }


        return bloodRequestRepository.save(request);
    }



//  Approve request and update blood stock
    @Transactional
    public String approveRequest(Long requestId) 
    {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Optional<BloodStock> stockOpt = bloodStockRepository.findByBloodType(request.getBloodType());

        if (stockOpt.isEmpty()) 
        {
            request.rejectRequest();
            bloodRequestRepository.save(request);
            return "Error: Blood stock not available for the requested type.";
        }

        BloodStock stock = stockOpt.get();

        if (stock.getQuantity() < request.getQuantity()) 
        {
            request.rejectRequest();
            bloodRequestRepository.save(request);
            return "Error: Not enough stock available. Request rejected.";
        }

        stock.decreaseStock(request.getQuantity());
        bloodStockRepository.save(stock);

        request.approveRequest();
        bloodRequestRepository.save(request);
        return "Success: Request approved!";
    }

//  Reject request
    public String rejectRequest(Long requestId) 
    {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.rejectRequest();
        bloodRequestRepository.save(request);
        return "Success: Request rejected.";
    }
}

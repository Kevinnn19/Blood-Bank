package com.example.bloodbank.project.entities;

import jakarta.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Recipient extends User 
{

    private String bloodType; // A+, A-, B+, B-, O+, O-, AB+, AB-
    private String contactNumber;
    private String address;
    private String email;  // ✅ Added Email Field

    public Recipient() 
    {
        super();
    }

    public Recipient(String bloodType, String contactNumber, String address, String email) 
    {
        super();
        this.bloodType = bloodType;
        this.contactNumber = contactNumber;
        this.address = address;
        this.email = email;  // ✅ Assign Email
    }

    @Override
    public String toString() 
    {
        return "Recipient [bloodType=" + bloodType + ", contactNumber=" + contactNumber +
               ", address=" + address + ", email=" + email + "]";
    }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

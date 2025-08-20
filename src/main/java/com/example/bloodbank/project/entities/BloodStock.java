package com.example.bloodbank.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "blood_stock")
public class BloodStock 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bloodType; // A+, A-, B+, B-, O+, O-, AB+, AB-

    @Column(nullable = false)
    private int quantity;

    public void decreaseStock(int amount) 
    {
        if (this.quantity >= amount) 
        {
            this.quantity -= amount;
        } 
        else 
        {
            throw new RuntimeException("Not enough blood stock available.");
        }
    }

    public void increaseStock(int amount) 
    {
        this.quantity += amount;
    }
     
	public BloodStock() 
	{
		super();
	}

	public BloodStock(Long id, String bloodType, int quantity) 
	{
		super();
		this.id = id;
		this.bloodType = bloodType;
		this.quantity = quantity;
	}

	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}

	public String getBloodType() 
	{
		return bloodType;
	}

	public void setBloodType(String bloodType) 
	{
		this.bloodType = bloodType;
	}

	public int getQuantity() 
	{
		return quantity;
	}

	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}

	@Override
	public String toString() 
	{
		return "BloodStock [id=" + id + ", bloodType=" + bloodType + ", quantity=" + quantity + "]";
	}    
}

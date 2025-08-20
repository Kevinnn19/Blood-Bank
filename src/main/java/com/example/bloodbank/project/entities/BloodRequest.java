package com.example.bloodbank.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "blood_requests")
public class BloodRequest 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Recipient recipient;

    @Column(nullable = false)
    private String bloodType;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String status = "PENDING";

    // Constructor
    public BloodRequest(Recipient recipient, String bloodType, int quantity) 
    {
        this.recipient = recipient;
        this.bloodType = bloodType;
        this.quantity = quantity;
        this.status = "PENDING"; // Default status as String
    }

    // Approve Request
    public void approveRequest() 
    {
        this.status = "APPROVED";
    }

    // Reject Request
    public void rejectRequest() 
    {
        this.status = "REJECTED";
    }

    public BloodRequest() 
    {
		super();
		// TODO Auto-generated constructor stub
	}

	public BloodRequest(Long id, Recipient recipient, String bloodType, int quantity, String status) 
	{
		super();
		this.id = id;
		this.recipient = recipient;
		this.bloodType = bloodType;
		this.quantity = quantity;
		this.status = status;
	}

	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}

	public Recipient getRecipient() 
	{
		return recipient;
	}

	public void setRecipient(Recipient recipient) 
	{
		this.recipient = recipient;
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

	public String getStatus() 
	{
		return status;
	}

	public void setStatus(String status) 
	{
		this.status = status;
	}

	@Override
    public String toString() 
	{
        return "BloodRequest [id=" + id + ", recipient=" + recipient + ", bloodType=" + bloodType + ", quantity="
                + quantity + ", status=" + status + "]";
    }
}

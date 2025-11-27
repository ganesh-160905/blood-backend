package com.lifelinelink.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_donations")
public class BloodDonation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bank_id")
    private BloodBank bloodBank;
    
    @NotNull
    @Column(name = "donation_date")
    private LocalDateTime donationDate;
    
    @NotBlank
    @Column(name = "blood_type")
    private String bloodType;
    
    @Column(name = "units_donated")
    private Double unitsDonated = 1.0;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status = DonationStatus.COMPLETED;
    
    @Enumerated(EnumType.STRING)
    private DonationType type = DonationType.VOLUNTARY;
    
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public BloodDonation() {}
    
    public BloodDonation(User user, BloodBank bloodBank, LocalDateTime donationDate, 
                        String bloodType, Double unitsDonated, DonationType type) {
        this.user = user;
        this.bloodBank = bloodBank;
        this.donationDate = donationDate;
        this.bloodType = bloodType;
        this.unitsDonated = unitsDonated;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public BloodBank getBloodBank() {
        return bloodBank;
    }
    
    public void setBloodBank(BloodBank bloodBank) {
        this.bloodBank = bloodBank;
    }
    
    public LocalDateTime getDonationDate() {
        return donationDate;
    }
    
    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }
    
    public String getBloodType() {
        return bloodType;
    }
    
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    
    public Double getUnitsDonated() {
        return unitsDonated;
    }
    
    public void setUnitsDonated(Double unitsDonated) {
        this.unitsDonated = unitsDonated;
    }
    
    public DonationStatus getStatus() {
        return status;
    }
    
    public void setStatus(DonationStatus status) {
        this.status = status;
    }
    
    public DonationType getType() {
        return type;
    }
    
    public void setType(DonationType type) {
        this.type = type;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

enum DonationStatus {
    SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
}

enum DonationType {
    VOLUNTARY, EMERGENCY, REPLACEMENT
}
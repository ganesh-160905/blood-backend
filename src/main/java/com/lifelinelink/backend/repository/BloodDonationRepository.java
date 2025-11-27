package com.lifelinelink.backend.repository;

import com.lifelinelink.backend.entity.BloodDonation;
import com.lifelinelink.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long> {
    
    List<BloodDonation> findByUser(User user);
    
    List<BloodDonation> findByUserOrderByDonationDateDesc(User user);
    
    List<BloodDonation> findByBloodType(String bloodType);
    
    @Query("SELECT bd FROM BloodDonation bd WHERE bd.donationDate BETWEEN :startDate AND :endDate")
    List<BloodDonation> findDonationsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(bd) FROM BloodDonation bd WHERE bd.user = :user")
    Long countDonationsByUser(@Param("user") User user);
    
    @Query("SELECT SUM(bd.unitsDonated) FROM BloodDonation bd WHERE bd.user = :user")
    Double getTotalUnitsDonatedByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(bd) FROM BloodDonation bd")
    Long countTotalDonations();
    
    @Query("SELECT SUM(bd.unitsDonated) FROM BloodDonation bd")
    Double getTotalUnitsCollected();
}
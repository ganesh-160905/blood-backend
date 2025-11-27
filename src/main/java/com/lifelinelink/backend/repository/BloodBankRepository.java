package com.lifelinelink.backend.repository;

import com.lifelinelink.backend.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    
    List<BloodBank> findByCity(String city);
    
    List<BloodBank> findByCityAndState(String city, String state);
    
    List<BloodBank> findByIsVerified(Boolean isVerified);
    
    @Query("SELECT bb FROM BloodBank bb WHERE bb.city = :city AND bb.isVerified = true")
    List<BloodBank> findVerifiedBloodBanksByCity(@Param("city") String city);
    
    @Query(value = "SELECT *, " +
           "( 6371 * acos( cos( radians(:latitude) ) * cos( radians( latitude ) ) * " +
           "cos( radians( longitude ) - radians(:longitude) ) + sin( radians(:latitude) ) * " +
           "sin( radians( latitude ) ) ) ) AS distance " +
           "FROM blood_banks " +
           "HAVING distance < :radius " +
           "ORDER BY distance", nativeQuery = true)
    List<BloodBank> findBloodBanksWithinRadius(@Param("latitude") Double latitude, 
                                              @Param("longitude") Double longitude, 
                                              @Param("radius") Double radius);
}

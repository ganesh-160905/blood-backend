package com.lifelinelink.backend.repository;

import com.lifelinelink.backend.entity.BloodInventory;
import com.lifelinelink.backend.entity.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    
    List<BloodInventory> findByBloodBank(BloodBank bloodBank);
    
    List<BloodInventory> findByBloodType(String bloodType);
    
    Optional<BloodInventory> findByBloodBankAndBloodType(BloodBank bloodBank, String bloodType);
    
    @Query("SELECT bi FROM BloodInventory bi WHERE bi.bloodType = :bloodType AND bi.unitsAvailable > 0")
    List<BloodInventory> findAvailableBloodByType(@Param("bloodType") String bloodType);
    
    @Query("SELECT bi FROM BloodInventory bi WHERE bi.bloodBank.city = :city AND bi.bloodType = :bloodType AND bi.unitsAvailable > 0")
    List<BloodInventory> findAvailableBloodByTypeAndCity(@Param("bloodType") String bloodType, @Param("city") String city);
}

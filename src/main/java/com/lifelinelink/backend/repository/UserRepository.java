package com.lifelinelink.backend.repository;

import com.lifelinelink.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    List<User> findByBloodType(String bloodType);
    
    List<User> findByBloodTypeAndCityAndIsAvailable(String bloodType, String city, Boolean isAvailable);
    
    @Query("SELECT u FROM User u WHERE u.bloodType = :bloodType AND u.city = :city AND u.isAvailable = true")
    List<User> findAvailableDonorsByBloodTypeAndCity(@Param("bloodType") String bloodType, @Param("city") String city);
    
    @Query("SELECT u FROM User u WHERE u.bloodType IN :bloodTypes AND u.city = :city AND u.isAvailable = true")
    List<User> findAvailableDonorsByBloodTypesAndCity(@Param("bloodTypes") List<String> bloodTypes, @Param("city") String city);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    Long countTotalUsers();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isAvailable = true")
    Long countAvailableDonors();
}
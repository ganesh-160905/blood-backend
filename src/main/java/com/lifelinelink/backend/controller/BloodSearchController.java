package com.lifelinelink.backend.controller;

import com.lifelinelink.backend.entity.BloodBank;
import com.lifelinelink.backend.entity.BloodInventory;
import com.lifelinelink.backend.entity.User;
import com.lifelinelink.backend.repository.BloodBankRepository;
import com.lifelinelink.backend.repository.BloodInventoryRepository;
import com.lifelinelink.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/blood-search")
public class BloodSearchController {

    @Autowired
    private BloodBankRepository bloodBankRepository;

    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/blood-banks")
    public ResponseEntity<?> searchBloodBanks(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String bloodType,
            @RequestParam(required = false, defaultValue = "50") Double radius,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {

        List<BloodBank> bloodBanks;

        if (latitude != null && longitude != null) {
            // Search by location coordinates
            bloodBanks = bloodBankRepository.findBloodBanksWithinRadius(latitude, longitude, radius);
        } else if (city != null) {
            // Search by city
            bloodBanks = bloodBankRepository.findVerifiedBloodBanksByCity(city);
        } else {
            // Return all verified blood banks
            bloodBanks = bloodBankRepository.findByIsVerified(true);
        }

        // If blood type is specified, filter by availability
        if (bloodType != null) {
            bloodBanks = bloodBanks.stream()
                    .filter(bank -> hasBloodTypeAvailable(bank, bloodType))
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> result = bloodBanks.stream().map(bank -> {
            Map<String, Object> bankInfo = new HashMap<>();
            bankInfo.put("id", bank.getId());
            bankInfo.put("name", bank.getName());
            bankInfo.put("address", bank.getAddress());
            bankInfo.put("city", bank.getCity());
            bankInfo.put("state", bank.getState());
            bankInfo.put("phone", bank.getPhone());
            bankInfo.put("email", bank.getEmail());
            bankInfo.put("latitude", bank.getLatitude());
            bankInfo.put("longitude", bank.getLongitude());
            bankInfo.put("openHours", bank.getOpenHours());
            bankInfo.put("rating", bank.getRating());
            bankInfo.put("isVerified", bank.getIsVerified());
            
            // Get blood inventory for this bank
            List<BloodInventory> inventory = bloodInventoryRepository.findByBloodBank(bank);
            Map<String, Integer> bloodAvailability = new HashMap<>();
            for (BloodInventory inv : inventory) {
                bloodAvailability.put(inv.getBloodType(), inv.getUnitsAvailable());
            }
            bankInfo.put("bloodAvailability", bloodAvailability);
            
            return bankInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/donors")
    public ResponseEntity<?> searchDonors(
            @RequestParam String bloodType,
            @RequestParam(required = false) String city) {

        List<User> donors;

        if (city != null) {
            donors = userRepository.findAvailableDonorsByBloodTypeAndCity(bloodType, city);
        } else {
            donors = userRepository.findByBloodType(bloodType)
                    .stream()
                    .filter(User::getIsAvailable)
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> result = donors.stream().map(donor -> {
            Map<String, Object> donorInfo = new HashMap<>();
            donorInfo.put("id", donor.getId());
            donorInfo.put("name", donor.getFullName());
            donorInfo.put("bloodType", donor.getBloodType());
            donorInfo.put("city", donor.getCity());
            donorInfo.put("state", donor.getState());
            donorInfo.put("totalDonations", donor.getTotalDonations());
            donorInfo.put("lastDonationDate", donor.getLastDonationDate());
            // Don't include personal information like phone/email for privacy
            return donorInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/compatible-donors")
    public ResponseEntity<?> findCompatibleDonors(
            @RequestParam String recipientBloodType,
            @RequestParam(required = false) String city) {

        List<String> compatibleBloodTypes = getCompatibleBloodTypes(recipientBloodType);
        
        List<User> donors;
        if (city != null) {
            donors = userRepository.findAvailableDonorsByBloodTypesAndCity(compatibleBloodTypes, city);
        } else {
            donors = userRepository.findAll()
                    .stream()
                    .filter(user -> compatibleBloodTypes.contains(user.getBloodType()) && user.getIsAvailable())
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> result = donors.stream().map(donor -> {
            Map<String, Object> donorInfo = new HashMap<>();
            donorInfo.put("id", donor.getId());
            donorInfo.put("name", donor.getFullName());
            donorInfo.put("bloodType", donor.getBloodType());
            donorInfo.put("city", donor.getCity());
            donorInfo.put("state", donor.getState());
            donorInfo.put("totalDonations", donor.getTotalDonations());
            donorInfo.put("compatibility", getCompatibilityType(donor.getBloodType(), recipientBloodType));
            return donorInfo;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    private boolean hasBloodTypeAvailable(BloodBank bank, String bloodType) {
        return bloodInventoryRepository.findByBloodBankAndBloodType(bank, bloodType)
                .map(inventory -> inventory.getUnitsAvailable() > 0)
                .orElse(false);
    }

    private List<String> getCompatibleBloodTypes(String recipientBloodType) {
        Map<String, List<String>> compatibility = Map.of(
                "A+", Arrays.asList("A+", "A-", "O+", "O-"),
                "A-", Arrays.asList("A-", "O-"),
                "B+", Arrays.asList("B+", "B-", "O+", "O-"),
                "B-", Arrays.asList("B-", "O-"),
                "AB+", Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"),
                "AB-", Arrays.asList("A-", "B-", "AB-", "O-"),
                "O+", Arrays.asList("O+", "O-"),
                "O-", Arrays.asList("O-")
        );
        
        return compatibility.getOrDefault(recipientBloodType, Arrays.asList(recipientBloodType));
    }

    private String getCompatibilityType(String donorType, String recipientType) {
        if (donorType.equals(recipientType)) {
            return "Perfect Match";
        } else if (donorType.equals("O-")) {
            return "Universal Donor";
        } else {
            return "Compatible";
        }
    }
}
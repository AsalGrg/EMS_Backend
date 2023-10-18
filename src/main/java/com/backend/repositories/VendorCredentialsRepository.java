package com.backend.repositories;

import com.backend.models.VendorCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VendorCredentialsRepository extends JpaRepository<VendorCredential, Integer> {

    Optional<List<VendorCredential>> findByIsVerifiedAndIsDeclined(boolean isVerified, boolean isDeclined);
}

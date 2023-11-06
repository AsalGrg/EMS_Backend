package com.backend.repositories;

import com.backend.models.User;
import com.backend.models.VendorCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorCredentialsRepository extends JpaRepository<VendorCredential, Integer> {

    boolean existsByUser_Username(String username);
    Optional<VendorCredential> findByUser(User user);
    List<VendorCredential> findByIsVerifiedAndIsDeclined(boolean isVerified, boolean isDeclined);

    List<VendorCredential> findByIsTerminated(boolean isTerminated);
}

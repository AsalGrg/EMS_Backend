package com.backend.repositories;

import com.backend.models.VendorCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorCredentialsRepository extends JpaRepository<VendorCredential, Integer> {
}

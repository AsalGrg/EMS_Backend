package com.backend.repositories;

import com.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public Optional<User> findByEmailAndPassword(String email, String password);

    public Optional<User> findByEmailOrUsername(String email, String username);

    public Optional<User> findByUsername(String username);
}

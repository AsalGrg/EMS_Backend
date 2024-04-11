package com.backend.repositories;

import com.backend.models.UserSocials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSocailRepository extends JpaRepository<UserSocials, Integer> {

    UserSocials findUserSocialsByUser_UserId(int userId);
}

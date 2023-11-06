package com.backend.services;

import com.backend.models.Role;

public interface RoleService {

    Role findRoleByTitle(String title);
}

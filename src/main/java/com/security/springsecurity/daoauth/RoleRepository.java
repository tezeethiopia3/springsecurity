package com.security.springsecurity.daoauth;


import com.security.springsecurity.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<AuthRole,Integer>
{
    Optional<AuthRole> findByName(String role);
}

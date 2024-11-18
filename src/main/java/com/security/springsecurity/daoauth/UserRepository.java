package com.security.springsecurity.daoauth;



import com.security.springsecurity.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser,Integer>
{
    Optional<AuthUser> findByEmail(String email);
}

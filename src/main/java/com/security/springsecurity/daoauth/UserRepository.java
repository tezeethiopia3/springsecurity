package com.security.springsecurity.daoauth;



import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser,Integer>
{
    Optional<AuthUser> findByEmail(String email);
    @Query("""
            select u from AuthUser u join roles r where r.name=:rol
            """)
       List<AuthUser> findUserByRoles(String rol);

}

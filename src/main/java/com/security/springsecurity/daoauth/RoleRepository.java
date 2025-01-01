package com.security.springsecurity.daoauth;


import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<AuthRole,Integer>
{
    Optional<AuthRole> findByName(String role);
    @Query(
            """
            select u from AuthRole u join users a on a.email=:email
            """)
    List<AuthRole> findAllRolesOfUser(String email);

}

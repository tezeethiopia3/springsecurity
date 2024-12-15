package com.security.springsecurity.daoauth;

import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthAccessListRepository extends JpaRepository<AuthAccessList,Integer> {
     Optional<AuthAccessList> findByName(String s);
     @Query(
             """
             select u from AuthAccessList u join authRoleList a on a.id=:id
             """)
     List<AuthAccessList> findByRoles(int id);

     @Query("select u from AuthAccessList u join authRoleList a")
     List<AuthAccessList> findByRolesAll();
}

package com.security.springsecurity.daoauth;

import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthAccessListRepository extends JpaRepository<AuthAccessList,Integer> {
     Optional<AuthAccessList> findByName(String s);
}

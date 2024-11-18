package com.security.springsecurity.daoauth;



import com.security.springsecurity.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<AuthToken,Integer>
{
    Optional<AuthToken> findByToken(String token);
}

package com.security.springsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthAccessList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "accessLists",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private Set<AuthRole> authRoleList;

}

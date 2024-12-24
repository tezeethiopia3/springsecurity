package com.security.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @CreatedDate
    @Column(nullable =false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    //please note that the name roles is the name used in user class. It should be same name.
    @ManyToMany(mappedBy ="roles" )
    @JsonIgnore //used to avoid loop when it eagerly fetch data
    private List<AuthUser> users;
//    @JsonIgnore //used to avoid loop when it eagerly fetch data
    //auth role is called target class
//    @ManyToMany(mappedBy = "authRoleList",fetch = FetchType.LAZY)

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "Role_with_Access_list",
            joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "access_id",referencedColumnName = "id")
    )
      private List<AuthAccessList> accessLists;



//    public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
//        return getPermissionAuthorities();
//    }
//    public List<SimpleGrantedAuthority> getAuthorities(){
//        var auth=this.authAccessList.stream().map(p->new
//                        SimpleGrantedAuthority(p.getName()))
//                .collect(Collectors.toList());
//        auth.add(new SimpleGrantedAuthority("ROLE_"+ this.getName()));
//        return auth;
//    }

    public List<SimpleGrantedAuthority> getAuthorities(){

//        System.out.println("Tezera is comming for you=====================");
//
//        System.out.println("Tezera is comming for you 2 this.authAccessList.size()====================="+
//                this.accessLists.size());
//
//        System.out.println("Tezera is comming for you 2 this.authAccessList.size()====================="+
//                this.accessLists);

        var auth=this.accessLists.stream().map(p->new
                        SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());
        auth.add(new SimpleGrantedAuthority(this.getName()));
        return auth;



    }




}

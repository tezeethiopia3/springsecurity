package com.security.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
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
    private boolean status;
    @JsonIgnore
//    @JoinTable(
//            name = "User_Role",
//            joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id")
//    )
//    @ManyToMany(fetch = FetchType.LAZY
//            ,cascade = CascadeType.ALL
//    )
    @ManyToMany(mappedBy = "roles"
//            , fetch = FetchType.LAZY
//            ,cascade = CascadeType.ALL
    )
    private List<AuthUser> users=new ArrayList<>();
//    @JsonIgnore //used to avoid loop when it eagerly fetch data
    //auth role is called target class
//    @ManyToMany(mappedBy = "authRoleList",fetch = FetchType.LAZY)


    @ManyToMany(mappedBy = "authRoleList", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
      private List<AuthAccessList> accessLists=new ArrayList<>();



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

    public void addUser(AuthUser authUser){
        this.users.add(authUser);
    }




}

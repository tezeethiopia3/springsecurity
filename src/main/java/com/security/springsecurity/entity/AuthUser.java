package com.security.springsecurity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements UserDetails, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean accountLocked;
    private boolean enabled;
    @CreatedDate
//    @Column(nullable =false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
//    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @JoinTable(name = "User_role",
            joinColumns=@JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))

    @ManyToMany(
//            fetch = FetchType.LAZY
//            ,cascade = CascadeType.ALL
    )
//    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<AuthRole> roles=new ArrayList<>();

//    @OneToMany(
//            mappedBy = "owner"
//    )
//    List<Book> owner;

//    @OneToMany(
//            mappedBy = "user"
//    )
//    List<BookTransactionHistory> bookTransactionHistories;
    @Override
    public String getName() {
        return email;
    }
//original code
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream()
//                .map(r-> new SimpleGrantedAuthority(r.getName()))
//                .collect(Collectors.toList());
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles.get(0).getAuthorities();
              return this.roles.stream()
                .map(r-> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }





    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    public String fullNmae(){
        return firstName + " " + lastName;
    }
    public void addRole(AuthRole authRole){
        this.roles.add(authRole);
    }
}

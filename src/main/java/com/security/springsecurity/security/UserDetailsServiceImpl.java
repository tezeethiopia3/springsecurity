package com.security.springsecurity.security;


import com.security.springsecurity.daoauth.UserRepository;
import com.security.springsecurity.entity.AuthAccessList;
import com.security.springsecurity.entity.AuthRole;
import com.security.springsecurity.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsServiceImpl===loadUserByUsername");
        System.out.println("UserDetailsServiceImpl===username=="+username);

//        return repository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<AuthUser> userOptional=repository.findByEmail(username);
        AuthUser user=userOptional.get();
//        List<AuthRole> authRoleList=user.getRoles();
//        for(int i=0;i<authRoleList.size();i++){
//            authRoleList.get(i).setName("ROLE_"+authRoleList.get(i).getName());
//        }
//        user.setRoles(authRoleList);
       System.out.println("user.getEmail()=="+user.getEmail());
        System.out.println("user.getPassword()=="+user.getPassword());
        ArrayList<GrantedAuthority> arrayList= (ArrayList<GrantedAuthority>) user.getAuthorities();
        for(GrantedAuthority x:arrayList){
            System.out.println("x.getAuthority()==========="+x.getAuthority());
        }
       // System.out.println(arrayList.get(0).getAuthority());
//        List<AuthAccessList> authAccessLists=userOptional.get().getRoles().get(0).getAccessLists();
        User myUser;

        myUser = new User(user.getEmail(),user.getPassword(),user.getAuthorities());

        return myUser;

//        return repository.findByEmail(username).map(myUser-> User.builder().username(myUser.getEmail()).
//                password(myUser.getPassword()).roles(myUser.getRoles().get(0).getName()).build()
//        ).orElseThrow(() -> new UsernameNotFoundException("User not found"));

//        .stream()
//                .map(r-> new SimpleGrantedAuthority(r.getName()))
//                .collect(Collectors.toList())
    }
}

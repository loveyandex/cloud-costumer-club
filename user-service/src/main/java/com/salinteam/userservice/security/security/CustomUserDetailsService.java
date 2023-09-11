package com.salinteam.userservice.security.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Override
    @Transactional
    //TODO
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        return null;
    }

    @Transactional
    //TODO
    public UserDetails loadCompanyById(Long id, String jwt) {

        return CompanyPrincipal.create(null, jwt);
    }

    public UserDetails loadCompanyByClaims(Claims claims, String jwt) {
        HashMap companyhash = (HashMap) claims.get("company");
        Long id = Long.valueOf((Integer) companyhash.get("id"));
        ArrayList authorities = (ArrayList) companyhash.get("authorities");

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();


        for (Object authority : authorities) {
            HashMap authority1 = (HashMap) authority;
            String rolename = (String) authority1.get("authority");
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(rolename);
            grantedAuthorities.add(simpleGrantedAuthority);
        }

        String username = (String) companyhash.get("username");
        return new CompanyPrincipal(id, username, null, jwt, grantedAuthorities);


    }
}


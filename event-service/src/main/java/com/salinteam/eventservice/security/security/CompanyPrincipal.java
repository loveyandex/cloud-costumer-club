package com.salinteam.eventservice.security.security;

import com.salinteam.eventservice.security.model.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class CompanyPrincipal implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String jwt;

    private Collection<? extends GrantedAuthority> authorities;

    public CompanyPrincipal(Long id, String username, String password, String jwt, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.jwt = jwt;
        this.authorities = authorities;
    }

    public static CompanyPrincipal create(Company company, String jwt) {
        List<GrantedAuthority> authorities = company.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new CompanyPrincipal(
                company.getId(),
                company.getUsername(),
                company.getPassword(),
                jwt, authorities
        );
    }

    public Long getId() {
        return id;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPrincipal that = (CompanyPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}

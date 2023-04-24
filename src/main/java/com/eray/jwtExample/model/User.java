package com.eray.jwtExample.model;

import com.eray.jwtExample.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private  String email;
    private String password;

    @Enumerated(EnumType.STRING) // degerimizin enum tipinde olacagini ve veri tabaninda string olarak tutulmasini soyledik
    Role role;

    // kullanicinin rollerini donuyoruz
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // kullanicinin expire suresinin dolup dolmadigini belli ediyoruz
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // kullanici hesabi kitli mi degil mi
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // kullanici aktif mi
    @Override
    public boolean isEnabled() {
        return true;
    }
}

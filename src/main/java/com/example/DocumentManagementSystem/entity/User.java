package com.example.DocumentManagementSystem.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

     private String password;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public Rank getRank() { return rank; }
    public void setRank(Rank rank) { this.rank = rank; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Kullanıcının rütbesini (Rank) Spring Security'nin anlayacağı bir "Yetki/Role" formatına çeviriyoruz.
        String roleName = (this.rank != null && this.rank.getName() != null) ? this.rank.getName().toUpperCase() : "USER";
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // Sistemde eşsiz (unique) olan alanımız e-posta olduğu için,
        // kullanıcı adı (username) olarak email adresini kullanıyoruz.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  }
    @Override
    public boolean isAccountNonLocked() {
        return true;  }
    @Override
    public boolean isCredentialsNonExpired() {
        return true; }
    @Override
    public boolean isEnabled() {
        return true;   }
}
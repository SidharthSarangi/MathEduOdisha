package com.ocmse.user_service.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID id ;

    @Column(unique = true, nullable = false)
    private String email;

    private String password; // bcrypt hash
    private String name;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // default role

    public enum Role { USER, AUTHOR, EDITOR, ADMIN }

    private String provider ;
    private String providerId ;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}

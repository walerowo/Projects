package ru.kata.spring.boot_security.demo.model;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Column(nullable = false)
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Column(nullable = false)
    @Min(value = 0, message = "Age should be greater than 0")
    private Integer age;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,  nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(){

    }
    public User(String name, String lastName, Integer age, String username, String password, Set<Role> roles) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}

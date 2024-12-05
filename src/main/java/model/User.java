package model;

import java.util.*;

// BEAN- atributele sun private si avem acces la ele doar prin metode publice + constructor fara parametrii
public class User {

    private Long id;
    private String username;
    private String password;
    private List<Role> roles;
    private List<Book> soldBooks;
    private int totalSoldBooks;
    private int totalSalesValue;
    public Long getCurrentUserId() {
        if (id == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getTotalSoldBooks() {
        return totalSoldBooks;
    }

    public void setTotalSoldBooks(int totalSoldBooks) {
        this.totalSoldBooks = totalSoldBooks;
    }

    public int getTotalSalesValue() {
        return totalSalesValue;
    }

    public void setTotalSalesValue(int totalSalesValue) {
        this.totalSalesValue = totalSalesValue;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public boolean hasRole(String roleName) {
        if (roles == null) {
            return false;
        }

        for (Role role : roles) {
            if (role.getRole().equalsIgnoreCase(roleName)) {
                return true;  // role found
            }
        }
        return false;  // role not found
    }

    public List<Book> getSoldBooks() {
        return soldBooks;
    }
    public void setSoldBooks(List<Book> soldBooks) {
        this.soldBooks = soldBooks;
    }
}
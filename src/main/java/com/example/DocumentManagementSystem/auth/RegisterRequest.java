package com.example.DocumentManagementSystem.auth;

public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Long departmentId;
    private Long rankId;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String password, Long departmentId, Long rankId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.departmentId = departmentId;
        this.rankId = rankId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public Long getRankId() {
        return rankId;
    }
    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }
}

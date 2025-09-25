package com.clinic.patient.dto;

import com.clinic.patient.models.Role;

public class LoginResponse {
    private boolean success;
    private String message;
    private UserData user;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, UserData user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
    public static class UserData {
        private Long id;
        private String fullName;
        private String email;
        private Role role;

        public UserData(Long id, String fullName, String email, Role role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
        }

        // Getters
        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public Role getRole() { return role; }
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }
}

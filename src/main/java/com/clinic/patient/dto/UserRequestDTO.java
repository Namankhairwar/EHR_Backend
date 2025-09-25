package com.clinic.patient.dto;


import com.clinic.patient.models.Role;


public class UserRequestDTO {

    private String fullName;
    private String email;
    private String password;
    private Role role;

    // ---- Constructors ----
    public UserRequestDTO() {}

    public UserRequestDTO(String fullName, String email, String password, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ---- Getters & Setters ----
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // ---- Builder ----
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fullName;
        private String email;
        private String password;
        private Role role;

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public UserRequestDTO build() {
            return new UserRequestDTO(fullName, email, password, role);
        }
    }
}

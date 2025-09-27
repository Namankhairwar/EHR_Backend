package com.clinic.patient.dto;

public class DoctorResponseDTO {
    private String name;
    private String specialization;
    private String phone;

    public DoctorResponseDTO() {}

    public DoctorResponseDTO(String name, String specialization, String phone) {
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

package com.ceiba.medisalud.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class DoctorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 80)
    private String specialty;

    @Column(length = 30)
    private String phone;

    @Column(length = 120)
    private String email;

    protected DoctorJpaEntity() {
    }

    public DoctorJpaEntity(Long id, String fullName, String specialty, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}

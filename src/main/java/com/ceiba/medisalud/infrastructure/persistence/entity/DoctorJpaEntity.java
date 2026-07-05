package com.ceiba.medisalud.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents the JPA persistence entity for doctor records.
 */
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

    /**
     * Creates a new DoctorJpaEntity instance.
     */
    protected DoctorJpaEntity() {
    }

    /**
     * Creates a new DoctorJpaEntity instance.
     */
    public DoctorJpaEntity(Long id, String fullName, String specialty, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Returns the id value.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the fullName value.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the specialty value.
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Returns the phone value.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the email value.
     */
    public String getEmail() {
        return email;
    }
}

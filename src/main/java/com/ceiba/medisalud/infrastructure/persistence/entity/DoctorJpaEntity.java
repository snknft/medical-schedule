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
     * Creates an empty doctor entity required by JPA.
     */
    protected DoctorJpaEntity() {
    }

    /**
     * Creates a doctor entity with persisted column values.
     *
     * @param id database identifier
     * @param fullName complete name
     * @param specialty medical specialty
     * @param phone contact phone
     * @param email contact email
     */
    public DoctorJpaEntity(Long id, String fullName, String specialty, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
        this.phone = phone;
        this.email = email;
    }

    /**
     * Returns the generated database identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the persisted complete name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the persisted specialty.
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Returns the persisted contact phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the persisted contact email.
     */
    public String getEmail() {
        return email;
    }
}

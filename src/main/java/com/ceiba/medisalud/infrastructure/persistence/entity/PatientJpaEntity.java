package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents the JPA persistence entity for patient records.
 */
@Entity
@Table(
        name = "patients",
        uniqueConstraints = @UniqueConstraint(name = "uk_patient_document", columnNames = "document_number")
)
public class PatientJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "document_number", nullable = false, length = 30)
    private String documentNumber;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Creates an empty patient entity required by JPA.
     */
    protected PatientJpaEntity() {
    }

    /**
     * Creates a patient entity with persisted column values.
     *
     * @param id database identifier
     * @param fullName complete name
     * @param documentNumber identity document number
     * @param phone contact phone
     * @param email contact email
     * @param birthDate birth date
     */
    public PatientJpaEntity(Long id, String fullName, String documentNumber, String phone, String email, LocalDate birthDate) {
        this.id = id;
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
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
     * Returns the persisted identity document number.
     */
    public String getDocumentNumber() {
        return documentNumber;
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

    /**
     * Returns the persisted birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }
}

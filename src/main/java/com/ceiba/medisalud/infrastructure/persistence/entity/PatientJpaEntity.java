package com.ceiba.medisalud.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = @UniqueConstraint(name = "uk_patient_document", columnNames = "document_number")
)
/**
 * Represents the JPA persistence entity for patient records.
 */
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
     * Creates a new PatientJpaEntity instance.
     */
    protected PatientJpaEntity() {
    }

    /**
     * Creates a new PatientJpaEntity instance.
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
     * Returns the documentNumber value.
     */
    public String getDocumentNumber() {
        return documentNumber;
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

    /**
     * Returns the birthDate value.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }
}
